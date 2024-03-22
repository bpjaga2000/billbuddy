package presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import presentation.login.LoginScreen
import presentation.register.RegisterScreen

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {

    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.LoginChild -> LoginScreen(child.component)
            is RootComponent.Child.HomeChild -> TODO()
            is RootComponent.Child.RegisterChild -> RegisterScreen(child.component)
        }
    }
}