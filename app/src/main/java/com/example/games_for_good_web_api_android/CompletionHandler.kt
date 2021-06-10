package com.example.games_for_good_web_api_android

interface CompletionHandler<T> {
    fun onResult(result:T)
    fun onError(e:String)
}