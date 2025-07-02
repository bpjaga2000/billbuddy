package presentation.bottomnavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import constants.Constants
import presentation.addfriends.AddFriendsScreen
import presentation.balances.BalancesScreen
import presentation.creategroup.CreateGroupScreen
import presentation.editspends.EditSpendsScreen
import presentation.groupsettings.GroupSettingsScreen
import presentation.groupspends.GroupSpendsScreen
import presentation.home.HomeScreen
import presentation.profile.ProfileScreen
import presentation.search.SearchScreen
import presentation.settleup.SettleUpScreen
import presentation.spenddetails.SpendDetailsScreen
import presentation.totals.TotalsScreen
import utils.DataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationScreen(component: BottomNavigationComponent, modifier: Modifier = Modifier) {

    val configs = listOf(Constants.HOME, Constants.PROFILE)
    var selectedItem by remember {
        mutableStateOf(
            if (DataStore.settings.getStringOrNull("token").isNullOrEmpty()) 1 else 0
        )
    }
    var title by mutableStateOf("")
    val childStack = mutableStateOf(component.childStack.value.items.last())

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(title) },
                navigationIcon =
                    @Composable {
                        if (childStack.value.instance !is BottomNavigationComponent.Child.Home && childStack.value.instance !is BottomNavigationComponent.Child.Profile) {
                            IconButton(
                                onClick = component::onBackClicked,
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = "back",
                                )
                            }
                        } else null
                    },
                actions = {
                    when (val instance = childStack.value.instance) {

                        is BottomNavigationComponent.Child.GroupSpends -> {
                            IconButton(onClick = { instance.component.onGroupSpendSettingsClicked() }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "settings"
                                )
                            }
                        }

                        is BottomNavigationComponent.Child.EditSpends -> {
                            IconButton(onClick = { instance.component.onSaveClicked() }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "save"
                                )
                            }
                        }

                        is BottomNavigationComponent.Child.GroupSettings -> {
                            IconButton(onClick = { instance.component.onSaveClicked() }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "save"
                                )
                            }
                        }

                        is BottomNavigationComponent.Child.Search -> {
                            IconButton(onClick = { instance.component.onDoneClicked() }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "done"
                                )
                            }
                        }

                        is BottomNavigationComponent.Child.AddFriends -> {
                            IconButton(onClick = { instance.component.onDoneClicked() }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "done"
                                )
                            }
                        }

                        is BottomNavigationComponent.Child.Home -> {
                            IconButton(onClick = { instance.component.onLogoutClick() }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Outlined.Logout,
                                    contentDescription = "done"
                                )
                            }
                        }

                        is BottomNavigationComponent.Child.Profile -> {
                            IconButton(onClick = { instance.component.onLogoutClick() }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Outlined.Logout,
                                    contentDescription = "done"
                                )
                            }
                        }

                        else -> {}

                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.background(Color.White)) {
                repeat(configs.size) {
                    NavigationBarItem(
                        selected = selectedItem == it,
                        icon = @Composable {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = if (Constants.HOME == configs[it]) Icons.Filled.Home else Icons.Filled.AccountCircle,
                                    contentDescription = null
                                )
                                Text(text = configs[it])
                            }
                        },
                        onClick = {
                            when (configs[it]) {
                                Constants.HOME -> component.onHomeClicked()

                                Constants.PROFILE -> component.onProfileClicked()
                            }
                            selectedItem = it
                        },
                        colors = NavigationBarItemColors(
                            Color.Blue, Color.Blue, Color.Cyan,
                            Color.Gray, Color.Gray,
                            Color.Black, Color.Black
                        )
                    )
                }
            }
        }
    ) { padding ->
        Children(
            modifier = Modifier.padding(padding).fillMaxSize(),
            stack = component.childStack,
            animation = stackAnimation(fade()),
        ) {
            childStack.value = component.childStack.value.items.last()
            when (val child = it.instance) {
                is BottomNavigationComponent.Child.Home -> {
                    title = "Home"
                    HomeScreen(child.component)
                }

                is BottomNavigationComponent.Child.Profile -> {
                    title = "Profile"
                    ProfileScreen(child.component)
                }

                is BottomNavigationComponent.Child.GroupSpends -> {
                    title = "GroupSpends"
                    GroupSpendsScreen(child.component)
                }

                is BottomNavigationComponent.Child.GroupSettings -> {
                    title = "GroupSettings"
                    GroupSettingsScreen(child.component)
                }

                is BottomNavigationComponent.Child.EditSpends -> {
                    title = "EditSpends"
                    EditSpendsScreen(child.component)
                }

                is BottomNavigationComponent.Child.SpendDetails -> {
                    title = "SpendDetails"
                    SpendDetailsScreen(child.component)
                }

                is BottomNavigationComponent.Child.Balances -> {
                    title = "Balances"
                    BalancesScreen(child.component)
                }

                is BottomNavigationComponent.Child.SettleUp -> {
                    title = "Settle Up"
                    SettleUpScreen(child.component)
                }

                is BottomNavigationComponent.Child.Totals -> {
                    title = "Totals"
                    TotalsScreen(child.component)
                }

                is BottomNavigationComponent.Child.Search -> {
                    title = "Search"
                    SearchScreen(child.component)
                }

                is BottomNavigationComponent.Child.AddFriends -> {
                    title = "Add Friends"
                    AddFriendsScreen(child.component)
                }

                is BottomNavigationComponent.Child.CreateGroup -> {
                    title = "Create Group"
                    CreateGroupScreen(child.component)
                }
            }
        }
    }

}