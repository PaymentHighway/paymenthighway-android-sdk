package io.paymenthighway.sdk.util

import io.paymenthighway.sdk.exception.InvalidDataException
import io.paymenthighway.sdk.exception.NetworkErrorException
import io.paymenthighway.sdk.exception.UnknowErrorException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallbackResult<T>(val completion: (Result<T, Exception>) -> Unit) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        val result = response.body()?.let { Result.success(it) } ?:
                     run { Result.failure(InvalidDataException("Received null data!")) }
        completion(result)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        completion(Result.failure(NetworkErrorException(t.message ?: "", cause = t)) )
    }
}

class CallbackResultConvert<T, R>(val completion: (Result<R, Exception>) -> Unit, val convert: (T) -> Result<R, Exception>) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        val result = response.body()?.let { convert(it) } ?:
            run { Result.failure(InvalidDataException("Received null data!")) }
        completion(result)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        completion(Result.failure(NetworkErrorException(t.message ?: "", cause = t)) )
    }
}