#!/usr/bin/env bash
# Preflight checks for CamFeed ATAK plugin before TAK.gov submission.
# Run from the CamFeed/camfeed/ directory: ./preflight.sh
# Exit 0 = all checks passed. Exit 1 = one or more failures.

set -uo pipefail

PLUGIN_PKG="com.atakmap.android.camfeed"
DEXDUMP=$(ls /Users/stephanpellegrini/Library/Android/sdk/build-tools/*/dexdump 2>/dev/null | sort -V | tail -1)
APK=$(find "$(dirname "$0")/app/build/outputs/apk/civ/release" -name "*.apk" 2>/dev/null | head -1)

PASS=0
FAIL=0

check() {
    local desc="$1" result="$2"
    if [[ "$result" == "0" ]]; then
        echo "  [PASS] $desc"
        ((PASS++)) || true
    else
        echo "  [FAIL] $desc"
        ((FAIL++)) || true
    fi
}

echo "=== CamFeed preflight ==="
echo ""

# 1. APK exists
echo "[1] APK exists"
if [[ -n "$APK" && -f "$APK" ]]; then
    echo "  [PASS] $APK"
    ((PASS++)) || true
else
    echo "  [FAIL] No APK found — run ./gradlew assembleCivRelease first"
    ((FAIL++)) || true
    APK=""
fi

# 2. No leaked ATAK SDK classes in DEX (plugin's own com.atakmap.android.camfeed namespace is allowed)
echo "[2] DEX leak check"
if [[ -n "$APK" && -n "$DEXDUMP" ]]; then
    LEAKED=$("$DEXDUMP" "$APK" 2>/dev/null | grep "^  Class descriptor" | grep -E "Lcom/atakmap|Lgov/tak" | grep -v "L${PLUGIN_PKG//./\/}" | wc -l | tr -d ' ')
    check "0 leaked SDK classes in DEX (found: $LEAKED)" "$([[ "$LEAKED" -eq 0 ]] && echo 0 || echo 1)"
else
    echo "  [SKIP] dexdump or APK not available"
fi

# 3. APK size sanity (< 5MB)
echo "[3] APK size sanity (< 5 MB)"
if [[ -n "$APK" ]]; then
    SIZE=$(stat -f%z "$APK" 2>/dev/null || stat -c%s "$APK")
    check "APK size ${SIZE} bytes < 5242880" "$([[ "$SIZE" -lt 5242880 ]] && echo 0 || echo 1)"
fi

# 4. libs/ does NOT contain main.jar (CamFeed should not have SDK JAR at all)
echo "[4] libs/ has no SDK JAR (main.jar)"
check "no main.jar in app/libs/" "$([[ ! -f "$(dirname "$0")/app/libs/main.jar" ]] && echo 0 || echo 1)"

echo ""
echo "=== Results: ${PASS} passed, ${FAIL} failed ==="
[[ "$FAIL" -eq 0 ]]
