package presentation.groupspends

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import data.model.SpendWithSplit
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.DispatcherUtils.componentCoroutineScope

interface GroupSpendsComponent {
    val groupId: String
    val spendList: MutableValue<List<SpendWithSplit>>

    fun onGroupSpendClicked(id: String)

    fun onAddSpendClicked()

    fun onGroupSpendSettingsClicked()
    fun getUserNameFromId(spentBy: String): String

}

class DefaultGroupSpendsComponent(
    private val componentContext: ComponentContext,
    override val groupId: String,
    val onGroupSpendClick: (String) -> Unit,
    val onAddSpendClick: () -> Unit,
    val onGroupSpendSettingsClick: (groupId: String) -> Unit
) : GroupSpendsComponent, ComponentContext by componentContext {
    override val spendList: MutableValue<List<SpendWithSplit>> = MutableValue(listOf())

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().getSpendAndSplit(groupId).collect { spends ->
                spendList.update { spends }
            }
        }
    }

    override fun onGroupSpendClicked(id: String) {
        this.onGroupSpendClick(id)
    }

    override fun onAddSpendClicked() {
        this.onAddSpendClick()
    }

    override fun onGroupSpendSettingsClicked() {
        this.onGroupSpendSettingsClick(groupId)
    }

    override fun getUserNameFromId(spentBy: String): String {
        var name = ""
        runBlocking(Dispatchers.IO) {
            name = RepositoryImpl().getUserNameFromId(spentBy).single()
        }
        return name
    }
}