package com.keiven.github.data.network.response

import com.google.gson.annotations.SerializedName

data class Result<T>(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    val items: T?
)