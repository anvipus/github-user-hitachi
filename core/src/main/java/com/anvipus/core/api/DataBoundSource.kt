package com.anvipus.core.api

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.anvipus.core.models.ApiResponse
import com.anvipus.core.models.Resource
import com.anvipus.core.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 * @param <ResultType> Type for the Resource data.
 * @param <RequestType> Type for the API response.
</RequestType></ResultType> */
@Suppress("LeakingThis")
abstract class DataBoundSource<ResultType, RequestType>(){

    companion object {
        const val TAG: String = "NetworkBoundDataSource"
    }

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        val dbSource = loadFromDb()

        result.addSource(dbSource){ data ->
            result.removeSource(dbSource)

            setValue(Resource.loading(data))

            if ((data is ArrayList<*>)) {
                if (data.size > 0) {
                    if (shouldFetch(data)) {
                        fetchFromNetwork(dbSource)
                    } else {
                        result.addSource(dbSource) {
                            setValue(Resource.success(it))
                        }
                    }
                } else {
                    fetchFromNetwork(dbSource)
                }
            } else {
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource)
                } else {
                    result.addSource(dbSource) {
                        setValue(Resource.success(it))
                    }
                }
            }
        }
    }

    private fun setValue(newValue: Resource<ResultType>) {
        // replace value if not equal to previous value
        // to avoid sending same data to LiveData
        // as it will cause unnecessary UI update
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {

        // initiate api request
        // used async to execute in background thread
        val apiCall = GlobalScope.async {
            try{
                createCall().execute()
            }catch (ignore: IOException){
                null
            }
        }

        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
//        result.addSource(dbSource){
//            setValue(Resource.loading(it))
//        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val apiResponse = apiCall.await()
                val apiBody = apiResponse?.body() as RequestType
                val data = processResponse(apiBody)

                if (apiResponse != null && apiResponse.isSuccessful && data != null) {
                    result.removeSource(dbSource)
                    launch(Dispatchers.IO){ clearSource() }.join()
                    launch(Dispatchers.IO){
                        saveCallResult(data)
                    }.join()

                    // we specially request a new live data,
                    // otherwise we will get immediately last cached value,
                    // which may not be updated with latest results received from network.
                    result.addSource(loadFromDb()){ newData ->
                        if(newData != null)setValue(Resource.success(newData))
                    }
                }else {
                    onFetchFailed()
                    result.addSource(dbSource) { data ->
                        result.removeSource(dbSource)
                        if ((data is ArrayList<*>)) {
                            if (data.size > 0) {
                                result.addSource(dbSource){
                                    setValue(Resource.success(it))
                                }
                            }else{
                                setValue(
                                    Resource.error("Terdapat gangguan koneksi, mohon periksa koneksi dan mencoba kembali"))
                            }
                        }
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
                setValue(
                    Resource.error("Terdapat gangguan koneksi, mohon periksa koneksi dan mencoba kembali"))
            }

        }

    }

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    fun asLiveData() = result as LiveData<Resource<ResultType>>

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract fun saveCallResult(data: ResultType?)

    @WorkerThread
    protected open fun clearSource(){}

    // Optional, called when there is a need to process the API response type before saving to db
    @Suppress("UNCHECKED_CAST")
    @WorkerThread
    protected open fun processResponse(body: RequestType) = body as ResultType?

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // Called to create the API call.
    @WorkerThread
    protected abstract fun createCall(): Call<RequestType>

}