package dev.bpj4.billbuddy

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import presentation.navigation.DefaultRootComponent
import presentation.navigation.RootContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        val root = DefaultRootComponent(defaultComponentContext())
        setContent {
            RootContent(root)
        }
    }

    companion object{
        lateinit var context: Context
    }
}