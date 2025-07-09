package utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings


lateinit var settings: Settings

actual fun createSettings(): Settings = settings

fun makeSettings(context: Context) {
    settings = SharedPreferencesSettings(
        context.getSharedPreferences(
            "billBuddyPrefs",
            MODE_PRIVATE
        )
    )
}