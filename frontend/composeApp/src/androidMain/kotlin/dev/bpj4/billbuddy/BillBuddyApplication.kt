package dev.bpj4.billbuddy

import android.app.Application
import di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class BillBuddyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BillBuddyApplication)
            androidLogger(Level.INFO)
        }
        multiplatform.network.cmptoast.AppContext.apply { set(applicationContext) }
    }
}