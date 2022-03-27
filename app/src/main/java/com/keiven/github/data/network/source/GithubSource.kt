package com.keiven.github.data.network.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import androidx.paging.rxjava3.RxRemoteMediator
import com.keiven.github.data.db.GithubDb
import com.keiven.github.data.db.dao.UserDao
import com.keiven.github.data.db.dao.UserRemoteKeysDao
import com.keiven.github.data.db.entity.Users
import com.keiven.github.data.db.entity.UsersMapper
import com.keiven.github.data.network.api.GithubApi
import com.keiven.github.data.network.response.Result
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import retrofit2.Retrofit
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class GithubSource(
    private val service: GithubApi,
    private val userDao: UserDao,
    private val userRemoteKeysDao: UserRemoteKeysDao,
    private val githubDb: GithubDb,
    ): RxRemoteMediator<Int, Users.User>() {

    var query = ""

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, Users.User>
    ): Single<MediatorResult> {
        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .map {
                when (it) {
                    LoadType.REFRESH -> {
                        return@map 1
                    }
                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                            ?: throw InvalidObjectException("Result is empty")

                        remoteKeys.prevKey ?: INVALID_PAGE
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                            ?: throw InvalidObjectException("Result is empty")

                        remoteKeys.nextKey ?: INVALID_PAGE
                    }
                }
            }
            .flatMap { page ->
                if (page == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                }
                else if(GithubSource@query.isNullOrBlank().not()) {
                    service.users(query = GithubSource@query, page = page, perPage = 20)
                        .map {
                            UsersMapper.transform(it.body()!!)
                        }
                        .map { insertToDb(page, loadType, it) }
                        .map<MediatorResult> { MediatorResult.Success(endOfPaginationReached = it.endOfPage) }
                        .onErrorReturn { MediatorResult.Error(it) }
                } else {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                }

            }
            .onErrorReturn { MediatorResult.Error(it) }
    }

    private fun insertToDb(page: Int, loadType: LoadType, data: Users): Users {
        githubDb.runInTransaction {
            if (loadType == LoadType.REFRESH) {
                userRemoteKeysDao.clearRemoteKeys()
                userDao.clearUsers()
            }

            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (data.endOfPage) null else page + 1
            val keys = data.users.map {
                Users.UserRemoteKeys(userId = it.id, prevKey = prevKey, nextKey = nextKey)
            }
            userRemoteKeysDao.insertAll(keys)
            userDao.insert(data.users)
        }


        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, Users.User>): Users.UserRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { repo ->
            userRemoteKeysDao.remoteKeysByUserId(repo.id)
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Users.User>): Users.UserRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { user ->
            userRemoteKeysDao.remoteKeysByUserId(user.id)
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Users.User>): Users.UserRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                userRemoteKeysDao.remoteKeysByUserId(id)
            }
        }
    }

    companion object {
        const val INVALID_PAGE = -1
    }
}