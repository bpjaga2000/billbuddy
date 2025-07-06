package presentation.spenddetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.SpendWithSplit
import data.repository.RepositoryImpl
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
}

class DefaultSpendDetailsComponent(
    private val componentContext: ComponentContext,
    private val spendId: String,
    private val onDeleteClicked: () -> Unit,
    private val onEditClicked: (spendId: String) -> Unit
) : SpendDetailsComponent, ComponentContext by componentContext {
    override val spenderOwes = mutableStateOf(0.0)
    override var spendDetails = mutableStateOf<SpendWithSplit?>(null)
    override val userNames = mutableStateOf(mapOf<String, String>())

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            RepositoryImpl().getSpendAndSplitWithSpendId(spendId).collect { spend ->
                spendDetails.value = spend
                spenderOwes.value = spend.calculateOwes(spend.spend.spentBy)
                RepositoryImpl().getUserNameFromId(spend.spend.createdBy).collect {
                    userNames.value = userNames.value.plus(Pair(spend.spend.createdBy, it))
                }
                spend.splits.forEach { split ->
                    componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
                        RepositoryImpl().getUserNameFromId(split.userId).collect {
                            userNames.value = userNames.value.plus(Pair(split.userId, it))
                        }
                    }
                }
            }
        }
    }

    override fun onDeleteClicked() {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            RepositoryImpl().deleteSpend(spendId).collect {
                if (it)
                    withContext(Dispatchers.Main) {
                        this@DefaultSpendDetailsComponent.onDeleteClicked.invoke()
                    }
            }
        }
    }

    override fun onEditClicked() {
        this.onEditClicked.invoke(spendId)
    }

    override fun calculateOwes(spentBy: String): Double =
        spendDetails.value?.calculateOwes(spentBy) ?: 0.0

}