package presentation.groupspends

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.russhwolf.settings.get
import constants.SplitType
import data.model.SpendWithSplit
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.DataStore
import utils.DispatcherUtils.componentCoroutineScope

interface GroupSpendsComponent {
    val groupId: String
    val spendList: MutableValue<List<SpendWithSplit>>
    fun onGroupSpendClicked(id: String)
    fun onAddSpendClicked()
    fun onGroupSpendSettingsClicked()
    fun getUserNameFromId(spentBy: String): String
    fun onSettleUpClicked()
    fun onBalancesClicked()
}

class DefaultGroupSpendsComponent(
    private val componentContext: ComponentContext,
    override val groupId: String,
    val onGroupSpendClick: (String) -> Unit,
    val onAddSpendClick: () -> Unit,
    val onGroupSpendSettingsClick: (groupId: String) -> Unit,
    val onSettleUpClick: (groupId: String) -> Unit,
    val onBalancesClick: (groupId: String) -> Unit
) : GroupSpendsComponent, ComponentContext by componentContext {
    override val spendList: MutableValue<List<SpendWithSplit>> = MutableValue(listOf())
    private val currentUserId = DataStore.settings.get<String>("id")

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().getSpendAndSplitForGroup(groupId).collect { spends ->
                spends.forEach { spend ->
                    spend.splits.find { it.userId == currentUserId }?.let {
                        val currentUserValue =
                            spend.splits.find { it.userId == currentUserId }?.value_ ?: 0.0
                        //negative owe means other members owe to the user
                        spend.owe = when (spend.splits[0].splitType.toInt()) {
                            SplitType.EQUAL -> {
                                val sharePerHead = spend.spend.totalAmount / spend.splits.size
                                if (currentUserId == spend.spend.spentBy) {
                                    sharePerHead - spend.spend.totalAmount
                                } else {
                                    sharePerHead
                                }
                            }

                            SplitType.AMOUNT -> currentUserValue

                            SplitType.SHARE -> {
                                var totalShares =
                                    spend.splits.sumOf { it.value_ }
                                val costPerShare = spend.spend.totalAmount / totalShares
                                if (currentUserId == spend.spend.spentBy) {
                                    costPerShare * currentUserValue - spend.spend.totalAmount
                                } else {
                                    costPerShare * currentUserValue
                                }
                            }

                            SplitType.RATIO -> spend.spend.totalAmount * currentUserValue

                            SplitType.DIFFERENCE -> {
                                val difference = spend.splits.sumOf { it.value_ }
                                val sharePerHeadWithoutDifference =
                                    (spend.spend.totalAmount - difference) / spend.splits.size
                                if (currentUserId == spend.spend.spentBy) {
                                    spend.spend.totalAmount - sharePerHeadWithoutDifference - currentUserValue
                                } else {
                                    sharePerHeadWithoutDifference + currentUserValue
                                }
                            }

                            else -> 0.0
                        }
                    }
                }
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

    override fun onSettleUpClicked() {
        onSettleUpClick(groupId)
    }

    override fun onBalancesClicked() {
        onBalancesClick(groupId)
    }
}