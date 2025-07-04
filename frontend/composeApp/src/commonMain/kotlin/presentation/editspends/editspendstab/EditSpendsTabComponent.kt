package presentation.editspends.editspendstab

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.EditSpendTabDetails
import data.model.GroupMemberSplit

interface EditSpendsTabComponent {
    val type: Int
    val amount: String
    var splitDetails: MutableState<List<EditSpendTabDetails>>
}

class DefaultEditSpendsTabComponent(
    componentContext: ComponentContext,
    override val type: Int,
    override var splitDetails: MutableState<List<EditSpendTabDetails>>,
    override val amount: String,
) : EditSpendsTabComponent, ComponentContext by componentContext