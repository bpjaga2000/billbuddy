package presentation.groupsettings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.GroupMember
import data.model.dto.GroupDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import dev.bpj4.billbuddy.tableandmigrations.Groups
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
}

class DefaultGroupSettingsComponent(
    private val componentContext: ComponentContext,
    override val groupId: String,
    private val onAddMemberClick: (String, String) -> Unit,
    private val popScreen: () -> Unit
) : GroupSettingsComponent, ComponentContext by componentContext {
    private var group: Groups? = null
    override var groupName: String = ""

    private val _groupMembers: MutableState<List<GroupMember>> = mutableStateOf(listOf())
    override val groupMembers get() = _groupMembers

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().getGroupById(groupId).collect {
                group = it
                group?.let { group -> groupName = group.name }
            }

            getGroupMembers()
        }
    }

    override fun onRemoveMemberClicked(groupMemberId: String) {
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().removeMemberFromGroup(listOf(groupMemberId), groupId).collect {
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
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().updateGroup(
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
        RepositoryImpl().getGroupMemberDetails(groupId).collect {
            _groupMembers.value = it
        }
    }

}