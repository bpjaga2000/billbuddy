package presentation.spenddetails

import com.arkivanov.decompose.ComponentContext

interface SpendDetailsComponent {

    fun onDeleteClicked()

    fun onEditClicked()
}

class DefaultSpendDetailsComponent(
    private val componentContext: ComponentContext,
    private val onDeleteClicked: () -> Unit,
    private val onEditClicked: () -> Unit
) : SpendDetailsComponent, ComponentContext by componentContext {

    override fun onDeleteClicked() {
        this.onDeleteClicked.invoke()
    }

    override fun onEditClicked() {
        this.onEditClicked.invoke()
    }

}