package io.paymenthighway.sdk

import io.paymenthighway.demo.backendadapterexample.BackendAdapterExample
import io.paymenthighway.sdk.model.*
import io.paymenthighway.sdk.service.PaymentHighwayService
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class PaymentHighwayServiceTest: BaseTest()  {

    val merchantId = MerchantId("test_merchantId")
    val accountId = AccountId("test")
    val cardTest = CardData("4153013999700024", "024", ExpirationDate("11", "2023"))

    lateinit var backendAdapter: BackendAdapterExample
    lateinit var service: PaymentHighwayService

    @Before
    fun setUp() {
        backendAdapter = BackendAdapterExample()
        service = PaymentHighwayService(merchantId, accountId)
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
    fun testReceiveEncryptionKey() {
        var key: String = ""

        backendAdapter.getTransactionId { result ->
            val transactionId = result.getOrThrow()
            service.encryptionKey(transactionId) { result ->
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
            service.encryptionKey(transactionId) { result ->
                val encryptionKey = result.getOrThrow()
                service.tokenizeTransaction(transactionId, cardTest, encryptionKey) { result ->
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
            service.encryptionKey(transactionId) { result ->
                val encryptionKey = result.getOrThrow()
                service.tokenizeTransaction(transactionId, cardTest, encryptionKey) { result ->
                    if (result.getOrThrow().result.code == ApiResult.OK) {
                        backendAdapter.addCardCompleted(transactionId) { result ->
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