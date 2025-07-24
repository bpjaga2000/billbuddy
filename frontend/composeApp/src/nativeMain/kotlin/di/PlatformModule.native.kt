package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import dev.bpj4.billbuddy.BillBuddyDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<SqlDriver> { NativeSqliteDriver(BillBuddyDatabase.Schema, "billbuddy.db") }
    }