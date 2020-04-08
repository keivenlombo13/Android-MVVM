package com.keiven.github.data.network

import io.reactivex.annotations.Nullable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


open class Network<T>(private val client: OkHttpClient, private val clazz: Class<T>) {

    @Nullable
    private var service: T? = null

    private fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    open fun service(): T {
        if (service == null) {
            val retrofit = provideRetrofit()
            service = retrofit.create(clazz)
        }
        return service!!
    }

    private fun getBaseUrl():String = "https://api.github.com/"
}