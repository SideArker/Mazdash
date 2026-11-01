#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MAZDASH_APK="$SCRIPT_DIR/dist/Mazdash-debug.apk"
HEADUNIT_APK="$SCRIPT_DIR/dist/OpenHeadunit-debug.apk"

# Fallback do build katalogów jeśli dist nie istnieje
if [ ! -f "$MAZDASH_APK" ]; then
    MAZDASH_APK="$SCRIPT_DIR/app/build/outputs/apk/legacy/debug/app-legacy-debug.apk"
fi
if [ ! -f "$HEADUNIT_APK" ]; then
    HEADUNIT_APK="$SCRIPT_DIR/open-headunit/app/build/outputs/apk/github/debug/com.andrerinas.headunitrevived_4.0.0-beta1_debug.apk"
fi

echo "=== Instalator pakietu Mazdash Suite ==="

# Wykrywanie urządzeń ADB
DEVICES=($(adb devices | grep -v "List of devices" | awk '{print $1}' | tr -d '\r' | grep -v '^$'))
DEVICE_COUNT=${#DEVICES[@]}

TARGET_DEVICE="${1:-}"

if [ -z "$TARGET_DEVICE" ]; then
    if [ "$DEVICE_COUNT" -eq 0 ]; then
        echo "BŁĄD: Nie wykryto żadnego urządzenia w ADB."
        echo "Podłącz telefon kablem USB i upewnij się, że włączone jest 'Debugowanie USB'."
        exit 1
    elif [ "$DEVICE_COUNT" -eq 1 ]; then
        TARGET_DEVICE="${DEVICES[0]}"
        echo "Wykryto 1 urządzenie: $TARGET_DEVICE"
    else
        # Jeśli podłączono telefon przez USB i radio przez Wi-Fi, preferuj urządzenie USB
        USB_DEVICE=""
        for d in "${DEVICES[@]}"; do
            if [[ "$d" != *":5555"* ]]; then
                USB_DEVICE="$d"
                break
            fi
        done
        if [ -n "$USB_DEVICE" ]; then
            TARGET_DEVICE="$USB_DEVICE"
            echo "Wykryto wiele urządzeń. Automatycznie wybrano telefon USB: $TARGET_DEVICE"
        else
            TARGET_DEVICE="${DEVICES[0]}"
            echo "Wybrano urządzenie: $TARGET_DEVICE"
        fi
    fi
fi

ADB_CMD="adb -s $TARGET_DEVICE"

echo "Cel instalacji: $TARGET_DEVICE"

install_apk_reliable() {
    local apk_path="$1"
    local pkg_name="$2"
    local apk_name="$(basename "$apk_path")"
    local remote_temp="/data/local/tmp/$apk_name"

    echo "Kopiowanie $apk_name na urządzenie..."
    $ADB_CMD push "$apk_path" "$remote_temp"
    $ADB_CMD shell chmod 644 "$remote_temp" 2>/dev/null || true

    echo "Instalacja $apk_name przez Package Manager..."
    local out
    out=$($ADB_CMD shell pm install -r -t -d -g "$remote_temp" 2>&1)
    echo "$out"

    if [[ "$out" == *"INSTALL_FAILED_UPDATE_INCOMPATIBLE"* ]]; then
        echo "Wykryto niezgodny podpis poprzedniej instalacji ($out)."
        echo "Automatyczne odinstalowanie starej wersji: $pkg_name..."
        $ADB_CMD shell pm uninstall "$pkg_name" 2>/dev/null || true
        echo "Ponowna instalacja nowej wersji..."
        $ADB_CMD shell pm install -r -t -d -g "$remote_temp"
    fi
    
    $ADB_CMD shell rm -f "$remote_temp"
}

echo ""
echo "1/2 Instalacja launchera Mazdash..."
install_apk_reliable "$MAZDASH_APK" "com.dashline.launcher"

echo ""
echo "2/2 Instalacja odbiornika Open Headunit (Mazda Edition)..."
install_apk_reliable "$HEADUNIT_APK" "com.andrerinas.headunitrevived"

echo ""
echo "Nadawanie uprawnień systemowych dla Open Headunit..."
$ADB_CMD shell pm grant com.andrerinas.headunitrevived android.permission.RECORD_AUDIO 2>/dev/null || true
$ADB_CMD shell pm grant com.andrerinas.headunitrevived android.permission.ACCESS_FINE_LOCATION 2>/dev/null || true
$ADB_CMD shell pm grant com.andrerinas.headunitrevived android.permission.READ_PHONE_STATE 2>/dev/null || true

echo ""
echo "=== Sukces! Pakiet Mazdash i Open Headunit zainstalowane pomyślnie na $TARGET_DEVICE ==="
