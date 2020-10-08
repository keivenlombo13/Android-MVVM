package com.keiven.github.di

import com.keiven.github.data.db.GithubDb
import com.keiven.github.data.network.HttpClientBuilder
import com.keiven.github.data.network.source.GithubSource
import com.keiven.github.ui.MainRepository
import com.keiven.github.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {
    val applicationModule = module {

        single { GithubDb(get()) }
        single { get<GithubDb>().userDao() }
        single { HttpClientBuilder().client }
        single { GithubSource(get()) }
        single { MainRepository(get(), get()) }
        viewModel { MainViewModel(get()) }
    }
}