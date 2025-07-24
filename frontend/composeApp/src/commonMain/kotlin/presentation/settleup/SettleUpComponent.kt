package presentation.settleup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.Repository
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
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            groupId: String?,
            payerId: String,
            payeeId: String,
            onSettleClick: () -> Unit
        ): SettleUpComponent
    }
}

class DefaultSettleUpComponent(
    private val componentContext: ComponentContext,
    override val groupId: String?,
    private val payerId: String,
    private val payeeId: String,
    val onSettleClick: () -> Unit,
    private val repository: Repository
) : SettleUpComponent, ComponentContext by componentContext {

    private val groupIds = arrayListOf<String>()
    override val amountToPay = mutableStateOf("")
    override val payerName = mutableStateOf("")
    override val payeeName = mutableStateOf("")
    override var amount = 0.0
    private val groupWiseAmount = hashMapOf<String, Double>()


    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            repository.getUserNameFromId(payerId).collect {
                payerName.value = it
            }
            repository.getUserNameFromId(payeeId).collect {
                payeeName.value = it
            }
            groupId?.let {
                groupIds.add(groupId)
            } ?: run {
                repository.getGroupsWithPendingBalances(payerId, payeeId).collect {
                    groupIds.addAll(it)
                }
            }
            groupIds.forEach { gId ->
                var amountPerGroup = 0.0
                repository.getSpendsAfterLastSettle(gId).collect {
                    repository.getSpendsAfterLastSettle(gId)
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
            repository.settleUp(
                payerId,
                payeeId,
                groupWiseAmount
            ).collect { r ->
                if (r) {
                    groupWiseAmount.keys.forEach {
                        checkGroupSettlesAndSync(repository, it).collect {
                        }
                    }
                    withContext(Dispatchers.Main) {
                        onSettleClick()
                    }
                }
            }
        }
    }

    class Factory(
        val repository: Repository,
    ) : SettleUpComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext, groupId: String?,
            payerId: String,
            payeeId: String,
            onSettleClick: () -> Unit
        ): SettleUpComponent {
            return DefaultSettleUpComponent(
                componentContext,
                groupId,
                payerId,
                payeeId,
                onSettleClick,
                repository
            )
        }
    }

}