package io.paymenthighway.sdk

import io.paymenthighway.demo.backendadapterexample.BackendAdapterExample
import io.paymenthighway.sdk.model.*
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog


@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE, sdk=[23])
internal class PaymentHighwayServiceTest  {

    val merchantId = MerchantId("test_merchantId")
    val accountId = AccountId("test")
    val cardTest = CardData("4153013999700024", "024", ExpirationDate("11", "2023"))

    lateinit var lock: CountDownLatch
    lateinit var backendAdapter: BackendAdapterExample
    lateinit var service: PaymentHighwayService

    @Before
    fun setUp() {
        ShadowLog.setupLogging()
        ShadowLog.stream = System.out;
        lock = CountDownLatch(1)
        backendAdapter = BackendAdapterExample()
        service = PaymentHighwayService(merchantId, accountId)
    }

    fun await(seconds: Long = 5) {
        lock.await(seconds, TimeUnit.SECONDS)
    }

    @Test
    fun testReceiveTransactionId() {
        var id: String  = ""
        backendAdapter.getTransactionId { result ->
            id = result.getOrThrow().id
            lock.countDown()
        }
        await()
        Assert.assertThat(id, CoreMatchers.containsString("-"))
    }

    @Test
    fun testReceiveTransactionKey() {
        var key: String = ""

        backendAdapter.getTransactionId { result ->
            val transactionId = result.getOrThrow()
            service.transactionKey(transactionId) { result ->
                key = result.getOrThrow().key
                lock.countDown()
            }
        }
        await()
        Assert.assertThat(key, CoreMatchers.containsString("MII"))
    }

    @Test
    fun testTokenize() {

        var resultCode: Int = 0
        backendAdapter.getTransactionId { result ->
            val transactionId = result.getOrThrow()
            service.transactionKey(transactionId) { result ->
                val transactionKey = result.getOrThrow()
                service.tokenizeTransaction(transactionId, cardTest, transactionKey) { result ->
                    resultCode = result.getOrThrow().result.code
                    lock.countDown()
                }
            }
        }
        await()
        Assert.assertEquals(resultCode, ApiResult.OK)
    }

    @Test
    fun testGetToken() {

        var token: String = ""
        backendAdapter.getTransactionId { result ->
            val transactionId = result.getOrThrow()
            service.transactionKey(transactionId) { result ->
                val transactionKey = result.getOrThrow()
                service.tokenizeTransaction(transactionId, cardTest, transactionKey) { result ->
                    if (result.getOrThrow().result.code == ApiResult.OK) {
                        backendAdapter.cardAdded(transactionId) { result ->
                            token = result.getOrThrow().token
                            lock.countDown()
                        }
                    }
                }
            }
        }
        await()
        Assert.assertThat(token, CoreMatchers.containsString("-"))
    }

}