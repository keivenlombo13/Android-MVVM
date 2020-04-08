package com.keiven.github.base

import com.keiven.github.internal.ObservableViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(

) : ObservableViewModel() {

    val compose = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compose.clear()
    }

    fun addSubscription(disposable: Disposable) = compose.add(disposable)
}