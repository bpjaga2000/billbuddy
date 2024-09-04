package presentation.spenddetails.spenddetailstab

import com.arkivanov.decompose.ComponentContext

interface SpendDetailsTabComponent {
    val type: String
    val data: List<Any>
}

class DefaultSpendDetailsTabComponent(
    componentContext: ComponentContext,
    override val type: String,
    override val data: List<Any>,
) : SpendDetailsTabComponent, ComponentContext by componentContext