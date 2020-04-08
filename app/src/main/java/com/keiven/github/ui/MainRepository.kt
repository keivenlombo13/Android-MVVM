package com.keiven.github.ui

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.keiven.github.data.db.dao.UserDao
import com.keiven.github.data.db.entity.User
import com.keiven.github.data.network.response.Resource
import com.keiven.github.data.network.source.GithubSource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
class MainRepository (
    private val githubSource: GithubSource,
    private val userDao: UserDao
) {
    private val paging: MainPaging? = null
    private val loadingStateBehaviorSubject = BehaviorSubject.create<Resource>()

    fun getLoadingStateWatcher(): Observable<Resource> = loadingStateBehaviorSubject

    fun getUsers(query: String): Observable<PagedList<User>> {

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build()

        val paging = MainPaging(query, githubSource, userDao, loadingStateBehaviorSubject, 1)
        return RxPagedListBuilder(userDao.search(query), config)
            .setFetchScheduler(Schedulers.io())
            .setBoundaryCallback(paging)
            .buildObservable()
    }

    fun onCleared() {
        paging?.onCleared()
    }

    fun reset() {
        paging?.reset()
    }

}

