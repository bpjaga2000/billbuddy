import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import presentation.navigation.DefaultRootComponent
import presentation.navigation.RootContent

fun main() = application {
    val root = DefaultRootComponent(DefaultComponentContext(LifecycleRegistry()))
    Window(onCloseRequest = ::exitApplication, title = "KotlinProject") {
        RootContent(root)
    }
}