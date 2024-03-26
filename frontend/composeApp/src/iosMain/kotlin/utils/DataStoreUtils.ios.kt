package utils

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings

actual fun createSettings(): Settings = NSUserDefaultsSettings(platform.Foundation.NSUserDefaults())
