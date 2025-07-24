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
import data.Repository
import dev.bpj4.billbuddy.tableandmigrations.Users
import di.koin
import kotlinx.serialization.Serializable
import presentation.addfriends.AddFriendsComponent
import presentation.balances.BalancesComponent
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
import presentation.editspends.EditSpendsComponent
import presentation.groupsettings.GroupSettingsComponent
import presentation.groupspends.GroupSpendsComponent
import presentation.home.HomeComponent
import presentation.profile.ProfileComponent
import presentation.search.SearchComponent
import presentation.settleup.SettleUpComponent
import presentation.spenddetails.SpendDetailsComponent
import presentation.totals.DefaultTotalsComponent
import presentation.totals.TotalsComponent

interface BottomNavigationComponent {
    val childStack: Value<ChildStack<*, Child>>

    fun onHomeClicked()
    val currentUser: MutableState<Users?>
    fun onProfileClicked()

    fun onBackClicked()

    fun getToken(): String?

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

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onLogout: () -> Unit
        ): BottomNavigationComponent
    }
}

class DefaultBottomNavigationComponent(
    private val componentContext: ComponentContext,
    private val onLogout: () -> Unit,
    val repository: Repository
) : BottomNavigationComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    override val childStack: Value<ChildStack<*, BottomNavigationComponent.Child>> = childStack(
        source = navigation,
        key = "bottom_navigation",
        serializer = Config.serializer(),
        initialConfiguration = if (repository.getSettings().getStringOrNull("token")
                .isNullOrEmpty()
        )
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

    override fun getToken() = repository.getSettings().getStringOrNull("token")

    private fun childFactory(
        config: Config,
        componentContext: ComponentContext,
    ): BottomNavigationComponent.Child = when (config) {
        Config.Home -> Home(
            (koin.inject<HomeComponent.Factory>()).value.invoke(
                componentContext = componentContext.childContext(key = "home"),
                { navigation.push(Config.GroupSpends(it)) },
                {
                    navigation.push(Config.CreateGroup)
                },
                { onLogout() },
                { payerId, payeeId ->
                    navigation.push(
                        Config.SettleUp(
                            null,
                            payerId,
                            payeeId
                        )
                    )
                }
            )
        )

        Config.Profile -> Profile(
            (koin.inject<ProfileComponent.Factory>()).value.invoke(
                componentContext = componentContext.childContext(key = "profile")
            ) {
                onLogout()
            }
        )

        is Config.GroupSpends -> GroupSpends(
            (koin.inject<GroupSpendsComponent.Factory>()).value.invoke(
                componentContext.childContext(key = "groupSpends"),
                config.groupId,
                { navigation.push(Config.SpendDetails(config.groupId, it)) },
                { navigation.push(Config.EditSpends(config.groupId)) },
                { navigation.push(Config.GroupSettings(it)) },
                { navigation.push(Config.Balances(config.groupId)) },
            )
        )

        is Config.GroupSettings -> GroupSettings(
            (koin.inject<GroupSettingsComponent.Factory>()).value.invoke(
                componentContext.childContext(key = "groupSettings"),
                config.groupId,
                { memberIds, groupId -> navigation.push(Config.Search(memberIds, groupId)) }
            ) { navigation.pop() }
        )

        is Config.EditSpends -> EditSpends(
            (koin.inject<EditSpendsComponent.Factory>()).value.invoke(
                componentContext = componentContext.childContext(key = "editSpends"),
                config.groupId,
                config.spendId,
                selection = mutableStateOf(0),
                onSaved = { navigation.pop() }
            )
        )

        is Config.SpendDetails -> SpendDetails(
            (koin.inject<SpendDetailsComponent.Factory>()).value.invoke(
                componentContext.childContext(key = "spendDetails"),
                config.spendId,
                { navigation.pop() }
            ) { navigation.push(Config.EditSpends(config.groupId, config.spendId)) }
        )

        is Config.Balances -> Balances(
            (koin.inject<BalancesComponent.Factory>()).value.invoke(
                componentContext.childContext("balances"),
                config.groupId
            ) { groupId, payerId, payeeId ->
                navigation.push(
                    Config.SettleUp(
                        config.groupId,
                        payerId,
                        payeeId
                    )
                )
            }
        )

        Config.Totals -> Totals(
            DefaultTotalsComponent(
                componentContext.childContext("totals"),
                ""
            )
        )

        is Config.SettleUp -> SettleUp(
            (koin.inject<SettleUpComponent.Factory>()).value.invoke(
                componentContext.childContext("settleUp"),
                config.groupId,
                config.payerId,
                config.payeeId
            ) {
                navigation.pop()
            }
        )

        is Config.Search -> Search(
            (koin.inject<SearchComponent.Factory>()).value.invoke(
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
            (koin.inject<AddFriendsComponent.Factory>()).value.invoke(
                componentContext.childContext("addFriends"),
                config.existingMemberIds,
                config.groupId
            ) {
                navigation.pop()
            }
        )

        Config.CreateGroup -> CreateGroup(
            (koin.inject<CreateGroupComponent.Factory>()).value.invoke(
                componentContext.childContext("createGroup")
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
        data class GroupSpends(val groupId: String) : Config()

        @Serializable
        data class GroupSettings(val groupId: String) : Config()

        @Serializable
        data class EditSpends(val groupId: String, val spendId: String? = null) : Config()

        @Serializable
        data class SpendDetails(val groupId: String, val spendId: String) : Config()

        @Serializable
        data object Totals : Config()

        @Serializable
        data class Balances(val groupId: String) : Config()

        @Serializable
        data class SettleUp(val groupId: String?, val payerId: String, val payeeId: String) :
            Config()

        @Serializable
        data class Search(val existingMemberIds: String, val groupId: String) : Config()

        @Serializable
        data class AddFriends(val existingMemberIds: String, val groupId: String) : Config()

        @Serializable
        data object CreateGroup : Config()
    }

    class Factory(val repository: Repository) :
        BottomNavigationComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onLogout: () -> Unit
        ): BottomNavigationComponent {
            return DefaultBottomNavigationComponent(componentContext, onLogout, repository)
        }
    }

}