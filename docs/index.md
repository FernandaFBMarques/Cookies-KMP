---
layout: home
title: Cookies KMP Integration Reference
nav_order: 1
---

# Cookies KMP Integration Reference

This page documents the proof-of-concept migration of deterministic cookies logic into a Kotlin Multiplatform library and its integration into the Android and Apple DuckDuckGo forks.

The goal is to provide a reference that can be cited alongside the project report: what was built, why the architecture is split this way, which commits belong to each repository, and how the implementation was validated.

## Repositories

| Repository | Role | Main branch used in this work |
| --- | --- | --- |
| [FernandaFBMarques/Cookies-KMP](https://github.com/FernandaFBMarques/Cookies-KMP) | Shared Kotlin Multiplatform library | `main` |
| [FernandaFBMarques/Android](https://github.com/FernandaFBMarques/Android) | Android host application fork | `cookies-kmp-integration` |
| [FernandaFBMarques/apple-browsers](https://github.com/FernandaFBMarques/apple-browsers) | Apple host application fork | `cookies-kmp-integrate` |

## Project Overview

The project extracts deterministic cookies business rules from platform-specific application code into a shared Kotlin Multiplatform module.

The shared library is published as:

```text
io.github.fernandafbmarques:cookies-kmp-core:0.1.4
```

The same library is also distributed to Apple consumers through a Swift Package binary target:

```swift
.binaryTarget(
    name: "CookiesKMP",
    url: "https://github.com/FernandaFBMarques/Cookies-KMP/releases/download/0.1.4/CookiesKMP.xcframework.zip",
    checksum: "0f22bb204cc7e0b26d01ce91d4be668db2df50aab327aed8072985032ecd0b0e"
)
```

## Architecture

The integration follows an adapter-based boundary.

KMP owns deterministic logic:

- feature-toggle decisions for cookies features
- cookie-name matching
- cookie-domain matching
- content-scope cookie config formatting
- cross-platform parity fixtures

Android and Apple remain responsible for platform concerns:

- repositories and storage
- Room/Core Data/UserDefaults/native persistence
- WebView, WebKit, and cookie-store APIs
- dependency injection and application lifecycle
- native feature-flag and privacy-config loading
- native TLD/eTLD+1 normalization where the host already owns that behavior

The host applications gather native inputs, map them into KMP DTOs, call `CookiesHostParityFacade`, and use the result.

```text
Android / Apple host app
        |
        | native repositories, config readers, WebKit/WebView APIs
        v
Thin platform adapter
        |
        | ToggleEvaluationInput / CookieMatchInput / ConfigFormattingInput
        v
CookiesHostParityFacade
        |
        v
Shared deterministic KMP business rules
```

## KMP Library Commit History

Repository: [FernandaFBMarques/Cookies-KMP](https://github.com/FernandaFBMarques/Cookies-KMP)

| Commit | Explanation |
| --- | --- |
| [`42d8119`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/42d8119) | Initial repository commit. Established the starting project history. |
| [`552253b`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/552253b) | Added the first README for the Cookies KMP project, documenting the purpose of the proof of concept. |
| [`6529bd8`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/6529bd8) | Added initial default project configuration from the Kotlin Multiplatform scaffold. |
| [`607b481`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/607b481) | Continued the default project configuration setup. |
| [`d5aae6f`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/d5aae6f) | Added the first cookies KMP core implementation and updated project documentation. This introduced shared cookies models, repositories/contracts, toggle evaluation, content-scope config formatting, cookie-name matching, stacktrace redaction, and common tests. |
| [`5220b06`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/5220b06) | Configured Maven Central publishing through GitHub Actions. |
| [`2a22ea6`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/2a22ea6) | Updated Maven Central publishing logs and workflow visibility. |
| [`cf21c0b`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/cf21c0b) | Removed an invalid `kotlin.text.insert` import that prevented clean compilation. |
| [`922af04`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/922af04) | Adjusted publishing workflow key configuration. |
| [`91c60fd`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/91c60fd) | Adjusted publishing workflow key values. |
| [`1f92ca1`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/1f92ca1) | Adjusted version values in workflow configuration. |
| [`24136a2`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/24136a2) | Prepared the `0.1.0` release for Maven Central. |
| [`06cc46e`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/06cc46e) | Switched Maven coordinates to the owned namespace `io.github.fernandafbmarques` and prepared the `0.1.1` release. |
| [`e4b34f5`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/e4b34f5) | Updated Markdown documentation after the library was published. |
| [`2783255`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/2783255) | Bumped the library version to `0.1.2`. |
| [`3efd558`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/3efd558) | Bumped the library version to `0.1.3`. |
| [`3777c6c`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/3777c6c) | Updated the Swift Package manifest binary URL and checksum for the `0.1.3` Apple release. |
| [`3051ccb`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/3051ccb) | Exposed `CookiesHostParityFacade` and prepared the `0.1.4` release. This commit added parity fixtures, Apple binary publishing scripts/workflows, cross-platform parity workflow, KMP adapter contracts, policy engine tests, and the public facade consumed by Android and Apple. |

## Android Integration Commits

Repository: [FernandaFBMarques/Android](https://github.com/FernandaFBMarques/Android)

Branch: [`cookies-kmp-integration`](https://github.com/FernandaFBMarques/Android/tree/cookies-kmp-integration)

### [`a44371fef`](https://github.com/FernandaFBMarques/Android/commit/a44371fef) - Delegate cookies toggle evaluation to KMP

This commit wires the KMP dependency into the Android cookies modules and migrates final toggle evaluation into shared logic.

Changed files:

- `cookies/cookies-impl/build.gradle`
- `cookies/cookies-store/build.gradle`
- `cookies/cookies-impl/src/main/java/com/duckduckgo/cookies/impl/features/CookiesFeatureTogglesPlugin.kt`
- `cookies/cookies-impl/src/test/java/com/duckduckgo/cookies/impl/CookiesHostParityFacadeIntegrationTest.kt`

Responsibilities after the change:

- Android still resolves the native `CookiesFeatureName`.
- Android still reads toggle state and minimum supported version from `CookiesFeatureToggleRepository`.
- Android still reads the app version from `AppBuildConfig`.
- KMP receives a `ToggleEvaluationInput`.
- KMP returns the final `isEnabled` decision through `CookiesHostParityFacade.evaluateToggle(...)`.

This keeps the Android plugin as a thin adapter while moving the deterministic version/toggle rule into shared code.

### [`3ac92673f`](https://github.com/FernandaFBMarques/Android/commit/3ac92673f) - Delegate Android cookie-name matching to KMP

This commit migrates the third-party cookie-name matching decision into KMP.

Changed files:

- `cookies/cookies-impl/src/main/java/com/duckduckgo/cookies/impl/thirdpartycookienames/RealThirdPartyCookieNames.kt`
- `cookies/cookies-impl/src/test/java/com/duckduckgo/cookies/impl/thirdpartycookienames/RealThirdPartyCookieNamesTest.kt`

Responsibilities after the change:

- Android still reads excluded cookie names from `CookiesRepository`.
- Android maps the native values into `CookieMatchInput`.
- KMP performs the deterministic cookie-name match.
- Android returns `hasExcludedCookieName` from the KMP result.

The Android `cookies-store` module only receives the dependency in this slice. Storage and repository behavior intentionally remain native.

## Apple Integration Commits

Repository: [FernandaFBMarques/apple-browsers](https://github.com/FernandaFBMarques/apple-browsers)

Branch: [`cookies-kmp-integrate`](https://github.com/FernandaFBMarques/apple-browsers/tree/cookies-kmp-integrate)

### [`ccda915659`](https://github.com/FernandaFBMarques/apple-browsers/commit/ccda915659) - Wire CookiesKMP into BrowserServicesKit

This commit adds the `CookiesKMP` binary target to `SharedPackages/BrowserServicesKit/Package.swift`.

The package now makes the KMP binary available to:

- `BrowserServicesKit`
- `BrowserServicesKitTests`

This creates the Apple dependency boundary without importing the KMP binary directly throughout the app.

### [`3786b2eee7`](https://github.com/FernandaFBMarques/apple-browsers/commit/3786b2eee7) - Evaluate cookies config through KMP on Apple

This commit migrates Apple content-scope cookie config gating and formatting to KMP.

Changed files:

- `SharedPackages/BrowserServicesKit/Sources/BrowserServicesKit/ContentScopeScript/ContentScopePrivacyConfigurationJSONGenerator.swift`
- `SharedPackages/BrowserServicesKit/Tests/BrowserServicesKitTests/ContentScopeScript/ContentScopePrivacyConfigurationJSONGeneratorTests.swift`
- `SharedPackages/BrowserServicesKit/Tests/BrowserServicesKitTests/ContentScopeScript/CookiesHostParityFacadeTests.swift`

Responsibilities after the change:

- Apple still loads the native privacy configuration.
- Apple still determines native internal-user state through `featureFlagger.internalUserDecider`.
- Apple maps the native state into `ToggleEvaluationInput`.
- KMP evaluates whether the cookie config should be injected.
- KMP formats the content-scope cookie config fragment.

The tests cover disabled cookies, minimum-version gating, JSON fragment formatting, and KMP parity fixtures.

### [`021770a514`](https://github.com/FernandaFBMarques/apple-browsers/commit/021770a514) - Delegate Apple cookie-domain matching to KMP

This commit adds `CookiesDomainMatcher`, a small Apple adapter around `CookiesHostParityFacade.evaluateCookie(...)`.

Changed files:

- `SharedPackages/BrowserServicesKit/Sources/BrowserServicesKit/Cookies/CookiesDomainMatcher.swift`
- `SharedPackages/BrowserServicesKit/Tests/BrowserServicesKitTests/ContentScopeScript/CookiesDomainMatcherTests.swift`
- `iOS/Core/HTTPCookieExtension.swift`

Responsibilities after the change:

- iOS keeps WebKit and cookie access native.
- iOS passes cookie-domain inputs to `CookiesDomainMatcher`.
- KMP performs the deterministic allowed-domain match.

### [`cf9e76d075`](https://github.com/FernandaFBMarques/apple-browsers/commit/cf9e76d075) - Delegate macOS cookie-domain matching to KMP

This commit extends the Apple integration to macOS cookie-domain helpers.

Changed files:

- `macOS/DuckDuckGo/Common/Extensions/HTTPCookie.swift`
- `macOS/DuckDuckGo/Fireproofing/Model/FireproofDomains.swift`

Responsibilities after the change:

- macOS still owns native fireproofing storage and TLD/eTLD+1 normalization.
- macOS maps native cookie domains into the shape expected by the shared matcher.
- KMP performs the final deterministic cookie-domain matching decision.

The macOS adapter preserves the previous eTLD+1 behavior for subdomain cookies while delegating the final match to KMP.

## Testing And Validation

### KMP library tests

The KMP library contains shared tests for:

- `CookiesPolicyEngineTest`
- `CrossPlatformParityFixturesTest`
- `CookiesRepositoryAdaptersTest`
- `CookiesFeatureStoreUpdaterTest`
- `StacktraceRedactorTest`

Relevant command:

```bash
./gradlew :shared:testDebugUnitTest
```

### Android validation

Focused Android tests passed for the migrated cookies slice:

```bash
./gradlew :cookies-impl:testDebugUnitTest \
  --tests com.duckduckgo.cookies.impl.features.CookiesFeatureTogglesPluginTest \
  --tests com.duckduckgo.cookies.impl.thirdpartycookienames.RealThirdPartyCookieNamesTest \
  --tests com.duckduckgo.cookies.impl.CookiesHostParityFacadeIntegrationTest \
  --no-daemon
```

Validated behavior:

- Android delegates toggle evaluation to KMP.
- Android delegates third-party cookie-name matching to KMP.
- Android and KMP parity fixtures return expected outputs.
- Android storage/repository boundaries remain native.

### Apple validation

Focused Swift Package tests passed:

```bash
swift test --package-path SharedPackages/BrowserServicesKit \
  --filter 'CookiesHostParityFacadeTests|CookiesDomainMatcherTests|ContentScopePrivacyConfigurationJSONGeneratorTests'
```

Result:

```text
Executed 12 tests, with 0 failures
```

Validated behavior:

- Apple delegates content-scope cookie toggle evaluation to KMP.
- Apple delegates content-scope cookie config formatting to KMP.
- Apple delegates iOS cookie-domain matching to KMP.
- Apple delegates macOS cookie-domain matching to KMP.
- KMP parity fixtures are callable from Swift.

Full macOS Xcode validation was attempted, but package resolution was blocked by existing dependency issues unrelated to the cookies migration:

- private `native-apps-ducksans` repository access
- `BloomFilter` binary target artifact mapping
- `URLPredictorRust` binary target artifact mapping

## Boundary Decisions

The most important architectural decision is that the KMP library does not replace host infrastructure.

It does not own:

- Android repositories
- Android Room storage
- Apple UserDefaults/Core Data storage
- WebView or WebKit APIs
- app lifecycle
- dependency injection
- native release/runtime wiring

It owns only the rules that are deterministic and portable.

This is why many host files still exist after the migration. They are adapters, repositories, or native platform integration points. The desired end state is not a fully shared app. The desired end state is a shared source of truth for cookies policy decisions.

## How To Publish This Documentation With GitHub Pages

This repository includes a `/docs` directory configured for Jekyll using the `just-the-docs` theme.

To preview locally:

```bash
cd docs
docker-compose up
```

Then open:

```text
http://127.0.0.1:4000
```

To publish on GitHub Pages:

1. Open the repository settings for [FernandaFBMarques/Cookies-KMP](https://github.com/FernandaFBMarques/Cookies-KMP).
2. Go to **Pages**.
3. Select the `main` branch.
4. Select the `/docs` folder as the source.
5. Save.

The page should become available at:

```text
https://fernandafbmarques.github.io/Cookies-KMP/
```
