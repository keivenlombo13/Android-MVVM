package com.keiven.github.data.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keiven.github.data.db.entity.User

@Dao
abstract class UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(users: List<User>?)

    @Query("SELECT * FROM user WHERE login like '%' || :query || '%'")
    abstract fun search(query: String): DataSource.Factory<Int, User>
}