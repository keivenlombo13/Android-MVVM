package com.keiven.github.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.keiven.github.base.BaseViewModel
import com.keiven.github.data.db.entity.User
import com.keiven.github.data.network.response.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class MainViewModel(
    private val repository: MainRepository
) : BaseViewModel() {

    private val TAG = "MainViewModel"
    private val subscriptions = CompositeDisposable()

    private val users: MutableLiveData<PagedList<User>> = MutableLiveData()
    private val loadingState: MutableLiveData<Resource> = MutableLiveData()

    fun getUsers(): LiveData<PagedList<User>> = users
    fun loadingState(): LiveData<Resource> = loadingState

    val querySubmitListener : (String) -> Unit = { query ->
        reset()
        fetchUser(query)
    }

    fun fetchUser(query: String) {
        watchForLoadingProgress()
        repository.getUsers(query)
            .doOnSubscribe { subscriptions.add(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                users.value = it
            }, { Log.e(TAG, it.message) })
    }

    private fun watchForLoadingProgress() {
        repository.getLoadingStateWatcher()
            .doOnSubscribe { subscriptions.add(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadingState.value = it
            }, {
                Log.e(TAG, it.message)
            })
    }

    fun reset() {
        users.value = null
        repository.reset()
        subscriptions.clear()
        repository.onCleared()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
        repository.onCleared()
    }
}