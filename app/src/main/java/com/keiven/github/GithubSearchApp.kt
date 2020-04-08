package com.keiven.github

import android.app.Application
import com.keiven.github.di.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GithubSearchApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initInjection()
    }

    fun initInjection(){
        startKoin {
            androidContext(this@GithubSearchApp)
            modules(Modules.applicationModule)
        }
    }
}