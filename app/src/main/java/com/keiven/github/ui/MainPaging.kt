package com.keiven.github.ui

import android.annotation.SuppressLint
import androidx.paging.PagedList
import com.keiven.github.data.db.dao.UserDao
import com.keiven.github.data.db.entity.User
import com.keiven.github.data.network.response.Resource
import com.keiven.github.data.network.source.GithubSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

@SuppressLint("CheckResult")
class MainPaging(
    private val query: String,
    private val githubSource: GithubSource,
    private val userDao: UserDao,
    private val progress: BehaviorSubject<Resource>,
    private val initialPage: Int
): PagedList.BoundaryCallback<User>() {

    private val DEFAULT_PAGE_SIZE = 20
    private val subscriptions = CompositeDisposable()
    // Keep reference to what page we are on
    private var pageToRequest = initialPage

    // Avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    // Avoid requesting pages after all pages have been request
    private var allPagesGrabbed = false

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: User) {
        requestAndSaveData()
    }

    override fun onItemAtFrontLoaded(itemAtFront: User) {

    }

    fun onCleared() {
        subscriptions.clear()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return
        if (allPagesGrabbed) return

        githubSource.users(query, page = pageToRequest)
            .doFinally { isRequestInProgress = false }
            .doOnSubscribe { subscriptions.add(it) }
            .doOnSubscribe {
                progress.onNext(Resource.loading())
                isRequestInProgress = true
            }
            .map {
                if(it.isSuccessful) {
                    saveToDatabase(it.body()?.items)
                }
                it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({response ->
                if(response.isSuccessful) {
                    if (response.body()?.items.isNullOrEmpty()) {
                        allPagesGrabbed = true
                    }

                    if (pageToRequest == initialPage && response?.body()?.items?.isEmpty() ?: true) {
                        progress.onNext(Resource.empty())
                    } else {
                        progress.onNext(Resource.success())
                    }

                    pageToRequest++
                    isRequestInProgress = false
                } else {
                    val msg = response.errorBody()?.string()
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                    progress.onNext(Resource.error(errorMsg ?: "unknown error"))
                }
            }, {
                progress.onNext(Resource.error(it.localizedMessage))
            })
    }

    private fun saveToDatabase(data: List<User>?) {
        userDao.insert(data)
    }

    fun reset() {
        pageToRequest = initialPage
    }
}