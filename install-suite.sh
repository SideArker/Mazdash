#!/usr/bin/env bash
set -e

DEVICE="${1:-}"
ADB_CMD="adb"
if [ -n "$DEVICE" ]; then
    ADB_CMD="adb -s $DEVICE"
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MAZDASH_APK="$SCRIPT_DIR/app/build/outputs/apk/legacy/debug/app-legacy-debug.apk"
HEADUNIT_APK="$SCRIPT_DIR/open-headunit/app/build/outputs/apk/github/debug/com.andrerinas.headunitrevived_4.0.0-beta1_debug.apk"

echo "=== Instalacja pakietu Mazdash Suite ==="

if [ ! -f "$MAZDASH_APK" ]; then
    echo "Brak zbuildowanego APK Mazdash, kompiluję..."
    (cd "$SCRIPT_DIR" && ./gradlew assembleLegacyDebug)
fi

if [ ! -f "$HEADUNIT_APK" ]; then
    echo "Brak zbuildowanego APK Open Headunit, kompiluję..."
    (cd "$SCRIPT_DIR/open-headunit" && ./gradlew assembleGithubDebug)
fi

echo "1/2 Instalacja launchera Mazdash..."
$ADB_CMD install -r "$MAZDASH_APK"

echo "2/2 Instalacja odbiornika Open Headunit (Mazda Edition)..."
$ADB_CMD install -r "$HEADUNIT_APK"

echo "Nadawanie uprawnień systemowych dla Open Headunit..."
$ADB_CMD shell pm grant com.andrerinas.headunitrevived android.permission.RECORD_AUDIO 2>/dev/null || true
$ADB_CMD shell pm grant com.andrerinas.headunitrevived android.permission.ACCESS_FINE_LOCATION 2>/dev/null || true
$ADB_CMD shell pm grant com.andrerinas.headunitrevived android.permission.READ_PHONE_STATE 2>/dev/null || true

echo "=== Sukces! Pakiet Mazdash i Open Headunit zainstalowane pomyślnie ==="
