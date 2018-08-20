package io.paymenthighway.demo.backendadapterexample

import io.paymenthighway.sdk.util.Result
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertThat
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class BackendAdapterEndpointTest {

    lateinit var lock: CountDownLatch

    @Before
    fun setUp() {
        lock = CountDownLatch(1)
    }

    fun await(seconds: Long = 5) {
        lock.await(seconds, TimeUnit.SECONDS)
    }

    @Test
    fun testAdapter() {
        val service = BackendAdapterExample()

        service.getTransactionId { result ->
            when (result) {
                is Result.Success -> {
                    assertThat(result.value.id, containsString("-"))
                    print(" received transaction id ${result.value}")
                    lock.countDown()
                }
                is Result.Failure -> {
                    assert(false, {"unexpected error"})
                }
            }
        }
        await()
    }
}