package com.cookies.kmp.core

class RepositoryPrivacyConfigInputProvider(
    private val contentScopeRepository: CookiesContentScopeRepository,
) : PrivacyConfigInputProvider {
    override fun rawConfig(featureName: CookiesFeatureName): String? {
        return if (featureName == CookiesFeatureName.Cookie) {
            contentScopeRepository.rawJson()
        } else {
            null
        }
    }
}

class RepositoryFeatureToggleInputProvider(
    private val toggleRepository: CookiesToggleRepository,
) : FeatureToggleInputProvider {
    override fun enabled(featureName: CookiesFeatureName, defaultValue: Boolean): Boolean {
        return toggleRepository.get(featureName, defaultValue)
    }

    override fun minSupportedVersion(featureName: CookiesFeatureName): Int {
        return toggleRepository.getMinSupportedVersion(featureName)
    }
}

class DelegatingCookiesAppVersionProvider(
    private val appVersionProvider: AppVersionProvider,
) : CookiesAppVersionProvider {
    override val appVersionCode: Int
        get() = appVersionProvider.versionCode
}

