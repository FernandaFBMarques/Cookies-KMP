package com.cookies.kmp.core

object CookiesContentScopeConfigFormatter {
    fun format(featureName: CookiesFeatureName, configJson: String): String {
        return "\"${featureName.value}\":$configJson"
    }
}