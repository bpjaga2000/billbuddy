package dev.bpj4.billbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import di.initKoin
import di.koin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import presentation.navigation.RootComponent
import presentation.navigation.RootContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootComponent by koin.inject<RootComponent.Factory>()
        val root = rootComponent.invoke(defaultComponentContext())
        setContent {
            RootContent(root)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}