package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import dev.bpj4.billbuddy.BillBuddyDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<Settings> { NSUserDefaultsSettings(platform.Foundation.NSUserDefaults()) }
        single<SqlDriver> { NativeSqliteDriver(BillBuddyDatabase.Schema, "billbuddy.db") }
    }