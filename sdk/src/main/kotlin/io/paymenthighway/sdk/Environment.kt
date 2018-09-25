package io.paymenthighway.sdk

/**
 * Interface to provide environment base URL
 */
interface ServerBaseURL {
    var baseURL: String
}

/**
 * Payment Highway environment interface
 */
interface EnvironmentInterface: ServerBaseURL

/**
 * Payment Highway environment
 *
 */
enum class Environment: EnvironmentInterface {
    /**
     * Payment Highway sandbox environment for testing
     */
    Sandbox {
        override var baseURL: String = "https://v1-hub-staging.sph-test-solinor.com/"
    },

    /**
     * Payment Highway production environment
     */
    Production {
        override var baseURL: String = "https://v1.api.paymenthighway.io/"
    };
}