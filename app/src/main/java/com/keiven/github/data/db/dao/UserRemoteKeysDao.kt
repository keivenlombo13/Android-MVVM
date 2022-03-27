package com.keiven.github.data.db.dao

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keiven.github.data.db.entity.Users

@Dao
interface UserRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<Users.UserRemoteKeys>)

    @Query("SELECT * FROM user_remote_keys WHERE userId = :id")
    fun remoteKeysByUserId(id: Int): Users.UserRemoteKeys?

    @Query("DELETE FROM user_remote_keys")
    fun clearRemoteKeys()
}