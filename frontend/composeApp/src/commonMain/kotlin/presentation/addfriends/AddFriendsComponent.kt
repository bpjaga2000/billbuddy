package presentation.addfriends

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.GroupMember

interface AddFriendsComponent {
    var addFriendsString: MutableState<String>
    var addFriendsResults: MutableState<List<GroupMember>>
    fun onAddFriendsClicked(text: String)
    fun onAddFriendsResultTapped(userId: String)
    fun onDoneClicked()
}

class DefaultAddFriendsComponent(
    private val componentContext: ComponentContext,
    private val onDone: () -> Unit,
) : AddFriendsComponent, ComponentContext by componentContext {
    override var addFriendsString = mutableStateOf("")
    override var addFriendsResults = mutableStateOf(
        listOf<GroupMember>(
            GroupMember("1234", "abcd"),
            GroupMember("124", "abcd")
        )
    )

    override fun onAddFriendsClicked(text: String) {

    }

    override fun onAddFriendsResultTapped(userId: String) {

    }

    override fun onDoneClicked() {
        onDone()
    }
}