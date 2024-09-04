package presentation.spenddetails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import presentation.spenddetails.spenddetailstab.DefaultSpendDetailsTabComponent
import presentation.spenddetails.spenddetailstab.SpendDetailsTabComponent

interface SpendDetailsComponent {

    @OptIn(ExperimentalDecomposeApi::class)
    val pageStack: Value<ChildPages<*, SpendDetailsTabComponent>>

    fun onSaveClicked()

}

class DefaultSpendDetailsComponent(
    private val componentContext: ComponentContext,
    val onSaved: () -> Unit
) : SpendDetailsComponent, ComponentContext by componentContext {
    @OptIn(ExperimentalDecomposeApi::class)
    private val navigation = PagesNavigation<Config>()

    @OptIn(ExperimentalDecomposeApi::class)
    override val pageStack: Value<ChildPages<*, SpendDetailsTabComponent>> = childPages(
        source = navigation,
        serializer = Config.serializer(),
        initialPages = {
            Pages(
                items = List(10) { index -> Config(index.toString()) },
                selectedIndex = 0
            )
        }
    ) { config, childComponentContext ->
        DefaultSpendDetailsTabComponent(
            componentContext = childComponentContext,
            type = config.type,
            data = listOf(1,2,3,4,5)
        )

    }

    override fun onSaveClicked() {
        onSaved()
    }

    @Serializable
    private data class Config(val type: String)
}