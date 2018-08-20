package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.TransactionId
import io.paymenthighway.sdk.model.TransactionKey
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

//import com.google.gson.Gson
//import java.util.*
//
//import com.github.kittinunf.fuel.core.Method
//import com.github.kittinunf.fuel.util.FuelRouting
//
//import io.paymenthighway.sdk.model.*
//import io.paymenthighway.sdk.util.timestamp

//internal data class TokenizeTransactionBody(val encrypted: String, val key: TokenizeTransactionKey)
//internal data class TokenizeTransactionKey(val key: String, val iv: String)
//
//internal sealed class PaymentHighwayEndpoint: FuelRouting {
//
//    override val basePath = PaymentHighwayProperties.baseURL
//
//    class transactionKey(val merchantId: MerchantId, val accountId: AccountId, val transactionId: TransactionId): PaymentHighwayEndpoint() {}
//    class tokenizeTransaction(val merchantId: MerchantId, val accountId: AccountId, val transactionId: TransactionId, val tokenizeTransactionBody:  TokenizeTransactionBody): PaymentHighwayEndpoint() {}
//
//    override val method: Method
//        get() {
//            return when(this) {
//                is transactionKey -> Method.GET
//                is tokenizeTransaction -> Method.POST
//            }
//        }
//
//    override val path: String
//        get() {
//            return when(this) {
//                is transactionKey -> "/mobile/${this.transactionId.id}/key"
//                is tokenizeTransaction -> "/mobile/${this.transactionId.id}/tokenize"
//            }
//        }
//
//
//    override val params: List<Pair<String, Any?>>? = null
//
//    override val headers: Map<String, String>?
//        get() {
//            return when(this) {
//                is transactionKey -> this.buildHeader(this.merchantId, this.accountId)
//                is tokenizeTransaction -> this.buildHeader(this.merchantId, this.accountId)
//            }
//        }
//
//    override val body: String?
//        get() {
//            return when(this) {
//                is transactionKey -> null
//                is tokenizeTransaction -> Gson().toJson(this.tokenizeTransactionBody)
//            }
//        }
//
//    private fun buildHeader(merchantId: MerchantId, accountId: AccountId): Map<String, String> {
//        val headers = mutableMapOf<String, String>()
//        headers["SPH-Merchant"] = merchantId.id
//        headers["SPH-Account"] = accountId.id
//        headers["SPH-Request-ID"] = UUID.randomUUID().toString().toLowerCase()
//        headers["SPH-Timestamp"] = Date().timestamp()
//        return headers
//    }
//
//}

interface PaymentHighwayServiceNew {

    @GET("/mobile/{transactionId}/key")
    fun transactionKey(@Path("transactionId") transactionId: TransactionId): Observable<TransactionKey>;

    @POST("/mobile/{transactionId}/tokenize")
    fun tokenizeTransaction(@Path("transactionId") transactionId: TransactionId): Observable<TransactionKey>;

    companion object Factory {
        fun create(): PaymentHighwayServiceNew {
            val retrofit = Retrofit.Builder()
                    .baseUrl(PaymentHighwayProperties.baseURL)
                    .client(makeHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(PaymentHighwayServiceNew::class.java);
        }
    }
}

private fun makeHttpClient() = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor())
        .addInterceptor(loggingInterceptor())
        .build()

fun headersInterceptor() = Interceptor { chain ->
    chain.proceed(chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Accept-Language", "en")
            .addHeader("Content-Type", "application/json")
            .build())
}

fun loggingInterceptor() = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}
