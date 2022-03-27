package com.keiven.github.data.network

import android.util.Log
import com.keiven.github.data.network.interceptor.AuthInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class HttpClientBuilder() {

    private val okHttpClient = okHttpClientBuilder(OkHttpClient.Builder()).connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder = builder
        .addInterceptor(AuthInterceptor())
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

    val client = Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()


    private fun getBaseUrl():String = "https://api.github.com/"
}