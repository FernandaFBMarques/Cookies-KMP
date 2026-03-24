package com.cookies.kmp.core

/**
 * Stable host-facing entry point for parity fixtures and deterministic cookies policy evaluation.
 *
 * Host apps should prefer this facade in tests and thin adapters instead of depending directly
 * on the internal shape of the policy engine.
 */
object CookiesHostParityFacade {
    private val engine = CookiesPolicyEngine()

    fun configFormattingFixtures(): List<ConfigFormattingParityFixture> {
        return CookiesParityFixtures.configFormattingFixtures
    }

    fun toggleEvaluationFixtures(): List<ToggleEvaluationParityFixture> {
        return CookiesParityFixtures.toggleEvaluationFixtures
    }

    fun cookieMatchFixtures(): List<CookieMatchParityFixture> {
        return CookiesParityFixtures.cookieMatchFixtures
    }

    fun formatConfig(input: ConfigFormattingInput): String {
        return engine.formatConfig(input)
    }

    fun evaluateToggle(input: ToggleEvaluationInput): ToggleEvaluationOutput {
        return engine.evaluateToggle(input)
    }

    fun evaluateCookie(input: CookieMatchInput): CookieMatchOutput {
        return engine.evaluateCookie(input)
    }
}
