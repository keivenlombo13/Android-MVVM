package com.keiven.github.data.network.source

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.keiven.github.data.db.entity.Users
import com.keiven.github.data.db.entity.UsersMapper
import com.keiven.github.data.network.api.GithubApi
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class GithubPagingSource(
    private val service: GithubApi,
    private val query: String,
) : RxPagingSource<Int, Users.User>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Users.User>> {
        val position = params.key ?: 1

        return service.users(query = query, perPage = 1, page =  position)
            .subscribeOn(Schedulers.io())
            .map { UsersMapper.transform(it.body()!!) }
            .map { toLoadResult(it, position) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(data: Users, position: Int): LoadResult<Int, Users.User> {
        return LoadResult.Page(
            data = data.users,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (data.incompleteResults) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Users.User>): Int? {
        return state.anchorPosition
    }
}