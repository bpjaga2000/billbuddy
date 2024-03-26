package utils

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import dev.bpj4.billbuddy.MainActivity


actual fun createSettings(): Settings =
    SharedPreferencesSettings(
        EncryptedSharedPreferences(MainActivity.context, "billBuddyPrefs", MasterKey(MainActivity.context))
    )
