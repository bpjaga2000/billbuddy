package presentation.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.GroupMember

interface SearchComponent {
    var searchString: MutableState<String>
    var searchResults: MutableState<List<GroupMember>>
    fun onSearchClicked(text: String)
    fun onSearchResultTapped(userId: String)
    fun onAddMoreClicked()
    fun onDoneClicked()
}

class DefaultSearchComponent(
    private val componentContext: ComponentContext,
    private val onAddMoreClick: () -> Unit,
    private val onDone: () -> Unit,
) : SearchComponent, ComponentContext by componentContext {
    override var searchString = mutableStateOf("")
    override var searchResults = mutableStateOf(
        listOf<GroupMember>(
            GroupMember("1234", "abcd"),
            GroupMember("124", "abcd")
        )
    )

    override fun onSearchClicked(text: String) {

    }

    override fun onSearchResultTapped(userId: String) {

    }

    override fun onAddMoreClicked() {
        onAddMoreClick()
    }

    override fun onDoneClicked() {
        onDone()
    }

}