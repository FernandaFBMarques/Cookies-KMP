package com.cookies.kmp.core

import kotlinx.serialization.Serializable

@Serializable
internal data class CookiesFeaturePayload(
    val state: String? = null,
    val minSupportedVersion: Int? = null,
    val exceptions: List<FeatureExceptionPayload> = emptyList(),
    val settings: SettingsPayload = SettingsPayload(),
)

@Serializable
internal data class FeatureExceptionPayload(
    val domain: String,
    val reason: String? = null,
)

@Serializable
internal data class SettingsPayload(
    val firstPartyCookiePolicy: FirstPartyCookiePolicyPayload? = null,
    val thirdPartyCookieNames: List<String> = emptyList(),
)

@Serializable
internal data class FirstPartyCookiePolicyPayload(
    val threshold: Int? = null,
    val maxAge: Int? = null,
)