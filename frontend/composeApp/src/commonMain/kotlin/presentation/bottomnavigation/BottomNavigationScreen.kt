package presentation.bottomnavigation

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import presentation.home.HomeScreen
import presentation.profile.ProfileScreen

@Composable
fun BottomNavigationScreen(component: BottomNavigationComponent, modifier: Modifier = Modifier) {

    NavigationBar {
        Children(
            stack = component.childStack,
            animation = stackAnimation(fade())
        ) {
            when (val child = it.instance) {
                is BottomNavigationComponent.Child.Home -> HomeScreen(child.component)
                is BottomNavigationComponent.Child.Profile -> ProfileScreen(child.component)
            }
        }
    }

}