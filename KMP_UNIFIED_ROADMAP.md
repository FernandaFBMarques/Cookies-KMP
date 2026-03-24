# KMP Unified Plan (Android + iOS + macOS)

## Objective
Use a single Kotlin Multiplatform project as the source of truth for cookies business logic and consume it from:
- Android (Maven artifact)
- iOS (Swift Package with binary xcframework)
- macOS (same Swift Package with binary xcframework)

This keeps one shared logic codebase while using platform-native distribution formats.

## Target Architecture

### Shared KMP modules
- `commonMain`: business logic, policy decisions, parsing, matching, utilities
- `appleMain` (optional but recommended): shared Apple adapter contracts and common Apple-side glue
- `iosMain`: iOS-specific adapter implementations
- `macosMain`: macOS-specific adapter implementations
- `androidMain`: Android-specific adapter implementations

### Host app boundaries
- Shared KMP owns: deterministic business rules
- Native apps own: platform APIs, lifecycle, storage engines, UI integration, WebKit/WebView specifics

## Distribution Strategy

### Android
- Continue publishing `io.github.fernandafbmarques:cookies-kmp-core:<version>` to Maven Central.

### Apple (iOS + macOS)
- Build KMP Apple framework as `.xcframework`
- Zip and publish release asset (usually GitHub Releases)
- Expose via Swift Package Manager (`Package.swift` + `binaryTarget(url:, checksum:)`)
- Consume package from `apple-browsers` using SPM dependency

## Phased Implementation Plan

## Phase 1: KMP source-set hardening
1. Ensure `shared/build.gradle.kts` has Apple targets needed by both apps:
   - `iosArm64`, `iosSimulatorArm64`, `iosX64` (if needed)
   - `macosArm64`, `macosX64`
2. Introduce `appleMain` source set and wire dependencies:
   - `appleMain` depends on `commonMain`
   - `iosMain` and `macosMain` depend on `appleMain`
3. Keep platform adapters thin; keep policy logic in `commonMain`.

Exit criteria:
- KMP module compiles for Android, iOS, macOS targets locally.

## Phase 2: Apple adapter surface
1. Define adapter interfaces for Apple-side integrations (storage, config feed, toggles feed, app version provider).
2. Implement concrete adapters in:
   - `iosMain` for iOS-specific behavior
   - `macosMain` for macOS-specific behavior
3. Keep host app integration points unchanged initially; add wrappers first.

Exit criteria:
- iOS/macOS adapters compile and return equivalent outputs for known fixtures.

## Phase 3: Apple packaging (SPM binary package)
1. Add build automation in KMP repo to produce `CookiesKMP.xcframework`.
2. Zip artifact and compute checksum:
   - `swift package compute-checksum CookiesKMP.xcframework.zip`
3. Create/update a Swift package manifest:
   - product: `CookiesKMP`
   - target: `.binaryTarget(name:url:checksum:)`
4. Publish package + asset per release tag.

Exit criteria:
- A clean iOS/macOS sample project can resolve and import `CookiesKMP` using SPM only.

## Phase 4: Integrate in `apple-browsers`
1. Add SPM dependency in Apple repo.
2. Integrate behind existing entry points first:
   - content-scope privacy config generation
   - toggle evaluation helpers used by content-scope/autofill
   - cookie-domain/cookie-name matching where applicable
3. Keep fallback path for one release behind a debug flag if needed.

Suggested initial file touchpoints in `apple-browsers`:
- `SharedPackages/BrowserServicesKit/Sources/BrowserServicesKit/ContentScopeScript/ContentScopePrivacyConfigurationJSONGenerator.swift`
- `iOS/DuckDuckGo/AutofillContentScopeFeatureToggles.swift`
- `iOS/Core/HTTPCookieExtension.swift`
- `iOS/Core/Fireproofing.swift`
- Script assembly points:
  - `iOS/DuckDuckGo/UserScripts.swift`
  - `iOS/DuckDuckGo/Subscription/AsyncHeadlessWebview/HeadlessWebView.swift`
  - `iOS/DuckDuckGo/DuckPlayer/NativeUI/Views/DuckPlayerWebView.swift`

Exit criteria:
- iOS/macOS host apps compile and run with KMP-backed logic for selected paths.

## Phase 5: Parity validation
1. Add cross-platform fixture tests (same inputs, same expected outputs) for:
   - config parsing and filtering
   - toggle resolution outputs
   - cookie matching behavior
2. Compare Android vs iOS vs macOS output snapshots.
3. Block rollout if any behavior drift is detected.

Suggested test targets:
- KMP: `commonTest` + Apple target tests
- Apple repo:
  - `iOS/DuckDuckGoTests/ScriptSourceProviderTests.swift`
  - `iOS/WebViewUnitTests/UserDefaultsFireproofingTests.swift`
  - BrowserServicesKit content-scope and feature-flag tests

Exit criteria:
- Parity suite green and reviewed.

## Release Workflow (single version across ecosystems)
For each release (example `0.1.2`):
1. Tag KMP repo version.
2. Publish Android artifact to Maven Central.
3. Build and upload Apple `xcframework.zip` release asset.
4. Update SPM `checksum` and package tag.
5. Update host app dependency pins:
   - Android version in Gradle
   - Apple package version in Xcode/SPM
6. Run smoke tests in both host repos.

## CI/CD Blueprint

## KMP CI jobs
- `build-android`: Android targets + unit tests
- `build-apple-framework`: build xcframework for iOS/macOS
- `parity-tests`: shared fixture tests
- `publish-android`: Maven publish on release tag
- `publish-apple-package`: upload xcframework zip + update/release package metadata

## Apple consumer CI jobs
- Resolve SPM dependency at pinned tag
- Build iOS and macOS targets
- Run unit/integration tests for touched cookie paths

## Risks and Mitigations
- Risk: behavior drift between native and KMP
  - Mitigation: fixture-based parity tests and staged rollout
- Risk: Apple packaging friction (checksum/tag mismatch)
  - Mitigation: scripted release step generating checksum automatically
- Risk: API churn in adapters
  - Mitigation: stable adapter interfaces, deprecate gradually
- Risk: platform-specific edge cases leaking into `commonMain`
  - Mitigation: keep platform APIs isolated in `iosMain`/`macosMain`/`androidMain`

## Implementation Checklist
- [ ] Add/validate KMP Apple targets (`ios*` + `macos*`)
- [ ] Create `appleMain` and move Apple-shared glue there
- [ ] Implement iOS and macOS adapter classes
- [ ] Build distributable `xcframework`
- [ ] Publish Swift Package binary target
- [ ] Integrate package in `apple-browsers`
- [ ] Route first integration path (config formatter)
- [ ] Route toggle evaluation and cookie matching
- [ ] Add/green parity tests across Android/iOS/macOS
- [ ] Release version and pin dependencies in host apps

## Recommended First Milestone
Ship one narrow vertical slice end-to-end:
1. KMP formatter available in Apple package
2. Integrated at `ContentScopePrivacyConfigurationJSONGenerator`
3. Tests green on Android + iOS + macOS
4. Release as `0.1.2`

Then expand to toggle evaluation and cookie matching in subsequent milestones.
