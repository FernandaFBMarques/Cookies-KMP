package com.cookies.kmp.core

class ThirdPartyCookieNameMatcher {
    fun hasExcludedCookieName(cookieString: String, excludedCookieNames: List<String>): Boolean {
        return excludedCookieNames.any { cookieString.contains(it) }
    }
}