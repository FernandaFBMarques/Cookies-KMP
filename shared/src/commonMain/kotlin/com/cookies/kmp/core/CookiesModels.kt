package com.cookies.kmp.core

enum class CookiesFeatureName(val value: String) {
    Cookie("cookie"),
    ;

    companion object {
        fun fromValue(value: String): CookiesFeatureName? {
            return entries.firstOrNull { it.value == value }
        }
    }
}

data class FeatureException(
    val domain: String,
    val reason: String,
)

data class FirstPartyCookiePolicy(
    val threshold: Int,
    val maxAge: Int,
)

data class CookiesFeatureToggle(
    val featureName: CookiesFeatureName,
    val enabled: Boolean,
    val minSupportedVersion: Int?,
)

object CookiesDefaults {
    const val DEFAULT_THRESHOLD = 86400
    const val DEFAULT_MAX_AGE = 86400
}
