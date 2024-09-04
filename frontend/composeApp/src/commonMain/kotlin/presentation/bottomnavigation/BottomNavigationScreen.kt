package presentation.bottomnavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import constants.Constants
import presentation.groupspends.GroupSpendsScreen
import presentation.home.HomeScreen
import presentation.profile.ProfileScreen
import presentation.spenddetails.SpendDetailsScreen
import utils.DataStore

@Composable
fun BottomNavigationScreen(component: BottomNavigationComponent, modifier: Modifier = Modifier) {

    val configs = listOf(Constants.HOME, Constants.PROFILE)
    var selectedItem by remember {
        mutableStateOf(
            if (DataStore.settings.getStringOrNull("token").isNullOrEmpty()) 1 else 0
        )
    }

    Scaffold(
        topBar = { TopAppBar(modifier = Modifier.fillMaxWidth(), title = { Text("Home") }) },
        bottomBar = {
            BottomAppBar(modifier = Modifier.background(Color.White)) {
                repeat(configs.size) {
                    BottomNavigationItem(
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
                        selectedContentColor = Color.Blue,
                        unselectedContentColor = Color.Gray
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
            when (val child = it.instance) {
                is BottomNavigationComponent.Child.Home -> HomeScreen(child.component)
                is BottomNavigationComponent.Child.Profile -> ProfileScreen(child.component)
                is BottomNavigationComponent.Child.GroupSpends -> GroupSpendsScreen(child.component)
                is BottomNavigationComponent.Child.SpendDetails -> SpendDetailsScreen(child.component)
            }
        }
    }

}