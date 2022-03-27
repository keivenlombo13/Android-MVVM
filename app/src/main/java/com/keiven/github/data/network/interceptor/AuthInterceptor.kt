package com.keiven.github.data.network.interceptor

import android.content.Context
import com.keiven.github.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        BuildConfig.GITHUB_TOKEN?.let {
            requestBuilder.addHeader("Authorization", "token $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}