package com.keiven.github.data.network.api

import com.keiven.github.data.db.entity.Users
import com.keiven.github.data.network.response.Result
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/users")
    fun users(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Single<Response<Result<List<Users.User>>>>
}