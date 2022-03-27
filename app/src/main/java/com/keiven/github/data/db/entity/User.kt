package com.keiven.github.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Parcelize
data class Users(
    val totalCount: Int = 0,
    val incompleteResults: Boolean = false,
    val users: List<User>
) : Parcelable {

    @IgnoredOnParcel
    val endOfPage = incompleteResults == true

    @Parcelize
    @Entity(tableName = "users")
    data class User(
        @field:SerializedName("id")
        @PrimaryKey val id: Int,
        @field:SerializedName("avatar_url")
        val avatarUrl: String,
        @field:SerializedName("login")
        val login: String
    ): Parcelable

    @Parcelize
    @Entity(tableName = "user_remote_keys")
    data class UserRemoteKeys(
        @PrimaryKey val userId: Int,
        val prevKey: Int?,
        val nextKey: Int?
    ) : Parcelable
}