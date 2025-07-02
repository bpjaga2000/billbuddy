package presentation.creategroup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.GroupTags
import data.remote.ApiResult
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import utils.DispatcherUtils.componentCoroutineScope

interface CreateGroupComponent {
    val isLoading: MutableState<Boolean>
    fun createGroup(groupName: String, groupTag: GroupTags)
}

class DefaultCreateGroupComponent(
    private val componentContext: ComponentContext
) : CreateGroupComponent, ComponentContext by componentContext {
    override val isLoading: MutableState<Boolean> = mutableStateOf(false)

    override fun createGroup(groupName: String, groupTag: GroupTags) {
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().createGroup(groupName, groupTag).collectLatest {
                when (it) {
                    is ApiResult.Success -> {
                        isLoading.value = false
                    }

                    is ApiResult.Error -> {
                        isLoading.value = false
                    }

                    is ApiResult.Loading -> {
                        isLoading.value = true
                    }
                }
            }
        }
    }

}