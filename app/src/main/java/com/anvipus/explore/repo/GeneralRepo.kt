package com.anvipus.explore.repo

import androidx.lifecycle.LiveData
import com.anvipus.core.api.ApiCall
import com.anvipus.core.api.DataBoundSource
import com.anvipus.core.models.ApiResponse
import com.anvipus.core.models.Resource
import com.anvipus.core.models.SearchUserListData
import com.anvipus.core.models.UserDetail
import com.anvipus.core.models.Users
import com.anvipus.core.utils.Constants
import com.anvipus.explore.BuildConfig
import com.anvipus.explore.api.GeneralApi
import com.anvipus.explore.db.AppDatabase
import retrofit2.Call
import javax.inject.Inject

class GeneralRepo @Inject constructor(
    private val api: GeneralApi,
    private val db: AppDatabase
){
    fun getDetailUser(username:String): LiveData<Resource<UserDetail>> = object : ApiCall<UserDetail, UserDetail>(){
        override fun createCall(): Call<UserDetail> {

            return api.getDetailUser(accessToken = Constants.BEARER + "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", username = username)
        }
    }.asLiveData()

    fun getListUsers(since:Int) = object : DataBoundSource<List<Users>, List<Users>>(){
        override fun processResponse(body: List<Users>): List<Users>? {
            return body
        }
        override fun saveCallResult(data: List<Users>?) {
            db.runInTransaction {
                db.usersDao().run {
                    deleteAll()
                    try{
                        insertUsers(data!!)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }
            }
        }
        override fun shouldFetch(data: List<Users>?): Boolean {
            return true
        }
        override fun loadFromDb(): LiveData<List<Users>> = db.usersDao().getUsers()
        override fun createCall(): Call<List<Users>> {
            val params = HashMap<String, String>()
            params["since"] = since.toString()
            params["per_page"] = "50"
            return api.getListUser(accessToken = Constants.BEARER + "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", params = params)
        }

    }.asLiveData()


    fun searchUser(query: String): LiveData<Resource<SearchUserListData>> = object : ApiCall<SearchUserListData, SearchUserListData>(){
        override fun createCall(): Call<SearchUserListData> {
            val bodyParam = HashMap<String, String>()
            bodyParam["q"] = query
            return api.getSearchUsers( accessToken = Constants.BEARER + "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", params = bodyParam)
        }
    }.asLiveData()
}