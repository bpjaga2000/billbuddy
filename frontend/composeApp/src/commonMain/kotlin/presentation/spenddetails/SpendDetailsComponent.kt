package presentation.spenddetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.SpendWithSplit
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import utils.DispatcherUtils.componentCoroutineScope
import utils.calculateOwes

interface SpendDetailsComponent {
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
    override var spendDetails = mutableStateOf<SpendWithSplit?>(null)
    override val userNames = mutableStateOf(mapOf<String, String>())

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().getSpendAndSplitWithSpendId(spendId).collect { spend ->
                spendDetails.value = spend
                RepositoryImpl().getUserNameFromId(spend.spend.createdBy).collect {
                    userNames.value = userNames.value.plus(Pair(spend.spend.createdBy, it))
                }
                spend.splits.forEach { split ->
                    componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
                        RepositoryImpl().getUserNameFromId(split.userId).collect {
                            userNames.value = userNames.value.plus(Pair(split.userId, it))
                        }
                    }
                }
            }
        }
    }

    override fun onDeleteClicked() {
        this.onDeleteClicked.invoke()
    }

    override fun onEditClicked() {
        this.onEditClicked.invoke(spendId)
    }

    override fun calculateOwes(spentBy: String): Double =
        spendDetails.value?.calculateOwes(spentBy) ?: 0.0

}