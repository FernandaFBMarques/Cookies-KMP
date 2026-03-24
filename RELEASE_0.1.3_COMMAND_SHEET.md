# Release 0.1.3 Command Sheet

This is a copy/paste runbook to validate and release `0.1.3` for:
- Android (Maven Central)
- Apple (SwiftPM binary `CookiesKMP.xcframework.zip`)

## 0) Preconditions
- Android SDK configured (`ANDROID_HOME` or `local.properties` with `sdk.dir=...`)
- Xcode CLI configured (`xcodebuild -version` must work)
- Swift installed (`swift --version`)
- GitHub repo access with permission to create tags/releases
- Maven Central credentials/signing configured (CI secrets or local env)

## 1) Move to repo
```bash
cd /Users/mariafernandamarques/IdeaProjects/Cookies-KMP
```

## 2) Set version to 0.1.3
Update `shared/build.gradle.kts`:
```kotlin
version = "0.1.3"
```

Commit:
```bash
git add shared/build.gradle.kts
git commit -m "chore: bump version to 0.1.3"
```

## 3) Validate before release
```bash
# shared compile
./gradlew :cookies-kmp-core:compileCommonMainKotlinMetadata

# Android unit tests
./gradlew :cookies-kmp-core:testDebugUnitTest

# Apple tests (if Xcode CLI is configured)
./gradlew :cookies-kmp-core:iosSimulatorArm64Test :cookies-kmp-core:macosX64Test

# aggregate (optional)
./gradlew :cookies-kmp-core:allTests
```

## 4) Build Apple binary zip
```bash
./scripts/build_apple_xcframework.sh
ls -la build/apple-release/CookiesKMP.xcframework.zip
```

## 5) Compute SwiftPM checksum
```bash
CHECKSUM=$(./scripts/compute_spm_checksum.sh | tail -n 1)
echo "$CHECKSUM"
```

## 6) Tag release in KMP repo
```bash
git tag 0.1.3
git push origin main
git push origin 0.1.3
```

## 7) Publish Android artifact (Maven Central)
Option A: CI release workflow (recommended)
- Trigger `.github/workflows/publish-maven-central.yml` with `version=0.1.3`.

Option B: local publish
```bash
./gradlew :cookies-kmp-core:publishAndReleaseToMavenCentral --no-daemon --stacktrace --info
```

## 8) Publish Apple binary release asset
Upload this file to GitHub Release `0.1.3`:
- `build/apple-release/CookiesKMP.xcframework.zip`

## 9) Update SwiftPM manifest for 0.1.3
```bash
./scripts/update_spm_manifest.sh 0.1.3 "$CHECKSUM"
git add apple-spm/CookiesKMP/Package.swift
git commit -m "chore(spm): update CookiesKMP binary URL/checksum for 0.1.3"
git push origin main
```

## 10) Verify SPM manifest
Check:
- URL points to `.../releases/download/0.1.3/CookiesKMP.xcframework.zip`
- checksum matches `CHECKSUM`

```bash
sed -n '1,200p' apple-spm/CookiesKMP/Package.swift
```

## 11) Consumer updates

## Android consumer (`/Users/mariafernandamarques/StudioProjects/Android`)
If using released artifact:
- Ensure dependency is `io.github.fernandafbmarques:cookies-kmp-core:0.1.3`

If using local composite build:
- keep `includeBuild(...)` + substitution and optionally keep version string as-is.

Quick validate:
```bash
cd /Users/mariafernandamarques/StudioProjects/Android
./gradlew :cookies-impl:help
```

## Apple consumer (`/Users/mariafernandamarques/Desktop/apple-browsers`)
1. Add/update SPM package to tag `0.1.3`
2. Link `CookiesKMP` product to needed targets
3. Build iOS/macOS targets and run tests

## 12) Final smoke checks
Back in KMP repo:
```bash
cd /Users/mariafernandamarques/IdeaProjects/Cookies-KMP
./gradlew :cookies-kmp-core:compileCommonMainKotlinMetadata
```

In Android repo:
```bash
cd /Users/mariafernandamarques/StudioProjects/Android
./gradlew :cookies-impl:help
```

In Apple repo:
- Resolve package dependencies in Xcode
- Build iOS and macOS schemes
- Run target tests related to cookies/content-scope/fireproofing

## 13) Release notes template
- Version: `0.1.3`
- Android artifact: `io.github.fernandafbmarques:cookies-kmp-core:0.1.3`
- Apple binary: `CookiesKMP.xcframework.zip` (tag `0.1.3`)
- SwiftPM checksum: `<CHECKSUM>`
- Key changes:
  - Apple + macOS target stabilization
  - adapter contracts/DTOs
  - parity fixtures/tests
  - release automation scripts

