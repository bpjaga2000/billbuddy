package presentation.totals

import com.arkivanov.decompose.ComponentContext

interface TotalsComponent {
    val groupId: String

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            groupId: String
        ): TotalsComponent
    }
}

class DefaultTotalsComponent(
    private val componentContext: ComponentContext,
    override val groupId: String
) : TotalsComponent, ComponentContext by componentContext {

    class Factory() : TotalsComponent.Factory {
        override fun invoke(componentContext: ComponentContext, groupId: String): TotalsComponent {
            return DefaultTotalsComponent(componentContext, groupId)
        }
    }
}