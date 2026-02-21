# Codex Context

## Mission
Extract reusable cookies business logic into this KMP project, keep platform-specific behavior in adapters, and preserve Android compatibility during migration.

## North star
Single-source policy logic, native platform execution.

## Source of truth
- KMP shared code lives under `cookies-kmp-core/src/commonMain`.
- Platform bindings live under `androidMain` and `iosMain`.
- Android integration currently uses wrapper/adapters in Android repo cookies modules.

## Non-negotiable invariants
- Do not break existing Android app contracts (`cookies-api` interfaces still work).
- Keep behavior equivalent for config parsing, toggle checks, and matching logic.
- Avoid moving Android-only APIs (WebView SQLite internals, WorkManager, etc.) into shared code.

## Current integration points (Android side)
- `CookiesFeaturePlugin` delegates to KMP store updater.
- `CookiesFeatureTogglesPlugin` delegates to KMP toggle evaluator.
- `RealThirdPartyCookieNames` delegates to KMP matcher.
- `CookiesContentScopeConfigPlugin` uses KMP formatter.
- `CookiesFeatureNameUtil.redactStacktraceInBase64` delegates to KMP.

## Local composite build (DDG Android)
The DDG Android repo consumes this project via Gradle composite build with dependency substitution:
```groovy
includeBuild("/Users/mariafernandafreitasbarbosamarques/IdeaProjects/CookiesKMP") {
    dependencySubstitution {
        substitute(module("io.github.fernandafbmarques:cookies-kmp-core"))
            .using(project(":cookies-kmp-core"))
    }
}
```
Consumers use `implementation "io.github.fernandafbmarques:cookies-kmp-core:0.1.1"`.

## Maven Central publishing plan
1) Register Sonatype Central account and `groupId` namespace.
2) Create a GPG signing key for release artifacts.
3) Configure Gradle `maven-publish` and `signing` with coordinates.
4) Publish with `./gradlew publishAllPublicationsToMavenCentralRepository`.
5) Release in Sonatype Central Portal.

## What still needs platform work
- iOS concrete adapters for cookie persistence and cookie API integration.
- iOS parity test suite and cross-platform contract verification.

## Codex guardrails
- Prefer adding shared behavior in `commonMain` first.
- Keep adapter classes thin and explicit.
- Add tests for every shared behavior change.
- If behavior parity is unclear, preserve existing Android semantics first.

## Done criteria for a change
- Compiles in Android integration path.
- Relevant unit tests updated/passing.
- No API break to existing Android consumers unless explicitly planned.
- Documentation updated when architecture or boundaries change.
