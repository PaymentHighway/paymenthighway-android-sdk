package io.paymenthighway.demo.backendadapterexample

import io.paymenthighway.sdk.BackendAdapter
import io.paymenthighway.sdk.model.TransactionId
import io.paymenthighway.sdk.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvalidDataException(message: String) : Exception(message)
class UnknowErrorException(message: String = "") : Exception(message)
class NetworkErrorException(message: String, cause: Throwable) : Exception(message, cause)

class BackendAdapterExample: BackendAdapter<TransactionToken, Exception> {
    override fun getTransactionId(completion: (Result<TransactionId, Exception>) -> Unit) {
        val apiService = BackendAdapterEndpointExample.create()
        val api = apiService.transactionKey()
        api.enqueue(object : Callback<TransactionId> {
            override fun onResponse(call: Call<TransactionId>?, response: Response<TransactionId>?) {
                val result = response?.body()?.let { Result.success(it) } ?:
                             run { Result.failure(InvalidDataException("Received null data!")) }
                completion(result)
            }

            override fun onFailure(call: Call<TransactionId>?, t: Throwable?) {
                val result = t?.let { Result.failure(NetworkErrorException("Network error", t))  } ?: run { Result.failure(UnknowErrorException()) }
                completion(result)
            }
        })

    }

    override fun cardAdded(transactionId: TransactionId, completion: (Result<TransactionToken, Exception>) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}