package com.cookies.kmp.core

data class ConfigFormattingParityFixture(
    val name: String,
    val input: ConfigFormattingInput,
    val expectedOutput: String,
)

data class ToggleEvaluationParityFixture(
    val name: String,
    val input: ToggleEvaluationInput,
    val expectedOutput: ToggleEvaluationOutput,
)

data class CookieMatchParityFixture(
    val name: String,
    val input: CookieMatchInput,
    val expectedOutput: CookieMatchOutput,
)

object CookiesParityFixtures {
    val configFormattingFixtures: List<ConfigFormattingParityFixture> = listOf(
        ConfigFormattingParityFixture(
            name = "cookie formatter wraps enabled payload",
            input = ConfigFormattingInput(
                featureName = CookiesFeatureName.Cookie,
                configJson = """{"state":"enabled","minSupportedVersion":1234}""",
            ),
            expectedOutput = """"cookie":{"state":"enabled","minSupportedVersion":1234}""",
        ),
        ConfigFormattingParityFixture(
            name = "cookie formatter preserves nested settings payload",
            input = ConfigFormattingInput(
                featureName = CookiesFeatureName.Cookie,
                configJson = """{"settings":{"thirdPartyCookieNames":["id_token","session"]}}""",
            ),
            expectedOutput = """"cookie":{"settings":{"thirdPartyCookieNames":["id_token","session"]}}""",
        ),
    )

    val toggleEvaluationFixtures: List<ToggleEvaluationParityFixture> = listOf(
        ToggleEvaluationParityFixture(
            name = "toggle enabled when rollout is on and app version is above minimum",
            input = ToggleEvaluationInput(
                featureName = CookiesFeatureName.Cookie,
                defaultValue = false,
                appVersionCode = 1200,
                enabled = true,
                minSupportedVersion = 1100,
            ),
            expectedOutput = ToggleEvaluationOutput(isEnabled = true),
        ),
        ToggleEvaluationParityFixture(
            name = "toggle disabled when rollout is off",
            input = ToggleEvaluationInput(
                featureName = CookiesFeatureName.Cookie,
                defaultValue = true,
                appVersionCode = 1200,
                enabled = false,
                minSupportedVersion = 1100,
            ),
            expectedOutput = ToggleEvaluationOutput(isEnabled = false),
        ),
        ToggleEvaluationParityFixture(
            name = "toggle enabled when app version matches minimum",
            input = ToggleEvaluationInput(
                featureName = CookiesFeatureName.Cookie,
                defaultValue = false,
                appVersionCode = 1100,
                enabled = true,
                minSupportedVersion = 1100,
            ),
            expectedOutput = ToggleEvaluationOutput(isEnabled = true),
        ),
        ToggleEvaluationParityFixture(
            name = "toggle disabled when app version is below minimum",
            input = ToggleEvaluationInput(
                featureName = CookiesFeatureName.Cookie,
                defaultValue = true,
                appVersionCode = 500,
                enabled = true,
                minSupportedVersion = 1100,
            ),
            expectedOutput = ToggleEvaluationOutput(isEnabled = false),
        ),
    )

    val cookieMatchFixtures: List<CookieMatchParityFixture> = listOf(
        CookieMatchParityFixture(
            name = "cookie matches excluded name and exact allowed domain",
            input = CookieMatchInput(
                cookieString = "id_token=123; session=abc",
                cookieDomain = "example.com",
                excludedCookieNames = listOf("id_token"),
                allowedDomains = listOf("example.com"),
            ),
            expectedOutput = CookieMatchOutput(
                hasExcludedCookieName = true,
                matchesAllowedDomain = true,
            ),
        ),
        CookieMatchParityFixture(
            name = "cookie matches excluded name and dot prefixed allowed domain",
            input = CookieMatchInput(
                cookieString = "session=abc; auth_token=123",
                cookieDomain = ".example.com",
                excludedCookieNames = listOf("auth_token"),
                allowedDomains = listOf("example.com"),
            ),
            expectedOutput = CookieMatchOutput(
                hasExcludedCookieName = true,
                matchesAllowedDomain = true,
            ),
        ),
        CookieMatchParityFixture(
            name = "cookie matches allowed subdomain without excluded cookie name",
            input = CookieMatchInput(
                cookieString = "session=abc",
                cookieDomain = ".sub.example.com",
                excludedCookieNames = listOf("id_token"),
                allowedDomains = listOf("sub.example.com"),
            ),
            expectedOutput = CookieMatchOutput(
                hasExcludedCookieName = false,
                matchesAllowedDomain = true,
            ),
        ),
        CookieMatchParityFixture(
            name = "cookie does not match excluded name or unrelated domain",
            input = CookieMatchInput(
                cookieString = "session=abc",
                cookieDomain = ".another.com",
                excludedCookieNames = listOf("id_token"),
                allowedDomains = listOf("example.com"),
            ),
            expectedOutput = CookieMatchOutput(
                hasExcludedCookieName = false,
                matchesAllowedDomain = false,
            ),
        ),
    )
}
