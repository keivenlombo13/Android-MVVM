package com.keiven.github.di

import com.keiven.github.data.db.GithubDb
import com.keiven.github.data.network.HttpClientBuilder
import com.keiven.github.data.network.api.GithubApi
import com.keiven.github.data.network.source.GithubSource
import com.keiven.github.ui.MainRepository
import com.keiven.github.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

object Modules {
    val applicationModule = module {
        single { GithubDb(get()) }
        single { get<GithubDb>().userDao() }
        single { get<GithubDb>().userRemoteDao() }

        // network
        single { HttpClientBuilder().client }

        // source
        single { GithubSource(get(), get(), get(), get()) }

        // API service
        single { get<Retrofit>().create(GithubApi::class.java) }

        // Repository
        single { MainRepository(get(), get()) }

        // ViewModel
        viewModel { MainViewModel(get()) }
    }
}