package com.cookies.kmp.core

/**
 * Adapter boundary for host apps to supply the raw config payload for a cookies feature.
 */
interface PrivacyConfigInputProvider {
    fun rawConfig(featureName: CookiesFeatureName): String?
}

/**
 * Adapter boundary for host apps to supply toggle states and rollout constraints.
 */
interface FeatureToggleInputProvider {
    fun enabled(featureName: CookiesFeatureName, defaultValue: Boolean): Boolean
    fun minSupportedVersion(featureName: CookiesFeatureName): Int
}

/**
 * Adapter boundary for app version values consumed by deterministic rules.
 */
interface CookiesAppVersionProvider {
    val appVersionCode: Int
}

/**
 * Adapter boundary for cookie matching helpers that may be implemented natively.
 */
interface CookieMatchingHelper {
    fun hasExcludedCookieName(cookieString: String, excludedCookieNames: List<String>): Boolean
    fun cookieDomainMatchesTestDomain(cookieDomain: String, testDomain: String): Boolean
}

data class ConfigFormattingInput(
    val featureName: CookiesFeatureName,
    val configJson: String,
)

data class ToggleEvaluationInput(
    val featureName: CookiesFeatureName,
    val defaultValue: Boolean,
    val appVersionCode: Int,
    val enabled: Boolean,
    val minSupportedVersion: Int,
)

data class ToggleEvaluationOutput(
    val isEnabled: Boolean,
)

data class CookieMatchInput(
    val cookieString: String,
    val cookieDomain: String,
    val excludedCookieNames: List<String>,
    val allowedDomains: List<String>,
)

data class CookieMatchOutput(
    val hasExcludedCookieName: Boolean,
    val matchesAllowedDomain: Boolean,
)

