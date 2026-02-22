# Business Case

## Executive summary
The cookies feature currently contains a mix of policy logic and Android-specific integrations. This POC evaluates whether moving reusable logic to a KMP core can reduce duplicate engineering effort across Android and iOS while preserving a native-first implementation strategy.

## Hypothesis
A shared KMP core for cookies logic will:
- lower implementation time for cross-platform feature updates,
- reduce divergence bugs between Android and iOS behavior,
- improve maintainability by centralizing policy decisions.

## Baseline problem
Without a shared core:
- cookie policy changes are implemented twice,
- interpretation of privacy config can drift by platform,
- testing burden increases because logic must be validated independently.

## Target operating model
- Shared KMP layer: policy + config + decisioning logic.
- Native adapters: platform cookie APIs, storage specifics, background scheduling.

## Expected benefits
- Faster delivery for policy-level changes.
- Fewer logic inconsistencies between platforms.
- Better traceability for privacy behavior changes.

## Constraints
- Platform cookie storage internals are different and must stay native.
- Existing Android behavior must not regress.
- iOS parity must be validated through adapter-level tests.

## Risks and mitigations
- Risk: shared model diverges from platform behavior.
  Mitigation: contract tests + parity tests against platform adapters.
- Risk: migration introduces regressions.
  Mitigation: incremental replacement with compatibility wrappers.
- Risk: build/tooling friction in KMP setup.
  Mitigation: isolate KMP project and integrate via composite build.

## Measurement plan
Track before/after for:
- time-to-deliver for policy/config changes,
- number of platform-specific bug fixes tied to cookies logic,
- amount of duplicated cookies logic (LOC or module surface),
- test pass rates for shared and adapter suites.

## Progress update
- Shared KMP core is implemented and integrated into Android-side wrappers.
- Public distribution channel is now active via Maven Central.
- Initial public release is available as `io.github.fernandafbmarques:cookies-kmp-core:0.1.1`.
- Remaining value realization depends on iOS adapter parity and cross-platform contract tests.

## Decision gates
- Gate 1: Android parity maintained after KMP delegation. Status: achieved.
- Gate 2: iOS adapter implemented with equivalent decision outcomes. Status: in progress.
- Gate 3: at least one real policy update shipped via shared core path. Status: in progress.
