package presentation.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.dto.ProfileDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DispatcherUtils.componentCoroutineScope

interface SearchComponent {
    var searchString: MutableState<String>
    var searchResults: MutableState<List<ProfileDto>>
    fun onSearchClicked(text: String)
    fun onSearchResultTapped(userId: String, isChecked: Boolean)
    fun onAddMoreClicked()
    fun onDoneClicked()
}

class DefaultSearchComponent(
    private val componentContext: ComponentContext,
    private val existingMemberIds: String,
    private val groupId: String,
    private val onAddMoreClick: () -> Unit,
    private val onDone: () -> Unit,
) : SearchComponent, ComponentContext by componentContext {
    override var searchString = mutableStateOf("")
    private var addedMembers = mutableStateOf(listOf<ProfileDto>())
    override var searchResults = mutableStateOf(
        listOf<ProfileDto>()
    )

    override fun onSearchClicked(text: String) {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            RepositoryImpl().searchFriends(text).collect {
                searchResults.value = it.filterNot { f -> existingMemberIds.contains(f.id) }
            }
        }
    }

    override fun onSearchResultTapped(userId: String, isChecked: Boolean) {
        searchResults.value.find { it.id == userId }?.let {
            var list = ArrayList(addedMembers.value)
            if (isChecked)
                list.add(it)
            else
                list = ArrayList(list.filter { f -> f.id != userId })
            addedMembers.value = list
        }
    }

    override fun onAddMoreClicked() {
        onAddMoreClick()
    }

    override fun onDoneClicked() {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            RepositoryImpl().addUsers(addedMembers.value).collect { added ->
                if (added)
                    RepositoryImpl().addGroupMembers(addedMembers.value.map { it.id }, groupId)
                        .collect {
                            when (it) {
                                is ApiResult.Success -> {
                                    withContext(Dispatchers.Main) {
                                        onDone()
                                    }
                                }

                                else -> {}
                            }
                        }
            }
        }
    }

}