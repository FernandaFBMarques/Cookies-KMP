package com.cookies.kmp.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CookiesFeatureStoreUpdaterTest {

    private val settingsRepository = FakeSettingsRepository()
    private val contentScopeRepository = FakeContentScopeRepository()
    private val toggleRepository = FakeToggleRepository()

    private val updater = CookiesFeatureStoreUpdater(
        settingsRepository = settingsRepository,
        contentScopeRepository = contentScopeRepository,
        toggleRepository = toggleRepository,
    )

    @Test
    fun whenFeatureNameMatchesCookie_thenStoresExpectedValues() {
        val json =
            """
            {
              "state": "enabled",
              "minSupportedVersion": 1234,
              "exceptions": [{ "domain": "example.com", "reason": "reason" }],
              "settings": {
                "firstPartyCookiePolicy": { "threshold": 1, "maxAge": 2 },
                "thirdPartyCookieNames": ["cookie1", "cookie2"]
              }
            }
            """.trimIndent()

        val stored = updater.store("cookie", json)

        assertTrue(stored)
        assertEquals(1, settingsRepository.exceptions.size)
        assertEquals("example.com", settingsRepository.exceptions.first().domain)
        assertEquals(1, settingsRepository.policy?.threshold)
        assertEquals(2, settingsRepository.policy?.maxAge)
        assertEquals(listOf("cookie1", "cookie2"), settingsRepository.cookieNames)
        assertEquals(true, toggleRepository.toggle?.enabled)
        assertEquals(1234, toggleRepository.toggle?.minSupportedVersion)
        assertEquals(json, contentScopeRepository.json)
    }

    @Test
    fun whenFeatureNameDoesNotMatchCookie_thenReturnsFalse() {
        val stored = updater.store("not-cookie", "{}")

        assertEquals(false, stored)
    }

    private class FakeSettingsRepository : CookiesSettingsRepository {
        var exceptions: List<FeatureException> = emptyList()
        var policy: FirstPartyCookiePolicy? = null
        var cookieNames: List<String> = emptyList()

        override fun updateAll(
            exceptions: List<FeatureException>,
            firstPartyCookiePolicy: FirstPartyCookiePolicy,
            thirdPartyCookieNames: List<String>,
        ) {
            this.exceptions = exceptions
            this.policy = firstPartyCookiePolicy
            this.cookieNames = thirdPartyCookieNames
        }

        override val thirdPartyCookieNames: List<String>
            get() = cookieNames
    }

    private class FakeToggleRepository : CookiesToggleRepository {
        var toggle: CookiesFeatureToggle? = null

        override fun get(featureName: CookiesFeatureName, defaultValue: Boolean): Boolean {
            return defaultValue
        }

        override fun getMinSupportedVersion(featureName: CookiesFeatureName): Int {
            return 0
        }

        override fun insert(toggle: CookiesFeatureToggle) {
            this.toggle = toggle
        }
    }

    private class FakeContentScopeRepository : CookiesContentScopeRepository {
        var json: String = "{}"

        override fun updateRawJson(json: String) {
            this.json = json
        }

        override fun rawJson(): String = json
    }
}
