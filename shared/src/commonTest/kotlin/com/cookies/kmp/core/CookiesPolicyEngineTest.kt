package com.cookies.kmp.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CookiesPolicyEngineTest {
    private val engine = CookiesPolicyEngine()

    @Test
    fun formatConfig_wrapsFeatureNameAndPayload() {
        val output = engine.formatConfig(
            ConfigFormattingInput(
                featureName = CookiesFeatureName.Cookie,
                configJson = """{"state":"enabled"}""",
            ),
        )

        assertEquals(""""cookie":{"state":"enabled"}""", output)
    }

    @Test
    fun evaluateToggle_returnsEnabledWhenRolloutAndVersionAreValid() {
        val output = engine.evaluateToggle(
            ToggleEvaluationInput(
                featureName = CookiesFeatureName.Cookie,
                defaultValue = false,
                appVersionCode = 1200,
                enabled = true,
                minSupportedVersion = 1100,
            ),
        )

        assertTrue(output.isEnabled)
    }

    @Test
    fun evaluateToggle_returnsDisabledWhenVersionIsBelowMinimum() {
        val output = engine.evaluateToggle(
            ToggleEvaluationInput(
                featureName = CookiesFeatureName.Cookie,
                defaultValue = true,
                appVersionCode = 500,
                enabled = true,
                minSupportedVersion = 1100,
            ),
        )

        assertFalse(output.isEnabled)
    }

    @Test
    fun evaluateCookie_matchesExcludedNameAndAllowedDomain() {
        val output = engine.evaluateCookie(
            CookieMatchInput(
                cookieString = "id_token=abc",
                cookieDomain = ".example.com",
                excludedCookieNames = listOf("id_token"),
                allowedDomains = listOf("example.com"),
            ),
        )

        assertTrue(output.hasExcludedCookieName)
        assertTrue(output.matchesAllowedDomain)
    }

    @Test
    fun evaluateCookie_returnsNoAllowedDomainMatchForDifferentDomain() {
        val output = engine.evaluateCookie(
            CookieMatchInput(
                cookieString = "session=abc",
                cookieDomain = ".another.com",
                excludedCookieNames = listOf("id_token"),
                allowedDomains = listOf("example.com"),
            ),
        )

        assertFalse(output.hasExcludedCookieName)
        assertFalse(output.matchesAllowedDomain)
    }
}

