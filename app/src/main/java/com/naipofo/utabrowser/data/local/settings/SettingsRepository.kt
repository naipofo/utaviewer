package com.naipofo.utabrowser.data.local.settings

import android.content.SharedPreferences

class SettingsRepository(
    private val sharedPreferences: SharedPreferences
) {
    var sizeMultiplier: Float
        get() = sharedPreferences.getFloat("sizeMultiplier", 1f)
        set(value) = sharedPreferences.edit().putFloat("sizeMultiplier", value).apply()

    var showRuby: Boolean
        get() = sharedPreferences.getBoolean("showRuby", true)
        set(value) = sharedPreferences.edit().putBoolean("showRuby", value).apply()
}