package io.paymenthighway.sdk

import io.paymenthighway.demo.backendadapterexample.BackendAdapterExample
import io.paymenthighway.demo.backendadapterexample.TransactionCard
import io.paymenthighway.sdk.model.*
import io.paymenthighway.sdk.service.PaymentHighwayService
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class PaymentHighwayServiceTest: BaseTest()  {

    val merchantId = MerchantId("test_merchantId")
    val accountId = AccountId("test")
    val paymentConfig = PaymentConfig(merchantId, accountId, Environment.Sandbox)
    val cardTest = CardData("4153013999700024", "024", ExpiryDate("11", "2023"))

    lateinit var backendAdapter: BackendAdapterExample
    lateinit var service: PaymentHighwayService

    @Before
    fun setUp() {
        backendAdapter = BackendAdapterExample()
        service = PaymentHighwayService(paymentConfig)
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

        var success: Boolean = false
        backendAdapter.getTransactionId { result ->
            val transactionId = result.getOrThrow()
            service.encryptionKey(transactionId) { result ->
                val encryptionKey = result.getOrThrow()
                service.tokenizeTransaction(transactionId, cardTest, encryptionKey) { result ->
                    success = result.isSuccess
                    lock.countDown()
                }
            }
        }
        await()
        Assert.assertEquals(true,success)
    }

    @Test
    fun testGetToken() {

        var token: String = ""
        var card = TransactionCard("","","", "")
        backendAdapter.getTransactionId { result ->
            val transactionId = result.getOrThrow()
            service.encryptionKey(transactionId) { result ->
                val encryptionKey = result.getOrThrow()
                service.tokenizeTransaction(transactionId, cardTest, encryptionKey) { result ->
                    if (result.isSuccess) {
                        backendAdapter.addCardCompleted(transactionId) { result ->
                            card = result.getOrThrow().card
                            token = result.getOrThrow().token
                            lock.countDown()
                        }
                    }
                }
            }
        }
        await()
        Assert.assertThat(token, CoreMatchers.containsString("-"))
        Assert.assertEquals("Visa", card.cardType)
        Assert.assertEquals("0024", card.partialPan)
        Assert.assertEquals("11", card.expireMonth)
        Assert.assertEquals("2023", card.expireYear)
    }

}