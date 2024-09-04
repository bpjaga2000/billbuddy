package utils

import app.cash.sqldelight.db.SqlDriver
import dev.bpj4.billbuddy.BillBuddyDatabase

actual fun getSqlDriver(): SqlDriver? =
    NativeSqliteDriver(BillBuddyDatabase.Schema, "billbuddy.db")