package presentation.groupspends

import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.russhwolf.settings.get
import data.model.SpendWithSplit
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.DataStore
import utils.DispatcherUtils.componentCoroutineScope
import utils.calculateOwes

interface GroupSpendsComponent {
    val groupId: String
    val spendList: MutableValue<List<SpendWithSplit>>
    val groupUsersDetails: MutableState<Map<String, String>>
    val totalBalance: MutableDoubleState
    fun onGroupSpendClicked(id: String)
    fun onAddSpendClicked()
    fun onGroupSpendSettingsClicked()
    fun onBalancesClicked()
}

class DefaultGroupSpendsComponent(
    private val componentContext: ComponentContext,
    override val groupId: String,
    val onGroupSpendClick: (String) -> Unit,
    val onAddSpendClick: () -> Unit,
    val onGroupSpendSettingsClick: (groupId: String) -> Unit,
    val onBalancesClick: (groupId: String) -> Unit
) : GroupSpendsComponent, ComponentContext by componentContext {
    override val spendList: MutableValue<List<SpendWithSplit>> = MutableValue(listOf())
    private val currentUserId = DataStore.settings.get<String>("id")!!
    override val groupUsersDetails = mutableStateOf(mapOf<String, String>())
    override val totalBalance = mutableDoubleStateOf(0.0)

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            RepositoryImpl().getGroupMemberDetails(groupId).collect {
                it.forEach { user ->
                    groupUsersDetails.value =
                        groupUsersDetails.value.plus(Pair(user.userId, user.userName))
                }
            }
            RepositoryImpl().getSpendsAfterLastSettle(groupId).collect { spends ->
                totalBalance.value = 0.0
                spends.forEach { spend ->
                    spend.splits.let {
                        spend.owe = spend.calculateOwes(currentUserId)
                        totalBalance.value = totalBalance.value + spend.calculateOwes(currentUserId)
                    }
                }
            }
            RepositoryImpl().getSpendAndSplitForGroup(groupId).collect { spends ->
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

    override fun onBalancesClicked() {
        onBalancesClick(groupId)
    }
}