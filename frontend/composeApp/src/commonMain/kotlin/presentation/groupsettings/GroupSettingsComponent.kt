package presentation.groupsettings

import com.arkivanov.decompose.ComponentContext
import data.model.GroupMember

interface GroupSettingsComponent {
    val groupId: String?
    val groupName: String
    val groupMembers: List<GroupMember>
    fun onRemoveMemberClicked(userId: String)
    fun onAddMemberClicked(userIds: List<String>)
    fun onNameEdited(groupName: String)
}

class DefaultGroupSettingsComponent(
    private val componentContext: ComponentContext,
    override val groupId: String?,
) : GroupSettingsComponent, ComponentContext by componentContext {

    override val groupName: String = ""

    override val groupMembers: List<GroupMember> = listOf()

    override fun onRemoveMemberClicked(userId: String) {
        TODO("Not yet implemented")
    }

    override fun onAddMemberClicked(userIds: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onNameEdited(groupName: String) {
        TODO("Not yet implemented")
    }

}