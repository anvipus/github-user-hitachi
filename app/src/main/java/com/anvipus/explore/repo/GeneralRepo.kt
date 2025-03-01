package com.anvipus.explore.repo

import androidx.lifecycle.LiveData
import com.anvipus.core.api.ApiCall
import com.anvipus.core.models.Resource
import com.anvipus.core.models.UserDetail
import com.anvipus.core.models.Users
import com.anvipus.core.utils.Constants
import com.anvipus.explore.api.GeneralApi
import retrofit2.Call
import javax.inject.Inject

class GeneralRepo @Inject constructor(
    private val api: GeneralApi
){
    fun getListUsers(since:Int): LiveData<Resource<List<Users>>> = object : ApiCall<List<Users>, List<Users>>(){
        override fun createCall(): Call<List<Users>> {
            val params = HashMap<String, String>()
            params["since"] = since.toString()
            params["per_page"] = "30"
            return api.getListUser(accessToken = Constants.BEARER + "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", params = params)
        }
    }.asLiveData()

    fun getDetailUser(username:String): LiveData<Resource<UserDetail>> = object : ApiCall<UserDetail, UserDetail>(){
        override fun createCall(): Call<UserDetail> {

            return api.getDetailUser(accessToken = Constants.BEARER + "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", username = username)
        }
    }.asLiveData()
}