package utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseFileContext
import co.touchlab.sqliter.databaseFileExists
import dev.bpj4.billbuddy.BillBuddyDatabase

actual fun getSqlDriver(): SqlDriver? =
    NativeSqliteDriver(BillBuddyDatabase.Schema, "billbuddy.db")