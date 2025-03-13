package presentation.settleup

import com.arkivanov.decompose.ComponentContext
import data.model.Balance

interface SettleUpComponent {
    val groupId: String
    val lender: Balance
    val borrower: Balance
    fun onSettleClicked()
}

class DefaultSettleUpComponent(
    private val componentContext: ComponentContext,
    override val groupId: String,
    override val lender: Balance,
    override val borrower: Balance,
    val onSettleClick: () -> Unit
) : SettleUpComponent, ComponentContext by componentContext {

    override fun onSettleClicked() {
        onSettleClick()
    }

}