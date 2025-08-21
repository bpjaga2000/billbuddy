import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.initKoin
import di.koin
import platform.UIKit.UIViewController
import presentation.navigation.DefaultRootComponent
import presentation.navigation.RootComponent
import presentation.navigation.RootContent

fun MainViewController(): UIViewController {
    val rootComponent by koin.inject<RootComponent.Factory>()
    val root = rootComponent.invoke(DefaultComponentContext(LifecycleRegistry()))
    return ComposeUIViewController {
            RootContent(root)
    }
}