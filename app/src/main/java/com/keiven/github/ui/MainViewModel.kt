package com.keiven.github.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.rxjava3.cachedIn
import com.keiven.github.base.BaseViewModel
import com.keiven.github.data.db.entity.Users
import com.keiven.github.data.network.response.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@SuppressLint("CheckResult")
class MainViewModel(
    private val repository: MainRepository
) : BaseViewModel() {

//    private val TAG = "MainViewModel"
//    private val subscriptions = CompositeDisposable()
//
//    private val loadingState: MutableLiveData<Resource> = MutableLiveData()
//
//    fun getUsers(): LiveData<PagingData<Users.User>> = users
//    fun loadingState(): LiveData<Resource> = loadingState
//
//    val querySubmitListener : (String) -> Unit = { query ->
//        reset()
//        fetchUser(query)
//    }
//
//    fun fetchUser(query: String) {
//        repository.getUsers(query)
//            .value
//    }
//
//    private fun watchForLoadingProgress() {
//        repository.getLoadingStateWatcher()
//            .doOnSubscribe { subscriptions.add(it) }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                loadingState.value = it
//            }, {
//                it.message?.let {
//                    Log.e(TAG, it )
//                }
//            })
//    }
//
//    fun reset() {
//        users.value = null
//        repository.reset()
//        subscriptions.clear()
//        repository.onCleared()
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        subscriptions.clear()
//        repository.onCleared()
//    }

    fun setQuery(query: String = "") {
        repository.setQuery(query)
    }

    fun getUser(): Flowable<PagingData<Users.User>> {
        return repository
            .getUsers()
            .cachedIn(viewModelScope)
    }
}