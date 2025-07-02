package presentation.bottomnavigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import data.model.Balance
import dev.bpj4.billbuddy.tableandmigrations.Users
import kotlinx.serialization.Serializable
import presentation.addfriends.AddFriendsComponent
import presentation.addfriends.DefaultAddFriendsComponent
import presentation.balances.BalancesComponent
import presentation.balances.DefaultBalancesComponent
import presentation.bottomnavigation.BottomNavigationComponent.Child.AddFriends
import presentation.bottomnavigation.BottomNavigationComponent.Child.Balances
import presentation.bottomnavigation.BottomNavigationComponent.Child.CreateGroup
import presentation.bottomnavigation.BottomNavigationComponent.Child.EditSpends
import presentation.bottomnavigation.BottomNavigationComponent.Child.GroupSettings
import presentation.bottomnavigation.BottomNavigationComponent.Child.GroupSpends
import presentation.bottomnavigation.BottomNavigationComponent.Child.Home
import presentation.bottomnavigation.BottomNavigationComponent.Child.Profile
import presentation.bottomnavigation.BottomNavigationComponent.Child.Search
import presentation.bottomnavigation.BottomNavigationComponent.Child.SettleUp
import presentation.bottomnavigation.BottomNavigationComponent.Child.SpendDetails
import presentation.bottomnavigation.BottomNavigationComponent.Child.Totals
import presentation.creategroup.CreateGroupComponent
import presentation.creategroup.DefaultCreateGroupComponent
import presentation.editspends.DefaultEditSpendsComponent
import presentation.editspends.EditSpendsComponent
import presentation.groupsettings.DefaultGroupSettingsComponent
import presentation.groupsettings.GroupSettingsComponent
import presentation.groupspends.DefaultGroupSpendsComponent
import presentation.groupspends.GroupSpendsComponent
import presentation.home.DefaultHomeComponent
import presentation.home.HomeComponent
import presentation.profile.DefaultProfileComponent
import presentation.profile.ProfileComponent
import presentation.search.DefaultSearchComponent
import presentation.search.SearchComponent
import presentation.settleup.DefaultSettleUpComponent
import presentation.settleup.SettleUpComponent
import presentation.spenddetails.DefaultSpendDetailsComponent
import presentation.spenddetails.SpendDetailsComponent
import presentation.totals.DefaultTotalsComponent
import presentation.totals.TotalsComponent
import utils.DataStore

interface BottomNavigationComponent {
    val childStack: Value<ChildStack<*, Child>>

    fun onHomeClicked()
    val currentUser: MutableState<Users?>
    fun onProfileClicked()

    fun onBackClicked()

    sealed class Child {
        class Home(val component: HomeComponent) : Child()
        class Profile(val component: ProfileComponent) : Child()
        class GroupSpends(val component: GroupSpendsComponent) : Child()
        class GroupSettings(val component: GroupSettingsComponent) : Child()
        class EditSpends(val component: EditSpendsComponent) : Child()
        class SpendDetails(val component: SpendDetailsComponent) : Child()
        class Totals(val component: TotalsComponent) : Child()
        class Balances(val component: BalancesComponent) : Child()
        class SettleUp(val component: SettleUpComponent) : Child()
        class Search(val component: SearchComponent) : Child()
        class AddFriends(val component: AddFriendsComponent) : Child()
        class CreateGroup(val component: CreateGroupComponent) : Child()
    }
}

class DefaultBottomNavigationComponent(
    private val componentContext: ComponentContext,
    private val onLogout: () -> Unit
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
        navigation.replaceAll(Config.Home)
    }

    override fun onProfileClicked() {
        navigation.replaceAll(Config.Profile)
    }

    override fun onBackClicked() {
        navigation.pop()
    }

    private fun childFactory(
        config: Config,
        componentContext: ComponentContext,
    ): BottomNavigationComponent.Child = when (config) {
        Config.Home -> Home(
            DefaultHomeComponent(
                componentContext = componentContext.childContext(key = "home"),
                { navigation.push(Config.GroupSpends(it)) },
                {
                    navigation.push(Config.CreateGroup)
                }) {
                onLogout()
            }
        )

        Config.Profile -> Profile(
            DefaultProfileComponent(
                componentContext = componentContext.childContext(key = "profile")
            ) {
                onLogout()
            }
        )

        is Config.GroupSpends -> GroupSpends(
            DefaultGroupSpendsComponent(
                componentContext.childContext(key = "groupSpends"),
                config.groupId,
                { navigation.push(Config.SpendDetails(it)) },
                { navigation.push(Config.EditSpends) }
            ) { navigation.push(Config.GroupSettings(it)) }
        )

        is Config.GroupSettings -> GroupSettings(
            DefaultGroupSettingsComponent(
                componentContext.childContext(key = "groupSettings"),
                config.groupId,
                { memberIds, groupId -> navigation.push(Config.Search(memberIds, groupId)) }
            ) { navigation.pop() }
        )

        Config.EditSpends -> EditSpends(
            DefaultEditSpendsComponent(
                componentContext = componentContext.childContext(key = "editSpends"),
                onSaved = { navigation.pop() }
            )
        )

        is Config.SpendDetails -> SpendDetails(
            DefaultSpendDetailsComponent(
                componentContext.childContext(key = "spendDetails"),
                { navigation.pop() }
            ) { navigation.push(Config.EditSpends) }
        )

        Config.Balances -> Balances(
            DefaultBalancesComponent(
                componentContext.childContext("balances"),
                ""
            )
        )

        Config.Totals -> Totals(
            DefaultTotalsComponent(
                componentContext.childContext("totals"),
                ""
            )
        )

        Config.SettleUp -> SettleUp(
            DefaultSettleUpComponent(
                componentContext.childContext("settleUp"),
                "",
                Balance("", "", "", ""),
                Balance("", "", "", "")
            ) {
                navigation.pop()
            }
        )

        is Config.Search -> Search(
            DefaultSearchComponent(
                componentContext.childContext("search"),
                config.existingMemberIds,
                config.groupId,
                {
                    navigation.push(Config.AddFriends(config.existingMemberIds, config.groupId))
                }
            ) {
                navigation.pop()

            }
        )

        is Config.AddFriends -> AddFriends(
            DefaultAddFriendsComponent(
                componentContext.childContext("addFriends"),
                config.existingMemberIds,
                config.groupId
            ) {
                navigation.pop()
            }
        )

        Config.CreateGroup -> CreateGroup(
            DefaultCreateGroupComponent(
                componentContext.childContext("createGroup")
            )
        )

    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Home : Config()

        @Serializable
        data object Profile : Config()

        @Serializable
        data class GroupSpends(val groupId: String) : Config()

        @Serializable
        data class GroupSettings(val groupId: String) : Config()

        @Serializable
        data object EditSpends : Config()

        @Serializable
        data class SpendDetails(val spendId: String) : Config()

        @Serializable
        data object Totals : Config()

        @Serializable
        data object Balances : Config()

        @Serializable
        data object SettleUp : Config()

        @Serializable
        data class Search(val existingMemberIds: String, val groupId: String) : Config()

        @Serializable
        data class AddFriends(val existingMemberIds: String, val groupId: String) : Config()

        @Serializable
        data object CreateGroup : Config()
    }

}