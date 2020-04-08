package com.keiven.github.data.network.source

import com.keiven.github.data.network.Network
import com.keiven.github.data.network.api.GithubApi
import okhttp3.OkHttpClient

class GithubSource(client: OkHttpClient): Network<GithubApi>(client, GithubApi::class.java) {
    fun users(query: String, perPage: Int = 20, page: Int = 1) = service().users(query, perPage, page)
}