package com.cookies.kmp.core

class AppleCookiesAppVersionProvider(
    private val versionCodeProvider: () -> Int,
) : CookiesAppVersionProvider {
    override val appVersionCode: Int
        get() = versionCodeProvider()
}

fun appleCookieMatchingHelper(): CookieMatchingHelper = DefaultCookieMatchingHelper

