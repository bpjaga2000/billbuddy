package presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import presentation.login.DefaultLoginComponent
import presentation.login.LogInViewModel
import presentation.login.LoginComponent
import presentation.register.DefaultRegisterComponent
import presentation.register.RegisterComponent

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class LoginChild(val component: LoginComponent) : Child()

        data class RegisterChild(val component: RegisterComponent) : Child()
        class HomeChild : Child()
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Login,
        handleBackButton = true,
        childFactory = ::childFactory
    )

    private fun childFactory(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is Config.Login -> RootComponent.Child.LoginChild(DefaultLoginComponent(
                componentContext.childContext(key = "login"),
                LogInViewModel()
            ) {
                navigation.push(Config.Register)
            })

            is Config.Register -> RootComponent.Child.RegisterChild(
                DefaultRegisterComponent(
                    componentContext.childContext(key = "register")
                ) {
                    navigation.pop()
                })

            is Config.Home -> RootComponent.Child.HomeChild()//todo
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Login : Config()

        @Serializable
        data object Register : Config()

        @Serializable
        data object Home : Config()
    }

}