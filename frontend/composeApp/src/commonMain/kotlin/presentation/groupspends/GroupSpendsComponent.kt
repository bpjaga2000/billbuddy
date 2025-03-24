package presentation.groupspends

import com.arkivanov.decompose.ComponentContext

interface GroupSpendsComponent {

    fun onGroupSpendClicked()

    fun onAddSpendClicked()

    fun onGroupSpendSettingsClicked()

}

class DefaultGroupSpendsComponent(
    private val componentContext: ComponentContext,
    val onGroupSpendClick: () -> Unit,
    val onAddSpendClick: () -> Unit,
    val onGroupSpendSettingsClick: () -> Unit,
) : GroupSpendsComponent, ComponentContext by componentContext {
    override fun onGroupSpendClicked() {
        this.onGroupSpendClick()
    }

    override fun onAddSpendClicked() {
        this.onAddSpendClick()
    }

    override fun onGroupSpendSettingsClicked() {
        this.onGroupSpendSettingsClick()
    }
}