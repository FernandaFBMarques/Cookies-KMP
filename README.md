# DuckDuckGo Cookies KMP POC

## Why this project exists
This project exists to test whether the DuckDuckGo Android cookies feature can be extracted into a Kotlin Multiplatform (KMP) core and reused by both Android and iOS while keeping native platform integrations.

This is a **college study case / proof of concept**, not an official DuckDuckGo initiative.

## Business case in one sentence
If policy and feature logic are shared once in KMP and only platform adapters stay native, cross-platform effort should decrease without losing native behavior where platform internals differ.

## What success looks like
- Shared logic lives in `commonMain` and is consumed from Android and iOS.
- Android behavior remains functionally equivalent after migration.
- iOS can implement equivalent behavior through platform adapters.
- Future cookies policy changes are implemented once in shared code.

## What is in scope
- Cookies feature config parsing and storage orchestration.
- Feature-toggle evaluation.
- Third-party cookie name matching logic.
- Shared utility logic (for example stacktrace redaction/base64 behavior).

## What is out of scope
- Forcing identical low-level DB/WebView implementation across platforms.
- Replacing native platform cookie APIs.
- Full app-wide migration in one step.

## Current structure
- `cookies-kmp-core/src/commonMain`: shared cookies domain logic.
- `cookies-kmp-core/src/androidMain`: Android actuals/adapters.
- `cookies-kmp-core/src/iosMain`: iOS actual placeholders/adapters.
- `cookies-kmp-core/src/commonTest`: shared logic tests.

## Integration model
This KMP project is included from the Android repo as a composite build and consumed as:
- `com.duckduckgo.cookies:cookies-kmp-core:0.1.0-SNAPSHOT`

## Current status
- KMP core created and wired to Android cookies modules.
- Android wrappers now delegate selected business logic to KMP core.
- Android unit tests for `cookies-store` and `cookies-impl` pass.
- iOS compilation/tests require a valid local Xcode CLI setup.

## Runbook
From Android repo root:

```bash
./gradlew :cookies-store:testDebugUnitTest :cookies-impl:testDebugUnitTest
./gradlew :cookies-kmp:cookies-kmp-core:testDebugUnitTest
```

## Main risks
- iOS adapter parity for platform-specific cookie behavior.
- Continued AGP/Kotlin MPP version compatibility drift.
- Keeping Android behavior exactly stable during incremental migration.

See also:
- `BUSINESS_CASE.md`
- `CODEX_CONTEXT.md`
- `CODEX_WORKPLAN.md`
