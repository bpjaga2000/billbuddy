package utils

import app.cash.sqldelight.db.SqlDriver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

expect fun getSqlDriver(): SqlDriver?

object DbUtils {
    val settings: Settings = createSettings()

    fun check() {
        settings["token"] = "1"
    }
}