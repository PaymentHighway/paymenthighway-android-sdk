package io.paymenthighway.demo.backendadapterexample

import io.paymenthighway.sdk.model.ApiResultInfo
import io.paymenthighway.sdk.model.TransactionId
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.Executors

// From server we can get either the ApiResult either the TransactionToken
internal data class TransactionTokenResult(val result: ApiResultInfo? = null, val token: String? = null)

internal interface BackendAdapterEndpointExample {

    @GET("/mobile-key")
    fun transactionId(): Call<TransactionId>

    @POST("/tokenization/{transactionId}")
    fun tokenizeTransaction(@Path("transactionId") transactionId: TransactionId): Call<TransactionTokenResult>

    companion object {
        fun create(): BackendAdapterEndpointExample {
            val retrofit = Retrofit.Builder()
                            .baseUrl("http://54.194.196.206:8081")
                            .callbackExecutor(Executors.newCachedThreadPool())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

            return retrofit.create(BackendAdapterEndpointExample::class.java);
        }
    }
}