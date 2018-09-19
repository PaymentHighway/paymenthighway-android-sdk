package io.paymenthighway.demo.backendadapterexample

import io.paymenthighway.sdk.model.ApiResultInfo
import io.paymenthighway.sdk.model.ApiResultInterface
import io.paymenthighway.sdk.model.TransactionId
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.Executors

internal data class TransactionTokenApiResult(override val result: ApiResultInfo, val token: String? = null, val card: TransactionCard? = null): ApiResultInterface

internal interface BackendAdapterEndpointExample {

    @GET("/mobile-key")
    fun transactionId(): Call<TransactionId>

    @POST("/tokenization/{transactionId}")
    fun tokenizeTransaction(@Path("transactionId") transactionId: TransactionId): Call<TransactionTokenApiResult>

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