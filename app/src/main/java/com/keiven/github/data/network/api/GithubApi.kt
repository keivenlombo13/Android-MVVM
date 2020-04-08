package com.keiven.github.data.network.api

import com.keiven.github.data.db.entity.User
import com.keiven.github.data.network.response.Result
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/users?q=pi&per_page=1&page=3")
    fun users(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Observable<Response<Result<List<User>>>>
}