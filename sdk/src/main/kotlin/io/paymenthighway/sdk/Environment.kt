package io.paymenthighway.sdk

interface ServerType {
    var baseURL: String
}

enum class Environment: ServerType {
    Sandbox {
        override var baseURL: String = "https://v1-hub-staging.sph-test-solinor.com/"
    },
    Production {
        override var baseURL: String = "https://v1.api.paymenthighway.io/"
    };

    companion object {
        @JvmField var current: Environment = if (BuildConfig.DEBUG) Environment.Sandbox else Environment.Production
    }
}