package com.cookies.kmp.core

import kotlin.test.Test
import kotlin.test.assertEquals

class CookiesRepositoryAdaptersTest {

    @Test
    fun repositoryPrivacyConfigProvider_returnsRawConfigForCookieFeature() {
        val provider = RepositoryPrivacyConfigInputProvider(
            contentScopeRepository = object : CookiesContentScopeRepository {
                override fun updateRawJson(json: String) = Unit
                override fun rawJson(): String = """{"state":"enabled"}"""
            },
        )

        assertEquals("""{"state":"enabled"}""", provider.rawConfig(CookiesFeatureName.Cookie))
    }

    @Test
    fun repositoryFeatureToggleProvider_delegatesToRepository() {
        val provider = RepositoryFeatureToggleInputProvider(
            toggleRepository = object : CookiesToggleRepository {
                override fun get(featureName: CookiesFeatureName, defaultValue: Boolean): Boolean = true
                override fun getMinSupportedVersion(featureName: CookiesFeatureName): Int = 321
                override fun insert(toggle: CookiesFeatureToggle) = Unit
            },
        )

        assertEquals(true, provider.enabled(CookiesFeatureName.Cookie, defaultValue = false))
        assertEquals(321, provider.minSupportedVersion(CookiesFeatureName.Cookie))
    }
}

