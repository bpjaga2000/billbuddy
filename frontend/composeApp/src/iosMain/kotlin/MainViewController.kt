import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.koin
import presentation.navigation.DefaultRootComponent
import presentation.navigation.RootComponent
import presentation.navigation.RootContent

fun MainViewController() {
    val rootComponentFactory: RootComponent.Factory by koin.inject()
    val root = rootComponentFactory.invoke(DefaultComponentContext(LifecycleRegistry()))
    ComposeUIViewController {
        RootContent(root)
    }
}