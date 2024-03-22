import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import presentation.navigation.DefaultRootComponent
import presentation.navigation.RootContent

fun MainViewController() {
    val root = DefaultRootComponent(DefaultComponentContext(LifecycleRegistry()))
    ComposeUIViewController {
        RootContent(root)
    }
}