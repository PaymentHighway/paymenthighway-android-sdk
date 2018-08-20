package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.*
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class PaymentHighwayServiceTest  {

    val merchantId = MerchantId("test_merchantId")
    val accountId = AccountId("test")
    lateinit var lock: CountDownLatch

    @Before
    fun setUp() {
        lock = CountDownLatch(1)
    }

    fun await(seconds: Long = 5) {
        lock.await(seconds, TimeUnit.SECONDS)
    }

    @Test
    fun testEncodingNormal() {


//        var error: FuelError? = null
//        var transactionKey: TransactionKey? = null
//
//        val service = PaymentHighwayService(merchantId, accountId)
//        val transactionId = TransactionId("099887766554321")
//        service.transactionKey(transactionId) {
//            println("---> result ---> $it")
//            error = it.component2()
//            transactionKey = it.component1()
//            lock.countDown()
//        }
//        println("---> before await")
//        await()
//        println("---> before assertThat error")
//        assertThat(error, notNullValue())
//        assertThat(transactionKey, notNullValue())
//        assertThat(error, notNullValue())
//        println("---> end test")
    }

    @Test
    fun testRetrofitRx() {
        val apiService = PaymentHighwayServiceNew.create()
        val transactionId = TransactionId("099887766554321")
        val disposable = apiService.transactionKey(transactionId)
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    result ->
                    println("Result---> $result")
                    lock.countDown()
                }, { error ->
                    println("error---> $error")
                    lock.countDown()
                })
        println("---> before await")
        await()
        disposable.dispose()
        println("---> end")
    }

}