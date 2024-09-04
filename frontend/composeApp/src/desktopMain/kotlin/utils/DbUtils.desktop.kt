package utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.Properties

actual fun getSqlDriver(): SqlDriver? = JdbcSqliteDriver(
        url = "...",
        properties = Properties().apply { put("foreign_keys", "true") }
    )