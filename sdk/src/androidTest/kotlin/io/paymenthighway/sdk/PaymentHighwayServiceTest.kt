package io.paymenthighway.sdk

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class PaymentHighwayServiceInstrumentedTest {

    @Test
    fun myFirstTest() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("io.paymenthighway.sdk.test", appContext.getPackageName())
    }
}