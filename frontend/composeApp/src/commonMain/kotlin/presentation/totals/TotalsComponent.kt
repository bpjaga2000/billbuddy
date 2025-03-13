package presentation.totals

import com.arkivanov.decompose.ComponentContext

interface TotalsComponent {
    val groupId: String
}

class DefaultTotalsComponent(
    private val componentContext: ComponentContext,
    override val groupId: String
): TotalsComponent, ComponentContext by componentContext {

}