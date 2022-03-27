package com.keiven.github.data.db.entity

import com.keiven.github.data.network.response.Result
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object UsersMapper {
    fun transform(response: Result<List<Users.User>>): Users {
        return with(response) {
            Users(
                totalCount = totalCount,
                incompleteResults = incompleteResults,
                users = items ?: ArrayList()
            )
        }
    }
}