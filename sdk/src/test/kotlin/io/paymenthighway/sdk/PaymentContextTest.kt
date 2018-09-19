package io.paymenthighway.sdk

import com.google.gson.Gson
import io.paymenthighway.sdk.exception.UnknowErrorException
import io.paymenthighway.sdk.model.*
import io.paymenthighway.sdk.service.EncryptionKeyApiResult
import io.paymenthighway.sdk.util.Result
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.concurrent.schedule
import okhttp3.mockwebserver.MockWebServer

val transactionIdTest = TransactionId("111222333444555666777888999000")
val creditCardNumber = "5422 3333 4444 5555"
val cvcTest = "123"
val expiryDateTest = ExpiryDate("11", "22")
val cardDataTest = CardData(creditCardNumber, cvcTest, expiryDateTest)
val merchantIdTest = MerchantId("1234567890")
val accountIdTest = AccountId("0987654321")
val paymentConfig = PaymentConfig(merchantIdTest, accountIdTest)
val cardKey = "MIIDlTCCAn0CBwDvPs8AcHAwDQYJKoZIhvcNAQELBQAwgZ0xCzAJBgNVBAYTAkZJMRkwFwYDVQQIDBBTb3V0aGVybiBGaW5sYW5kMREwDwYDVQQHDAhIZWxzaW5raTETMBEGA1UECgwKU29saW5vciBPeTEYMBYGA1UECwwPUGF5bWVudCBIaWdod2F5MTEwLwYDVQQDDChTUEggc3RhZ2luZyBtb2JpbGUgQ2VydGlmaWNhdGUgQXV0aG9yaXR5MCAXDTE1MDcwMzEwMDI0NVoYDzIwNjUwNjIwMTAwMjQ1WjB7MQswCQYDVQQGEwJGSTEZMBcGA1UECAwQU291dGhlcm4gRmlubGFuZDERMA8GA1UEBwwISGVsc2lua2kxEzARBgNVBAoMClNvbGlub3IgT3kxGDAWBgNVBAsMD1BheW1lbnQgSGlnaHdheTEPMA0GA1UEAwwGbW9iaWxlMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwTNgR67GUJNODCyJ6W8yRFzIN99DG3o7xMiF+ogzbXL/07d32diFTZF3MsuGQjEPg+aoUCgp7ly4dG0GBQi7HdpNYeY1ATdBiWit8FGl9Iu++kBDGbOxyvj1hhlvyem/lNsh0H06oODavXKCjE6NjRMgKTlu69d+ZRSQBbCxx8KAS8ApVy5cSwym8CkfxDvLyBFU+EIsuYXJ6zCpHZmBPiVM2Ev0YpNsIl/C5I25UKqIokiSpLZC3gd0dwyD7H0gJEg/TVL9dhjzKSkYkyVOM9T/W0w4x/jQrm33+1dyzvUw7TBH+Hbiv3BE1qAnSGVHtlzt2gcApKJWI38JqOev6wIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQAePsPR/zrYcEg1s59htZeghN+2HwkHl0wWHy5YI8EyqnjkFAE+oaYTizNH+Estm0k8DT5X3OIi1iCHg9YEHLZhYgmMkWGCpDKu7PQ5CCvEwz3pQuIfPXUwaiXcMu6HKpHm4xs1ZMOnmEhlk1GQXK5ksegXFhGgXK1BVKuFDNC6ST78GvEcURa0RaZA1Q6EPjpJw84PFP6l8wB2+eZybX9OufjqFSktC5mBbvhB+r9tQ/FNb0LdSmX+zCNS2k4mVSZ9OWT67D/e3XJCCC055mDAB2MEZULOfl3wQG10FU+hGJqN6VPmstGKqCJlSRQKFlBXSmLVYFCUD6gH4jHl2mnL"
val apiResultInfoOK = ApiResultInfo(100, "OK")
internal val encryptionKeyApiResult = EncryptionKeyApiResult(apiResultInfoOK, cardKey)
val token = "TOKEN--AAAA-BBBB-CCCC"

class BackendAdapterMock(val transactionIdResult: Result<TransactionId, Exception> = Result.success(transactionIdTest),
                         val cardAddedResult: Result<String, Exception> = Result.success(token) ): BackendAdapter<String> {

    override fun getTransactionId(completion: (Result<TransactionId, Exception>) -> Unit) {
        Timer().schedule(100){
            completion(transactionIdResult)
        }
    }

    override fun addCardCompleted(transactionId: TransactionId, completion: (Result<String, Exception>) -> Unit) {
        Timer().schedule(100){
            completion(cardAddedResult)
        }
    }

}

internal class PaymentContextTest : BaseTest() {

    lateinit var paymentContext: PaymentContext<String>

    lateinit var server: MockWebServer

    private fun encryptionKeyResponse(statusCode: Int = 0): MockResponse {
        val mockResponse = MockResponse()
                            .addHeader("Content-Type", "application/json; charset=utf-8")
                            .addHeader("Cache-Control", "no-cache")
        if (statusCode > 0) {
            mockResponse.setResponseCode(statusCode)
        } else {
            mockResponse.setBody(Gson().toJson(encryptionKeyApiResult));
        }
        return mockResponse
    }

    private fun tokenizeTransactionResponse(statusCode: Int = 0): MockResponse {
        val mockResponse = MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
        if (statusCode > 0) {
            mockResponse.setResponseCode(statusCode)
        } else {
            mockResponse.setBody(Gson().toJson(ApiResult(apiResultInfoOK)));
        }
        return mockResponse
    }

    @Before
    fun setUp() {
        server = MockWebServer()
        val httpUrl = server.url("/test/")
        PaymentHighwayProperties.baseURL = httpUrl.url().toString()
    }

    @After
    fun clean() {
        PaymentHighwayProperties.baseURL = BuildConfig.BASE_URL
        server.shutdown()
    }

    @Test
    fun testGetTransactionIdFail() {

        val expectedException = UnknowErrorException()
        val paymentContext = PaymentContext(paymentConfig,
                                            BackendAdapterMock(Result.failure(expectedException)))

        var receivedException: Exception? = null

        paymentContext.addCard(cardDataTest) {result ->
            if (result.isError) {
                receivedException = result.getErrorOrNull()
                lock.countDown()
            }
        }

        await()
        Assert.assertEquals(expectedException, receivedException)
    }

    @Test
    fun testBackendAdapterGetEncryptionKeyFail() {

        val paymentContext = PaymentContext(paymentConfig, BackendAdapterMock())
        server.enqueue(encryptionKeyResponse(500))
        var receivedException: Exception? = null

        paymentContext.addCard(cardDataTest) {result ->
            if (result.isError) {
                receivedException = result.getErrorOrNull()
                lock.countDown()
            }
        }

        await()
        Assert.assertThat(receivedException?.message, CoreMatchers.containsString("500"))
    }

    @Test
    fun testGetTokenazeTransacionFail() {

        val paymentContext = PaymentContext(paymentConfig, BackendAdapterMock())
        server.enqueue(encryptionKeyResponse())
        server.enqueue(tokenizeTransactionResponse(400))
        var receivedException: Exception? = null

        paymentContext.addCard(cardDataTest) {result ->
            if (result.isError) {
                receivedException = result.getErrorOrNull()
                lock.countDown()
            }
        }

        await()
        Assert.assertThat(receivedException?.message, CoreMatchers.containsString("400"))
    }

    @Test
    fun testBackendAdapterAddCardFail() {

        val expectedException = UnknowErrorException()
        val paymentContext = PaymentContext(paymentConfig,
                                            BackendAdapterMock(cardAddedResult = Result.failure(expectedException)))
        server.enqueue(encryptionKeyResponse())
        server.enqueue(tokenizeTransactionResponse())
        var receivedException: Exception? = null

        paymentContext.addCard(cardDataTest) {result ->
            if (result.isError) {
                receivedException = result.getErrorOrNull()
                lock.countDown()
            }
        }

        await()
        Assert.assertEquals(expectedException, receivedException)
    }

    @Test
    fun testAddCardOK() {

        val paymentContext = PaymentContext(paymentConfig, BackendAdapterMock())
        server.enqueue(encryptionKeyResponse())
        server.enqueue(tokenizeTransactionResponse())
        var tokenReceived: String? = null

        paymentContext.addCard(cardDataTest) {result ->
            if (result.isSuccess) {
                tokenReceived = result.getValueOrNull()
                lock.countDown()
            }
        }

        await()
        Assert.assertEquals(tokenReceived, token)
    }

}