package io.paymenthighway.sdk.util

import android.accounts.NetworkErrorException
import io.paymenthighway.sdk.exception.InvalidDataException
import io.paymenthighway.sdk.exception.ResponseErrorException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Helper for connect the Retrofit2 Callback into Result completion
 *
 * @param T the type of the data for success operation
 * @param completion sdk callback result
 */
class CallbackResult<T>(val completion: (Result<T, Exception>) -> Unit) : Callback<T> {

    /**
     * Implementation of the  of the Retrofit2 onResponse Callback
     */
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful()) {
            val result = response.body()?.let { Result.success(it) }
                    ?: run { Result.failure(InvalidDataException("Received null data!")) }
            completion(result)
        } else {
            completion(Result.failure(ResponseErrorException(response.code(), response.message())))
        }
    }

    /**
     * Implementation of the  of the Retrofit2 onFailure Callback
     */
    override fun onFailure(call: Call<T>, t: Throwable) {
        completion(Result.failure(NetworkErrorException(t.message ?: "", t)) )
    }
}

/**
 * Helper for connect the Retrofit2 Callback into Result completion with a convertion of the data received
 *
 * @param T the type of the data for success operation
 * @param R the type of the data returned after transformation
 * @param completion sdk callback result
 * @param convert convert function to transfort the reveived data R in the completion data T
 */
class CallbackResultConvert<T, R>(val completion: (Result<R, Exception>) -> Unit, val convert: (T) -> Result<R, Exception>) : Callback<T> {

    /**
     * Implementation of the  of the Retrofit2 onResponse Callback
     */
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful()) {
            val result = response.body()?.let { convert(it) } ?:
                run { Result.failure(InvalidDataException("Received null data!")) }
            completion(result)
        } else {
            completion(Result.failure(ResponseErrorException(response.code(), response.message())))
        }
    }

    /**
     * Implementation of the  of the Retrofit2 onFailure Callback
     */
    override fun onFailure(call: Call<T>, t: Throwable) {
        completion(Result.failure(NetworkErrorException(t.message ?: "", t)) )
    }
}