package presentation.groupsettings

import com.arkivanov.decompose.ComponentContext
import data.model.GroupMember

interface GroupSettingsComponent {
    val groupId: String?
    val groupName: String
    val groupMembers: List<GroupMember>
    fun onRemoveMemberClicked(userId: String)
    fun onAddMemberClicked()
    fun onNameEdited(groupName: String)
    fun onSaveClicked()
}

class DefaultGroupSettingsComponent(
    private val componentContext: ComponentContext,
    override val groupId: String?,
    private val onAddMemberClick: () -> Unit,
    private val onSaveClick: () -> Unit
) : GroupSettingsComponent, ComponentContext by componentContext {

    override val groupName: String = ""

    override val groupMembers: List<GroupMember> = listOf()

    override fun onRemoveMemberClicked(userId: String) {
        TODO("Not yet implemented")
    }

    override fun onAddMemberClicked() {
        onAddMemberClick()
    }

    override fun onNameEdited(groupName: String) {
        TODO("Not yet implemented")
    }

    override fun onSaveClicked() {
        onSaveClick()
    }

}