package io.paymenthighway.sdk.util

import android.accounts.NetworkErrorException
import io.paymenthighway.sdk.exception.InvalidDataException
import io.paymenthighway.sdk.exception.ResponseErrorException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallbackResult<T>(val completion: (Result<T, Exception>) -> Unit) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful()) {
            val result = response.body()?.let { Result.success(it) }
                    ?: run { Result.failure(InvalidDataException("Received null data!")) }
            completion(result)
        } else {
            completion(Result.failure(ResponseErrorException(response.code(), response.message())))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        completion(Result.failure(NetworkErrorException(t.message ?: "", t)) )
    }
}

class CallbackResultConvert<T, R>(val completion: (Result<R, Exception>) -> Unit, val convert: (T) -> Result<R, Exception>) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful()) {
            val result = response.body()?.let { convert(it) } ?:
                run { Result.failure(InvalidDataException("Received null data!")) }
            completion(result)
        } else {
            completion(Result.failure(ResponseErrorException(response.code(), response.message())))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        completion(Result.failure(NetworkErrorException(t.message ?: "", t)) )
    }
}