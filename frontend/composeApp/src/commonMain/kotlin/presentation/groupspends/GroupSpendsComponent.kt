package presentation.groupspends

import com.arkivanov.decompose.ComponentContext

interface GroupSpendsComponent{
    fun onGroupSpendClicked()
}

class DefaultGroupSpendsComponent(
    private val componentContext: ComponentContext,
    val onGroupSpendClick: () -> Unit
) : GroupSpendsComponent, ComponentContext by componentContext {
    override fun onGroupSpendClicked() {
        this.onGroupSpendClick()
    }
}