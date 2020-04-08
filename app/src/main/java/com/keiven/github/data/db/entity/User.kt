package com.keiven.github.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName


@Entity(tableName = "user",
    primaryKeys = ["login"],
    indices = [
        Index("login")
    ]
)
data class User(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("avatar_url")
    val avatarUrl: String,
    @field:SerializedName("login")
    val login: String
)