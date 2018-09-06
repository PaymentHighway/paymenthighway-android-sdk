package io.paymenthighway.demo.backendadapterexample

import io.paymenthighway.sdk.model.TransactionId
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.Executors

interface BackendAdapterEndpointExample {

    @GET("/mobile-key")
    fun transactionKey(): Call<TransactionId>

    @POST("/tokenization/{transactionId}")
    fun tokenizeTransaction(@Path("transactionId") transactionId: TransactionId): Call<TransactionToken>

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