package presentation.groupspends

import com.arkivanov.decompose.ComponentContext

interface GroupSpendsComponent{

    fun onGroupSpendClicked()

    fun onAddSpendClicked()

}

class DefaultGroupSpendsComponent(
    private val componentContext: ComponentContext,
    val onGroupSpendClick: () -> Unit,
    val onAddSpendClick: () -> Unit
) : GroupSpendsComponent, ComponentContext by componentContext {
    override fun onGroupSpendClicked() {
        this.onGroupSpendClick()
    }

    override fun onAddSpendClicked() {
        this.onAddSpendClick()
    }
}