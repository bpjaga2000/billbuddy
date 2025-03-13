package presentation.editspends.editspendstab

import com.arkivanov.decompose.ComponentContext
import data.model.EditSpendTabDetails
import data.model.GroupMemberSplit

interface EditSpendsTabComponent {
    val type: Int
    val peopleInvolved: List<GroupMemberSplit>
    val amount: String
    var splitDetails: List<EditSpendTabDetails>
}

class DefaultEditSpendsTabComponent(
    componentContext: ComponentContext,
    override val type: Int,
    override val peopleInvolved: List<GroupMemberSplit>,
    override val amount: String,
) : EditSpendsTabComponent, ComponentContext by componentContext {
    override var splitDetails: List<EditSpendTabDetails> =
        List(peopleInvolved.size) { it ->
            EditSpendTabDetails(
                peopleInvolved[it].userId,
                peopleInvolved[it].userName
            )
        }
}