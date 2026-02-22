# Codex Workplan

## Objective
Complete cookies feature extraction by maximizing shared business logic and minimizing platform-specific duplication.

## Current progress snapshot
- Completed: KMP core extracted and integrated with Android wrappers.
- Completed: Maven Central release pipeline established and `0.1.1` published.
- In progress: iOS adapter implementation and cross-platform parity hardening.

## Phase 1: Stabilize shared core
- Keep shared models and decision logic complete and tested.
- Ensure no Android-only imports leak into `commonMain`.
- Maintain backward-compatible behavior for current Android wrappers.

## Phase 2: Expand adapter surface
- Create explicit Android and iOS adapter interfaces for:
  - cookie settings persistence,
  - content-scope payload persistence,
  - toggle state persistence,
  - app version provider.
- Keep adapter implementations out of shared code.

## Phase 3: iOS implementation
- Add iOS adapter implementations.
- Validate config parsing/toggle/matching outputs against Android expectations.
- Add iOS-targeted tests and contract tests.

## Phase 4: Parity hardening
- Compare outputs from Android and iOS for identical config inputs.
- Validate edge cases (unknown JSON keys, defaults, missing fields).
- Track and close any semantic differences.

## Phase 5: Incremental migration in host apps
- Replace remaining duplicated logic with KMP calls.
- Keep temporary wrappers where needed for compatibility.
- Remove dead duplicate code only after parity confirmation.

## Acceptance checklist
- [x] Shared core covers all targeted business logic.
- [x] Android integration path is stable and tested.
- [ ] iOS adapters exist and compile in a configured Xcode environment.
- [ ] Cross-platform decision parity is demonstrated with tests.
- [x] Migration notes and maintenance guidelines are documented.

## Upcoming milestones
1) Finish iOS adapters and parity tests.
2) Validate one additional policy/config change shipped via shared core.
3) Release next public version (`0.1.2`) after parity checkpoints.

## Commands (Android repo root)
```bash
./gradlew :cookies-store:testDebugUnitTest :cookies-impl:testDebugUnitTest
./gradlew :cookies-kmp:cookies-kmp-core:testDebugUnitTest
```

Note: full iOS target tasks require a valid local Xcode CLI setup.
