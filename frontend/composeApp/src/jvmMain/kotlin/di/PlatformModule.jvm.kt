package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import dev.bpj4.billbuddy.BillBuddyDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.Properties
import java.util.prefs.Preferences

actual val platformModule: Module
    get() = module {
        single<Settings> { PreferencesSettings(Preferences.userRoot()) }
        single<SqlDriver> {
            JdbcSqliteDriver(
                url = "jdbc:sqlite:billbuddy.db",
                properties = Properties().apply { put("foreign_keys", "true") },
                BillBuddyDatabase.Schema
            )
        }
    }