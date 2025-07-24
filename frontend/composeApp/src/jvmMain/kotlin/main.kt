import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.koin
import presentation.navigation.DefaultRootComponent
import presentation.navigation.RootComponent
import presentation.navigation.RootContent

fun main() = application {
    val lifecycle = LifecycleRegistry()
    val windowState = rememberWindowState()
    val rootComponentFactory: RootComponent.Factory by koin.inject()
    val root = rootComponentFactory.invoke(DefaultComponentContext(lifecycle))

    Window(onCloseRequest = ::exitApplication, state = windowState, title = "Billbuddy") {

        LifecycleController(
            lifecycleRegistry = lifecycle,
            windowState = windowState,
            windowInfo = LocalWindowInfo.current,
        )

        RootContent(root)
    }
}