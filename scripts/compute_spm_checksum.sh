#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ZIP_PATH="${1:-$ROOT_DIR/build/apple-release/CookiesKMP.xcframework.zip}"

if [[ ! -f "$ZIP_PATH" ]]; then
  echo "Missing zip artifact: $ZIP_PATH"
  echo "Run scripts/build_apple_xcframework.sh first."
  exit 1
fi

echo "Computing checksum for:"
echo "  $ZIP_PATH"
swift package compute-checksum "$ZIP_PATH"

