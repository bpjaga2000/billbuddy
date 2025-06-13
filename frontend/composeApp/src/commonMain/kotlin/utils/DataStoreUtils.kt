package utils

import com.russhwolf.settings.*

expect fun createSettings(): Settings

object DataStore {
    val settings: Settings = createSettings()

}