package com.anvipus.explore.api

import com.anvipus.core.models.UserDetail
import com.anvipus.core.models.Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface GeneralApi {
    @GET("users")
    fun getListUser(
        @Header("Authorization") accessToken: String,
        @QueryMap params: Map<String, String>
    ): Call<List<Users>>

    @GET("users/{username}")
    fun getDetailUser(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String
    ): Call<UserDetail>
}