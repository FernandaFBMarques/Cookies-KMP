package com.cookies.kmp.core

class CookiesFeatureToggleEvaluator(
    private val toggleRepository: CookiesToggleRepository,
    private val appVersionProvider: AppVersionProvider,
) {
    fun isEnabled(featureName: String, defaultValue: Boolean): Boolean? {
        val cookiesFeatureName = CookiesFeatureName.fromValue(featureName) ?: return null

        return toggleRepository.get(cookiesFeatureName, defaultValue) &&
                appVersionProvider.versionCode >= toggleRepository.getMinSupportedVersion(cookiesFeatureName)
    }
}
