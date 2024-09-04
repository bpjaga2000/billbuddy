package utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import dev.bpj4.billbuddy.MainActivity


lateinit var settings: Settings

actual fun createSettings(): Settings = settings

fun makeSettings(context: Context) {
    settings = SharedPreferencesSettings(
        EncryptedSharedPreferences(
            context,
            "billBuddyPrefs",
            MasterKey(context)
        )
    )
}