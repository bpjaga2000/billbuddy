package presentation.balances

import com.arkivanov.decompose.ComponentContext
import data.model.Balance

interface BalancesComponent {
    val groupId: String
    val lentBalances: List<Balance>
    val borrowedBalances: List<List<Balance>>
}

class DefaultBalancesComponent(
    private val componentContext: ComponentContext,
    override val groupId: String,
) : BalancesComponent, ComponentContext by componentContext {
    override val lentBalances: List<Balance> = listOf()
    override val borrowedBalances: List<List<Balance>> = listOf()
}
