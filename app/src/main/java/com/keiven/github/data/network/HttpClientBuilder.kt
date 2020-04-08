package com.keiven.github.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class HttpClientBuilder() {

    val client = okHttpClientBuilder(OkHttpClient.Builder()).connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder = builder
        .addInterceptor(loggingInterceptor())

    private fun loggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("Api", message)
            }
        })
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }
}