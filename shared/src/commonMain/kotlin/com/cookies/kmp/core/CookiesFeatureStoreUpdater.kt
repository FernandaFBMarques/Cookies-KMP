package com.cookies.kmp.core

import kotlinx.serialization.json.Json
import kotlin.text.insert

class CookiesFeatureStoreUpdater(
    private val settingsRepository: CookiesSettingsRepository,
    private val contentScopeRepository: CookiesContentScopeRepository,
    private val toggleRepository: CookiesToggleRepository,
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    fun store(featureName: String, jsonString: String): Boolean {
        val cookiesFeatureName = CookiesFeatureName.fromValue(featureName) ?: return false

        val payload = runCatching {
            json.decodeFromString<CookiesFeaturePayload>(jsonString)
        }.getOrNull()

        val firstPartyCookiePolicy = payload?.settings?.firstPartyCookiePolicy
        val policy = FirstPartyCookiePolicy(
            threshold = firstPartyCookiePolicy?.threshold ?: CookiesDefaults.DEFAULT_THRESHOLD,
            maxAge = firstPartyCookiePolicy?.maxAge ?: CookiesDefaults.DEFAULT_MAX_AGE,
        )

        settingsRepository.updateAll(
            exceptions = payload?.exceptions?.map {
                FeatureException(domain = it.domain, reason = it.reason.orEmpty())
            }.orEmpty(),
            firstPartyCookiePolicy = policy,
            thirdPartyCookieNames = payload?.settings?.thirdPartyCookieNames.orEmpty(),
        )

        toggleRepository.insert(
            CookiesFeatureToggle(
                featureName = cookiesFeatureName,
                enabled = payload?.state == "enabled",
                minSupportedVersion = payload?.minSupportedVersion,
            ),
        )

        contentScopeRepository.updateRawJson(jsonString)

        return true
    }
}
