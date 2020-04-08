package com.keiven.github.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.keiven.github.data.db.dao.UserDao
import com.keiven.github.data.db.entity.User

@Database(
    entities = [
        User::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var instance: GithubDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, GithubDb::class.java, "github.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}