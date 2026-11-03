// Copyright (C) 2026 Metehan Kaygısız
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.app.Application
import android.content.Context

/** Applies the saved language + theme as early as possible, before any activity. */
class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.applyTo(base))
    }

    override fun onCreate() {
        super.onCreate()
        val prefs = Prefs(this)
        ThemeManager.apply(prefs)
        MazdaBluetoothController.getInstance(this).init()
        MazdaAudioDspController.getInstance(this).init()
        MazdaOverlayService.start(this)
        if (prefs.adbWifiEnabled) {
            AdbController.setWifiAdb(true)
        }
    }
}
