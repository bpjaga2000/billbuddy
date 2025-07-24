package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import dev.bpj4.billbuddy.BillBuddyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.module.Module
import org.koin.dsl.module
import org.w3c.dom.Worker

actual val platformModule: Module
    get() = module {
        single<Settings> { StorageSettings() }
        single<SqlDriver> {
            WebWorkerDriver(
                Worker("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
            ).also {
                CoroutineScope(Dispatchers.Default).launch {
                    BillBuddyDatabase.Schema.create(it).await()
                }
            }
        }
    }