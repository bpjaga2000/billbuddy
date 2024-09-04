package utils

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.bpj4.billbuddy.BillBuddyDatabase

lateinit var sqlDriverObject: SqlDriver

actual fun getSqlDriver(): SqlDriver? = sqlDriverObject

fun makeSqlDriver(context: Context) {
    sqlDriverObject = AndroidSqliteDriver(BillBuddyDatabase.Schema, context, "test.db")
}

