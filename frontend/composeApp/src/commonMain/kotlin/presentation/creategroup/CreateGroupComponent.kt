package presentation.creategroup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.GroupTags
import data.Repository
import data.remote.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import utils.DispatcherUtils.componentCoroutineScope

interface CreateGroupComponent {
    var isLoading: MutableState<Boolean>
    fun createGroup(groupName: String, groupTag: GroupTags)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onGroupCreated: () -> Unit
        ): CreateGroupComponent
    }
}

class DefaultCreateGroupComponent(
    private val componentContext: ComponentContext,
    private val onGroupCreated: () -> Unit,
    private val repository: Repository
) : CreateGroupComponent, ComponentContext by componentContext {
    override var isLoading: MutableState<Boolean> = mutableStateOf(false)

    override fun createGroup(groupName: String, groupTag: GroupTags) {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            repository.createGroup(groupName, groupTag).collectLatest {
                when (it) {
                    is ApiResult.Success -> {
                        isLoading.value = false
                        onGroupCreated()
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

    class Factory(
        val repository: Repository
    ) : CreateGroupComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onGroupCreated: () -> Unit,
        ): CreateGroupComponent {
            return DefaultCreateGroupComponent(
                componentContext,
                onGroupCreated,
                repository
            )
        }

    }
}