package dev.bpj4.billbuddy

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import presentation.navigation.DefaultRootComponent
import presentation.navigation.RootContent
import utils.makeSettings
import utils.makeSqlDriver

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        makeSettings(applicationContext)
        makeSqlDriver(applicationContext)
        val root = DefaultRootComponent(defaultComponentContext())
        setContent {
            RootContent(root)
        }
    }

    override fun onDestroy() {
        context = null
        super.onDestroy()
    }

    companion object {
        var context: Context? = null
    }
}