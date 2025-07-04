package utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.bpj4.billbuddy.BillBuddyDatabase
import java.util.Properties

actual fun getSqlDriver(): SqlDriver? = JdbcSqliteDriver(
    url = "jdbc:sqlite:billbuddy.db",
    properties = Properties().apply { put("foreign_keys", "true") },
    BillBuddyDatabase.Schema
)