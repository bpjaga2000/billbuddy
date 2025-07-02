package presentation.addfriends

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.dto.ProfileDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DispatcherUtils.componentCoroutineScope

interface AddFriendsComponent {
    var searchTag: MutableState<String>

    //    var addedFriends: MutableState<List<String>>
    var addFriendsResults: MutableState<List<ProfileDto>>
    fun onAddFriendsClicked(text: String)
    fun onAddFriendsResultTapped(userId: String, isChecked: Boolean)
    fun onDoneClicked()
}

class DefaultAddFriendsComponent(
    private val componentContext: ComponentContext,
    private val existingFriendIds: String,
    private val groupId: String,
    private val onDone: () -> Unit,
) : AddFriendsComponent, ComponentContext by componentContext {
    override var searchTag = mutableStateOf("")
    private var addedFriends = mutableStateOf(listOf<ProfileDto>())
    override var addFriendsResults = mutableStateOf(listOf<ProfileDto>())

    override fun onAddFriendsClicked(text: String) {
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().searchFriendsOnline(text).collect {
                when (it) {
                    is ApiResult.Success -> {
                        addFriendsResults.value =
                            it.data.filterNot { f -> existingFriendIds.contains(f.id) }
                    }

                    is ApiResult.Loading -> {}

                    is ApiResult.Error -> {}
                }
            }
        }
    }

    override fun onAddFriendsResultTapped(userId: String, isChecked: Boolean) {
        addFriendsResults.value.find { it.id == userId }?.let {
            var list = ArrayList(addedFriends.value)
            if (isChecked)
                list.add(it)
            else
                list = ArrayList(list.filter { f -> f.id != userId })
            addedFriends.value = list
        }
    }

    override fun onDoneClicked() {
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().addUsers(addedFriends.value).collect { added ->
                if (added)
                    RepositoryImpl().addGroupMembers(addedFriends.value.map { it.id }, groupId)
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