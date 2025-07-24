package di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<Settings> { NSUserDefaultsSettings(platform.Foundation.NSUserDefaults()) }
    }