package com.cookies.kmp.core

class IosCookiesAppVersionProvider(
    private val versionCodeProvider: () -> Int,
) : CookiesAppVersionProvider {
    override val appVersionCode: Int
        get() = versionCodeProvider()
}

fun iosCookieMatchingHelper(): CookieMatchingHelper = DefaultCookieMatchingHelper

