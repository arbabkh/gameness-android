package com.nuevo.gameness.data.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Response

class StateLiveData<T>(
    private val job: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiCall: suspend () -> Response<T>
) {

    private val _state: MutableLiveData<NetworkResult<T>> = MutableLiveData()
    val state: LiveData<NetworkResult<T>> = _state

    fun fetchData() {
        job.launch {
            getResult().collect{ result ->
                _state.value = result
                Log.e("sonuc", result.toString());
            }
        }
    }

    private suspend fun getResult(): Flow<NetworkResult<T>> {
        return flow {
            emit(NetworkResult.Loading(true))
            emit(safeApiCall())
            emit(NetworkResult.Loading(false))
        }.flowOn(dispatcher)
    }

    private suspend fun safeApiCall(): NetworkResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body == null) NetworkResult.SuccessWithNoContent
                else NetworkResult.Success(body)
            } else NetworkResult.Error(response.message(), response.code())
        } catch (e: Exception) {
            NetworkResult.Error(e.message.toString())
        }
    }

}