package presentation.groupsettings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import data.Repository
import data.model.GroupMember
import data.model.dto.GroupDto
import data.remote.ApiResult
import dev.bpj4.billbuddy.tableandmigrations.Groups
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DispatcherUtils.componentCoroutineScope

interface GroupSettingsComponent {
    val groupId: String
    var groupName: String
    val groupMembers: MutableState<List<GroupMember>>
    fun onRemoveMemberClicked(groupMemberId: String)
    fun onAddMemberClicked()
    fun onSaveClicked()
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            groupId: String,
            onAddMemberClick: (String, String) -> Unit,
            popScreen: () -> Unit,
        ): GroupSettingsComponent
    }

    fun getCurrentUserId(): String
}

class DefaultGroupSettingsComponent(
    private val componentContext: ComponentContext,
    override val groupId: String,
    private val onAddMemberClick: (String, String) -> Unit,
    private val popScreen: () -> Unit,
    private val repository: Repository
) : GroupSettingsComponent, ComponentContext by componentContext {
    private var group: Groups? = null
    override var groupName: String = ""

    private val _groupMembers: MutableState<List<GroupMember>> = mutableStateOf(listOf())
    override val groupMembers get() = _groupMembers

    init {
        lifecycle.doOnResume {
            componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
                repository.getGroupById(groupId).collect {
                    group = it
                    group?.let { group -> groupName = group.name }
                }

                getGroupMembers()
            }
        }
    }

    override fun onRemoveMemberClicked(groupMemberId: String) {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            repository.removeMemberFromGroup(listOf(groupMemberId), groupId).collect {
                when (it) {
                    is ApiResult.Success -> {
                        getGroupMembers()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onAddMemberClicked() {
        val memberIds = StringBuilder()
        groupMembers.value.forEach {
            memberIds.append(it.userId)
        }
        onAddMemberClick(memberIds.toString(), groupId)
    }

    override fun onSaveClicked() {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            repository.updateGroup(
                groupId, GroupDto(
                    groupName,
                    group!!.tag,
                    group!!.groupOwnerId
                )
            ).collect {
                //todo
                if (it is ApiResult.Success)
                    withContext(Dispatchers.Main) {
                        popScreen()
                    }
            }
        }
    }

    private suspend fun getGroupMembers() {
        repository.getGroupMemberDetails(groupId).collect {
            _groupMembers.value = it
        }
    }

    override fun getCurrentUserId() = repository.getSettings().getString("id", "")

    class Factory(
        val repository: Repository,
    ) : GroupSettingsComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext, groupId: String,
            onAddMemberClick: (String, String) -> Unit,
            popScreen: () -> Unit,
        ): GroupSettingsComponent {
            return DefaultGroupSettingsComponent(
                componentContext,
                groupId,
                onAddMemberClick,
                popScreen,
                repository
            )
        }
    }

}