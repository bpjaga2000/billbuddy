package di

import android.content.Context.MODE_PRIVATE
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import dev.bpj4.billbuddy.BillBuddyDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {

        single<SqlDriver> {
            AndroidSqliteDriver(
                BillBuddyDatabase.Schema,
                androidContext(),
                "billbuddy.db"
            )
        }

        single<Settings> {
            SharedPreferencesSettings(
                androidContext().getSharedPreferences(
                    "billBuddyPrefs",
                    MODE_PRIVATE
                )
            )
        }

    }