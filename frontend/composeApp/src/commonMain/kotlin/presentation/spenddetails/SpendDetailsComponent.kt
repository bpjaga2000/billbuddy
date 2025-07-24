package presentation.spenddetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.Repository
import data.model.SpendWithSplit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DispatcherUtils.componentCoroutineScope
import utils.calculateOwes

interface SpendDetailsComponent {
    val spenderOwes: MutableState<Double>
    var spendDetails: MutableState<SpendWithSplit?>
    val userNames: MutableState<Map<String, String>>
    fun onDeleteClicked()
    fun onEditClicked()
    fun calculateOwes(spentBy: String): Double
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            spendId: String,
            onDeleteClicked: () -> Unit,
            onEditClicked: (spendId: String) -> Unit
        ): SpendDetailsComponent
    }
}

class DefaultSpendDetailsComponent(
    private val componentContext: ComponentContext,
    private val spendId: String,
    private val onDeleteClicked: () -> Unit,
    private val onEditClicked: (spendId: String) -> Unit,
    private val repository: Repository
) : SpendDetailsComponent, ComponentContext by componentContext {
    override val spenderOwes = mutableStateOf(0.0)
    override var spendDetails = mutableStateOf<SpendWithSplit?>(null)
    override val userNames = mutableStateOf(mapOf<String, String>())
    private lateinit var groupId: String

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            repository.getSpendAndSplitWithSpendId(spendId).collect { spend ->
                groupId = spend.spend.groupId
                spendDetails.value = spend
                spenderOwes.value = spend.calculateOwes(spend.spend.spentBy)
                repository.getUserNameFromId(spend.spend.createdBy).collect {
                    userNames.value = userNames.value.plus(Pair(spend.spend.createdBy, it))
                }
                spend.splits.forEach { split ->
                    componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
                        repository.getUserNameFromId(split.userId).collect {
                            userNames.value = userNames.value.plus(Pair(split.userId, it))
                        }
                    }
                }
            }
        }
    }

    override fun onDeleteClicked() {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            repository.groupSettleCorrection(
                spendDetails.value!!.spend.groupId,
                spendDetails.value!!.spend.updatedAt
            ).collect {
                repository.deleteSpend(spendId).collect {
                    if (it)
                        withContext(Dispatchers.Main) {
                            this@DefaultSpendDetailsComponent.onDeleteClicked.invoke()
                        }
                }
            }
        }
    }

    override fun onEditClicked() {
        this.onEditClicked.invoke(spendId)
    }

    override fun calculateOwes(spentBy: String): Double =
        spendDetails.value?.calculateOwes(spentBy) ?: 0.0


    class Factory(
        val repository: Repository,
    ) : SpendDetailsComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext, spendId: String,
            onDeleteClicked: () -> Unit,
            onEditClicked: (spendId: String) -> Unit
        ): SpendDetailsComponent {
            return DefaultSpendDetailsComponent(
                componentContext,
                spendId,
                onDeleteClicked,
                onEditClicked,
                repository
            )
        }
    }
}