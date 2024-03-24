package presentation.bottomnavigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

interface BottomNavigationComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Home(val componentContext: ComponentContext) : Child()
        class Profile : Child()
    }
}

class DefaultBottomNavigationComponent(
    private val componentContext: ComponentContext
) : BottomNavigationComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    override val childStack: Value<ChildStack<*, BottomNavigationComponent.Child>> = childStack(
        source = navigation,
        key = "bottom_navigation",
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::childFactory
    )

    private fun childFactory(
        config: Config,
        componentContext: ComponentContext
    ): BottomNavigationComponent.Child = when(config) {
        Config.Home -> BottomNavigationComponent.Child.Home(componentContext)
        Config.Profile -> BottomNavigationComponent.Child.Profile()
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Home : Config()

        @Serializable
        data object Profile : Config()
    }

}