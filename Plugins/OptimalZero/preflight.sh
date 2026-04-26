#!/usr/bin/env bash
# Preflight checks for OptimalZero ATAK plugin before TAK.gov submission.
# Run from the OptimalZero/ directory: ./preflight.sh
# Exit 0 = all checks passed. Exit 1 = one or more failures.

set -uo pipefail

PLUGIN_PKG="com.optimalzero.plugin"
DEXDUMP=$(ls /Users/stephanpellegrini/Library/Android/sdk/build-tools/*/dexdump 2>/dev/null | sort -V | tail -1)
APK=$(find "$(dirname "$0")/app/build/outputs/apk/civ/release" -name "*.apk" 2>/dev/null | head -1)
KEYSTORE="$(dirname "$0")/app/build/android_keystore"

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

echo "=== OptimalZero preflight ==="
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

# 2. Keystore exists
echo "[2] Keystore present"
check "keystore at app/build/android_keystore" "$([[ -f "$KEYSTORE" ]] && echo 0 || echo 1)"

# 3. No leaked ATAK SDK classes in DEX
echo "[3] DEX leak check"
if [[ -n "$APK" && -n "$DEXDUMP" ]]; then
    LEAKED=$("$DEXDUMP" "$APK" 2>/dev/null | grep "^  Class descriptor" | grep -E "Lcom/atakmap|Lgov/tak" | grep -v "L${PLUGIN_PKG//./\/}" | wc -l | tr -d ' ')
    check "0 leaked SDK classes in DEX (found: $LEAKED)" "$([[ "$LEAKED" -eq 0 ]] && echo 0 || echo 1)"
else
    echo "  [SKIP] dexdump or APK not available"
fi

# 4. APK size sanity (plugin-only APK should be < 5MB; SDK-bundled APK was ~32MB)
echo "[4] APK size sanity (< 5 MB)"
if [[ -n "$APK" ]]; then
    SIZE=$(stat -f%z "$APK" 2>/dev/null || stat -c%s "$APK")
    check "APK size ${SIZE} bytes < 5242880" "$([[ "$SIZE" -lt 5242880 ]] && echo 0 || echo 1)"
fi

# 5. main.jar still present in libs/ (needed for compilation)
echo "[5] main.jar present in libs/"
check "libs/main.jar exists" "$([[ -f "$(dirname "$0")/app/libs/main.jar" ]] && echo 0 || echo 1)"

# 6. build.gradle uses compileOnly for libs/*.jar
echo "[6] build.gradle uses compileOnly for libs/*.jar"
COMPILE_ONLY=$(grep -c "compileOnly fileTree(dir: 'libs'" "$(dirname "$0")/app/build.gradle" || true)
check "compileOnly fileTree in build.gradle" "$([[ "$COMPILE_ONLY" -ge 1 ]] && echo 0 || echo 1)"

echo ""
echo "=== Results: ${PASS} passed, ${FAIL} failed ==="
[[ "$FAIL" -eq 0 ]]
