package com.cookies.kmp.core

interface CookiesSettingsRepository {
    fun updateAll(
        exceptions: List<FeatureException>,
        firstPartyCookiePolicy: FirstPartyCookiePolicy,
        thirdPartyCookieNames: List<String>,
    )

    val thirdPartyCookieNames: List<String>
}

interface CookiesToggleRepository {
    fun get(featureName: CookiesFeatureName, defaultValue: Boolean): Boolean

    fun getMinSupportedVersion(featureName: CookiesFeatureName): Int

    fun insert(toggle: CookiesFeatureToggle)
}

interface CookiesContentScopeRepository {
    fun updateRawJson(json: String)

    fun rawJson(): String
}

interface AppVersionProvider {
    val versionCode: Int
}
