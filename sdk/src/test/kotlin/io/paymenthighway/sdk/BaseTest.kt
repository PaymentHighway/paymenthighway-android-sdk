package io.paymenthighway.sdk

import org.junit.Ignore
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

const val defaultTestTimeout: Long = 5

@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE, sdk=[23])
@Ignore
internal open class BaseTest {

    var lock: CountDownLatch = CountDownLatch(1)

    init {
        ShadowLog.setupLogging()
        ShadowLog.stream = System.out;
    }

    fun await(seconds: Long = defaultTestTimeout) {
        lock.await(seconds, TimeUnit.SECONDS)
    }
}