#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 2 ]]; then
  echo "Usage: $0 <version> <checksum>"
  exit 1
fi

VERSION="$1"
CHECKSUM="$2"
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MANIFEST="$ROOT_DIR/apple-spm/CookiesKMP/Package.swift"

if [[ ! -f "$MANIFEST" ]]; then
  echo "Missing manifest: $MANIFEST"
  exit 1
fi

URL="https://github.com/FernandaFBMarques/Cookies-KMP/releases/download/${VERSION}/CookiesKMP.xcframework.zip"

sed -i.bak \
  -e "s|url: \".*CookiesKMP.xcframework.zip\"|url: \"${URL}\"|g" \
  -e "s|checksum: \".*\"|checksum: \"${CHECKSUM}\"|g" \
  "$MANIFEST"

rm -f "${MANIFEST}.bak"
echo "Updated $MANIFEST"

