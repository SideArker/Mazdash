// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

/**
 * The bottom bar is built from this list rather than fixed layout entries, so the
 * user can reorder tabs and hide the ones their unit doesn't need.
 *
 * [id] is persisted — never rename one without a migration.
 */
enum class TabAction(
    val id: String,
    val iconRes: Int,
    val labelRes: Int,
    /**
     * Apps and Settings can be reordered but never hidden — without them there's
     * no way to reach the drawer or get back into Settings to undo it.
     */
    val canHide: Boolean = true
) {
    RADIO("radio", R.drawable.ic_radio, R.string.audio_radio),
    PHONE("phone", R.drawable.ic_phone, R.string.tab_phone),
    AUDIO("audio", R.drawable.ic_audio, R.string.tab_audio),
    BLUETOOTH("bluetooth", R.drawable.ic_bluetooth, R.string.tab_bluetooth),
    APPS("apps", R.drawable.ic_apps, R.string.tab_apps, canHide = false),
    SETTINGS("settings", R.drawable.ic_settings, R.string.tab_settings, canHide = false);

    companion object {
        fun byId(id: String): TabAction? = entries.firstOrNull { it.id == id }

        /** Default order, matching Mazda unit needs (no GPS nav). */
        val DEFAULT_ORDER: List<TabAction> = entries.toList()
    }
}
