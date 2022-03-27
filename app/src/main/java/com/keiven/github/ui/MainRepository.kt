package com.keiven.github.ui

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.paging.rxjava3.flowable
import com.keiven.github.data.db.GithubDb
import com.keiven.github.data.db.dao.UserDao
import com.keiven.github.data.db.dao.UserRemoteKeysDao
import com.keiven.github.data.network.api.GithubApi
import com.keiven.github.data.network.response.Resource
import com.keiven.github.data.network.source.GithubPagingSource
import com.keiven.github.data.network.source.GithubSource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class MainRepository (
    private val userDao: UserDao,
    private val source: GithubSource
) {
    private val loadingStateBehaviorSubject = BehaviorSubject.create<Resource>()

    fun getLoadingStateWatcher(): Observable<Resource> = loadingStateBehaviorSubject

    fun setQuery(query: String) {
        source.query = query
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getUsers() = Pager(
        config = PagingConfig(
            pageSize = 2,
            prefetchDistance = 2
        ),
        remoteMediator = source,
        pagingSourceFactory = { userDao.selectAll() }

        /** local paging only **/
//        pagingSourceFactory = { GithubPagingSource(
//            service = service,
//            query = query
//        ) }
    ).flowable

}

