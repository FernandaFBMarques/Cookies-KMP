package com.cookies.kmp.core

class AndroidCookiesAppVersionProvider(
    private val versionCodeProvider: () -> Int,
) : CookiesAppVersionProvider {
    override val appVersionCode: Int
        get() = versionCodeProvider()
}

fun androidCookieMatchingHelper(): CookieMatchingHelper = DefaultCookieMatchingHelper

