package presentation.addfriends

import com.arkivanov.decompose.ComponentContext

interface AddFriendsComponent {
}

class DefaultAddFriendsComponent(
    private val componentContext: ComponentContext,
) : AddFriendsComponent, ComponentContext by componentContext