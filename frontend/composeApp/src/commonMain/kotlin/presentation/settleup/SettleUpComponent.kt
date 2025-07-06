package presentation.settleup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DispatcherUtils.componentCoroutineScope
import utils.calculateOwes
import utils.checkGroupSettlesAndSync
import kotlin.math.absoluteValue

interface SettleUpComponent {
    val groupId: String?
    val payerName: MutableState<String>
    val payeeName: MutableState<String>
    var amount: Double
    val amountToPay: MutableState<String>
    fun onSettleClicked()
}

class DefaultSettleUpComponent(
    private val componentContext: ComponentContext,
    override val groupId: String?,
    private val payerId: String,
    private val payeeId: String,
    val onSettleClick: () -> Unit
) : SettleUpComponent, ComponentContext by componentContext {

    private val groupIds = arrayListOf<String>()
    override val amountToPay = mutableStateOf("")
    override val payerName = mutableStateOf("")
    override val payeeName = mutableStateOf("")
    override var amount = 0.0
    private val groupWiseAmount = hashMapOf<String, Double>()


    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            RepositoryImpl().getUserNameFromId(payerId).collect {
                payerName.value = it
            }
            RepositoryImpl().getUserNameFromId(payeeId).collect {
                payeeName.value = it
            }
            groupId?.let {
                groupIds.add(groupId)
            } ?: run {
                RepositoryImpl().getGroupsWithPendingBalances(payerId, payeeId).collect {
                    groupIds.addAll(it)
                }
            }
            groupIds.forEach { gId ->
                var amountPerGroup = 0.0
                RepositoryImpl().getSpendsAfterLastSettle(gId).collect {
                    RepositoryImpl().getSpendsAfterLastSettle(gId)
                        .collect { spendWithSplit ->
                            spendWithSplit.forEach { sp ->
                                if ((sp.spend.spentBy == payerId && sp.splits.find { it.userId == payeeId } != null)
                                    || (sp.spend.spentBy == payeeId && sp.splits.find { it.userId == payerId } != null))
                                    amountPerGroup = amountPerGroup + sp.calculateOwes(payeeId)
                            }
                        }
                }
                groupWiseAmount.put(gId, amountPerGroup)
            }
            amount = groupWiseAmount.values.sumOf { it }
            amountToPay.value = amount.absoluteValue.toString()
        }
    }

    override fun onSettleClicked() {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            RepositoryImpl().settleUp(
                payerId,
                payeeId,
                groupWiseAmount
            ).collect { r ->
                if (r) {
                    groupWiseAmount.keys.forEach {
                        checkGroupSettlesAndSync(it).collect {
                        }
                    }
                    withContext(Dispatchers.Main) {
                        onSettleClick()
                    }
                }
            }
        }
    }

}