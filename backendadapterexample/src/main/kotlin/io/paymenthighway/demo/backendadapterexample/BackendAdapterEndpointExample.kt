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

    @GET("paymenthighway/transaction")
    fun transactionId(): Call<TransactionId>

    @POST("paymenthighway/tokenization/{transactionId}")
    fun tokenizeTransaction(@Path("transactionId") transactionId: TransactionId): Call<TransactionTokenApiResult>

    companion object {
        fun create(): BackendAdapterEndpointExample {
            val retrofit = Retrofit.Builder()
                            .baseUrl("https://ssocw2l28c.execute-api.eu-west-1.amazonaws.com/staging/")
                            .callbackExecutor(Executors.newCachedThreadPool())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

            return retrofit.create(BackendAdapterEndpointExample::class.java);
        }
    }
}