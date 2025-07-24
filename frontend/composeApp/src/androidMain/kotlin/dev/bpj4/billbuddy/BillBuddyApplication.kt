package dev.bpj4.billbuddy

import android.app.Application
import di.initKoin
import org.koin.android.ext.koin.androidContext

class BillBuddyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BillBuddyApplication)
        }
    }
}