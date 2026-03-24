package com.cookies.kmp.core

import kotlin.test.Test
import kotlin.test.assertEquals

class CrossPlatformParityFixturesTest {
    private val engine = CookiesPolicyEngine()
    private val facade = CookiesHostParityFacade

    @Test
    fun formatterParity_fixturesMatchLegacyFormatterAndExpectedOutput() {
        facade.configFormattingFixtures().forEach { fixture ->
            val legacy = CookiesContentScopeConfigFormatter.format(
                fixture.input.featureName,
                fixture.input.configJson,
            )
            val actual = facade.formatConfig(fixture.input)

            assertEquals(fixture.expectedOutput, actual, fixture.name)
            assertEquals(legacy, actual, fixture.name)
        }
    }

    @Test
    fun toggleParity_fixturesMatchLegacyEvaluatorAndExpectedOutput() {
        facade.toggleEvaluationFixtures().forEach { fixture ->
            val toggleRepo = object : CookiesToggleRepository {
                override fun get(featureName: CookiesFeatureName, defaultValue: Boolean): Boolean = fixture.input.enabled
                override fun getMinSupportedVersion(featureName: CookiesFeatureName): Int = fixture.input.minSupportedVersion
                override fun insert(toggle: CookiesFeatureToggle) = Unit
            }
            val appVersionProvider = object : AppVersionProvider {
                override val versionCode: Int = fixture.input.appVersionCode
            }

            val legacy = CookiesFeatureToggleEvaluator(toggleRepo, appVersionProvider)
                .isEnabled(fixture.input.featureName.value, fixture.input.defaultValue)

            val actual = facade.evaluateToggle(fixture.input)

            assertEquals(fixture.expectedOutput.isEnabled, actual.isEnabled, fixture.name)
            assertEquals(legacy, actual.isEnabled, fixture.name)
        }
    }

    @Test
    fun cookieParity_fixturesMatchLegacyMatchersAndExpectedOutput() {
        facade.cookieMatchFixtures().forEach { fixture ->
            val actual = facade.evaluateCookie(fixture.input)
            val legacyNameMatch = ThirdPartyCookieNameMatcher().hasExcludedCookieName(
                fixture.input.cookieString,
                fixture.input.excludedCookieNames,
            )
            val legacyDomainMatch = fixture.input.allowedDomains.any { allowedDomain ->
                DefaultCookieMatchingHelper.cookieDomainMatchesTestDomain(
                    cookieDomain = fixture.input.cookieDomain,
                    testDomain = allowedDomain,
                )
            }

            assertEquals(fixture.expectedOutput, actual, fixture.name)
            assertEquals(legacyNameMatch, actual.hasExcludedCookieName, fixture.name)
            assertEquals(legacyDomainMatch, actual.matchesAllowedDomain, fixture.name)
        }
    }

    @Test
    fun hostParityFacade_matchesDirectEngineEvaluation() {
        facade.configFormattingFixtures().forEach { fixture ->
            assertEquals(engine.formatConfig(fixture.input), facade.formatConfig(fixture.input), fixture.name)
        }

        facade.toggleEvaluationFixtures().forEach { fixture ->
            assertEquals(engine.evaluateToggle(fixture.input), facade.evaluateToggle(fixture.input), fixture.name)
        }

        facade.cookieMatchFixtures().forEach { fixture ->
            assertEquals(engine.evaluateCookie(fixture.input), facade.evaluateCookie(fixture.input), fixture.name)
        }
    }
}
