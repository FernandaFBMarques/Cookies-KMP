#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUTPUT_DIR="$ROOT_DIR/build/apple-release"
ZIP_PATH="$OUTPUT_DIR/CookiesKMP.xcframework.zip"
XCF_DIR="$OUTPUT_DIR/CookiesKMP.xcframework"

mkdir -p "$OUTPUT_DIR"
rm -rf "$XCF_DIR" "$ZIP_PATH"

echo "Building Apple frameworks from KMP module..."
"$ROOT_DIR/gradlew" \
  :cookies-kmp-core:linkReleaseFrameworkIosArm64 \
  :cookies-kmp-core:linkReleaseFrameworkIosSimulatorArm64 \
  :cookies-kmp-core:linkReleaseFrameworkMacosArm64

IOS_ARM64="$ROOT_DIR/shared/build/bin/iosArm64/releaseFramework/CookiesKMP.framework"
IOS_SIM_ARM64="$ROOT_DIR/shared/build/bin/iosSimulatorArm64/releaseFramework/CookiesKMP.framework"
MACOS_ARM64="$ROOT_DIR/shared/build/bin/macosArm64/releaseFramework/CookiesKMP.framework"

for framework in "$IOS_ARM64" "$IOS_SIM_ARM64" "$MACOS_ARM64"; do
  if [[ ! -d "$framework" ]]; then
    echo "Missing framework: $framework"
    exit 1
  fi
done

xcodebuild -create-xcframework \
  -framework "$IOS_ARM64" \
  -framework "$IOS_SIM_ARM64" \
  -framework "$MACOS_ARM64" \
  -output "$XCF_DIR"

(
  cd "$OUTPUT_DIR"
  ditto -c -k --sequesterRsrc --keepParent CookiesKMP.xcframework CookiesKMP.xcframework.zip
)

echo "Built artifact:"
echo "  $ZIP_PATH"
