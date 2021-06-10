package com.example.games_for_good_web_api_android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Exception

class WebAPIViewmodel:  ViewModel() {
    val retrofitMindgrub = Retrofit.Builder().baseUrl("https://games-for-good-demo.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofitMindgrub.create<RetrofitService>()

    fun postLogin(user:String, password:String, handler:CompletionHandler<Any>)
    {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = service.login2(User(user,password))
                handler.onResult(response.string())
            }
            catch (e:Exception)
            {
                handler.onError(e.toString())
            }
        }
    }
}