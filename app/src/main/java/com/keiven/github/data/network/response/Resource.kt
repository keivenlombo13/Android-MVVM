package com.keiven.github.data.network.response

import com.keiven.github.data.db.entity.Status

data class Resource(val status: Status, val message: String?) {
    companion object {
        fun success(): Resource {
            return Resource(Status.SUCCESS, null)
        }

        fun error(msg: String): Resource {
            return Resource(Status.ERROR, msg)
        }

        fun loading(): Resource {
            return Resource(Status.LOADING, null)
        }

        fun empty(): Resource {
            return Resource(Status.EMPTY, null)
        }
    }
}