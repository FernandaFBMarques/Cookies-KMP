package com.cookies.kmp.core

class CookiesPolicyEngine(
    private val cookieMatchingHelper: CookieMatchingHelper = DefaultCookieMatchingHelper,
) {
    fun formatConfig(input: ConfigFormattingInput): String {
        return CookiesContentScopeConfigFormatter.format(input.featureName, input.configJson)
    }

    fun evaluateToggle(input: ToggleEvaluationInput): ToggleEvaluationOutput {
        return ToggleEvaluationOutput(
            isEnabled = input.enabled && input.appVersionCode >= input.minSupportedVersion,
        )
    }

    fun evaluateCookie(input: CookieMatchInput): CookieMatchOutput {
        val hasExcludedName = cookieMatchingHelper.hasExcludedCookieName(
            cookieString = input.cookieString,
            excludedCookieNames = input.excludedCookieNames,
        )

        val matchesAllowedDomain = input.allowedDomains.any { testDomain ->
            cookieMatchingHelper.cookieDomainMatchesTestDomain(
                cookieDomain = input.cookieDomain,
                testDomain = testDomain,
            )
        }

        return CookieMatchOutput(
            hasExcludedCookieName = hasExcludedName,
            matchesAllowedDomain = matchesAllowedDomain,
        )
    }
}

object DefaultCookieMatchingHelper : CookieMatchingHelper {
    private val nameMatcher = ThirdPartyCookieNameMatcher()

    override fun hasExcludedCookieName(
        cookieString: String,
        excludedCookieNames: List<String>,
    ): Boolean {
        return nameMatcher.hasExcludedCookieName(cookieString, excludedCookieNames)
    }

    override fun cookieDomainMatchesTestDomain(cookieDomain: String, testDomain: String): Boolean {
        return testDomain == cookieDomain ||
            ".${testDomain}" == cookieDomain ||
            (cookieDomain.startsWith(".") && testDomain.endsWith(cookieDomain))
    }
}

