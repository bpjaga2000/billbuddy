package utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import dev.bpj4.billbuddy.BillBuddyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Worker

actual fun getSqlDriver(): SqlDriver? = WebWorkerDriver(
    Worker("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
).also { CoroutineScope(Dispatchers.Default).launch { BillBuddyDatabase.Schema.create(it).await() } }