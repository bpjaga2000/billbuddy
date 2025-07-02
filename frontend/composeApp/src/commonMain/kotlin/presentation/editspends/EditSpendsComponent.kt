package presentation.editspends

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import data.model.GroupMemberSplit
import kotlinx.serialization.Serializable
import presentation.editspends.editspendstab.DefaultEditSpendsTabComponent
import presentation.editspends.editspendstab.EditSpendsTabComponent

interface EditSpendsComponent {

    val pageStack: Value<ChildPages<*, EditSpendsTabComponent>>

    var selection: MutableState<Int>

    val amount : MutableState<String>

    fun onSaveClicked()

    fun onPageSelected(index: Int)

}

class DefaultEditSpendsComponent(
    private val componentContext: ComponentContext,
    override var selection: MutableState<Int> = mutableStateOf(0),
    val onSaved: () -> Unit,
    private val groupMembers: List<GroupMemberSplit> = listOf(GroupMemberSplit("abc", "abcdf", false, 0f), GroupMemberSplit("def", "dafsa", false, 0f)),
) : EditSpendsComponent, ComponentContext by componentContext {

    override val amount = mutableStateOf("")

    private val navigation = PagesNavigation<Config>()

    override val pageStack: Value<ChildPages<*, EditSpendsTabComponent>> = childPages(
        source = navigation,
        serializer = Config.serializer(),
        initialPages = {
            Pages(
                items = List(5) { index -> Config(index + 1) },
                selectedIndex = selection.value
            )
        }
    ) { config, childComponentContext ->
        DefaultEditSpendsTabComponent(
            componentContext = childComponentContext,
            peopleInvolved = groupMembers,
            type = config.type,
            amount = amount.value
        )

    }

    override fun onSaveClicked() {
        onSaved()
    }

    override fun onPageSelected(index: Int) {
        selection.value = index
        navigation.select(index)
    }

    @Serializable
    private data class Config(val type: Int)
}