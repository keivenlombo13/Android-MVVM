package com.keiven.github.data.db.dao

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keiven.github.data.db.entity.Users

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<Users.User>?)

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun selectAll(): PagingSource<Int, Users.User>

    @Query("DELETE FROM users where id = :id")
    suspend fun deleteUser(id: Int)

    @Query("DELETE FROM users")
    fun clearUsers()
}