package presentation.bottomnavigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import dev.bpj4.billbuddy.tableandmigrations.Users
import kotlinx.serialization.Serializable
import presentation.groupspends.DefaultGroupSpendsComponent
import presentation.groupspends.GroupSpendsComponent
import presentation.home.DefaultHomeComponent
import presentation.home.HomeComponent
import presentation.profile.DefaultProfileComponent
import presentation.profile.ProfileComponent
import presentation.spenddetails.DefaultSpendDetailsComponent
import presentation.spenddetails.SpendDetailsComponent
import utils.DataStore

interface BottomNavigationComponent {
    val childStack: Value<ChildStack<*, Child>>

    fun onHomeClicked()
    val currentUser: MutableState<Users?>
    fun onProfileClicked()

    sealed class Child {
        class Home(val component: HomeComponent) : Child()
        class Profile(val component: ProfileComponent) : Child()
        class GroupSpends(val component: GroupSpendsComponent) : Child()
        class SpendDetails(val component: SpendDetailsComponent) : Child()
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
        initialConfiguration = if (DataStore.settings.getStringOrNull("token").isNullOrEmpty())
            Config.Profile
        else
            Config.Home,
        handleBackButton = true,
        childFactory = ::childFactory
    )

    private val _currentUser: MutableState<Users?> = mutableStateOf(null)
    override val currentUser get() = _currentUser

    override fun onHomeClicked() {
        navigation.bringToFront(Config.Home)
    }

    override fun onProfileClicked() {
        navigation.bringToFront(Config.Profile)
    }

    private fun childFactory(
        config: Config,
        componentContext: ComponentContext
    ): BottomNavigationComponent.Child = when (config) {
        Config.Home -> BottomNavigationComponent.Child.Home(
            DefaultHomeComponent(
                componentContext = componentContext.childContext(key = "home")
            ) { navigation.push(Config.GroupSpends) }
        )

        Config.Profile -> BottomNavigationComponent.Child.Profile(
            DefaultProfileComponent(
                componentContext = componentContext.childContext(key = "profile")
            )
        )

        Config.GroupSpends -> BottomNavigationComponent.Child.GroupSpends(
            DefaultGroupSpendsComponent(
                componentContext.childContext(key = "groupSpends")
            ) { navigation.push(Config.SpendDetails) }
        )

        Config.SpendDetails -> BottomNavigationComponent.Child.SpendDetails(
            DefaultSpendDetailsComponent(
                componentContext.childContext(key = "spendDetails")
            ) { navigation.pop() }
        )
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Home : Config()

        @Serializable
        data object Profile : Config()

        @Serializable
        data object GroupSpends : Config()

        @Serializable
        data object SpendDetails : Config()
    }

}