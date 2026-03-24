package com.cookies.kmp.core

class MacosCookiesAppVersionProvider(
    private val versionCodeProvider: () -> Int,
) : CookiesAppVersionProvider {
    override val appVersionCode: Int
        get() = versionCodeProvider()
}

fun macosCookieMatchingHelper(): CookieMatchingHelper = DefaultCookieMatchingHelper

