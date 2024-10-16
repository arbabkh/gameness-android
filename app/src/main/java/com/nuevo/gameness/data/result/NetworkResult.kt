package com.nuevo.gameness.data.result

import android.util.Log

sealed class NetworkResult<out R> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    object SuccessWithNoContent : NetworkResult<Nothing>()
    data class Loading(val isLoading: Boolean) : NetworkResult<Nothing>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>() {
        fun show() = Log.e("NETWORK RESULT ERROR",
            "MESSAGE: $message" + if (code != null) ", CODE: $code" else "")
    }
}