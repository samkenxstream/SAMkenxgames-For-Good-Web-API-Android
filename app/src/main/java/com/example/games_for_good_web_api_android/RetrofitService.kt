package com.example.games_for_good_web_api_android

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("email") email:String, @Field("password") password:String):ResponseBody

    @POST("login")
    suspend fun login2(@Body user:User):ResponseBody
}