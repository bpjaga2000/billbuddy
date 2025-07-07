package presentation.balances

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import data.model.Balance
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.DispatcherUtils.componentCoroutineScope
import utils.calculateOwes

interface BalancesComponent {
    val groupId: String
    val balances: MutableState<LinkedHashMap<String, Balance>>
    fun onSettleUpClicked(groupId: String, payerId: String, payeeId: String)
}

class DefaultBalancesComponent(
    private val componentContext: ComponentContext,
    override val groupId: String,
    private val onSettleUpClick: (String, String, String) -> Unit
) : BalancesComponent, ComponentContext by componentContext {
    override val balances = mutableStateOf(linkedMapOf<String, Balance>())

    init {
        lifecycle.doOnResume {
            componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
                val balanceList = linkedMapOf<String, Balance>()
                RepositoryImpl().getGroupMemberDetails(groupId).collect {
                    balanceList.putAll(
                        it.map { user ->
                            Pair(
                                user.userId, Balance(
                                    null,
                                    user.userId,
                                    user.userName,
                                    0.0,
                                    groupId
                                )
                            )
                        }
                    )
                    val temp = linkedMapOf<String, Balance>().apply { putAll(balanceList) }
                    balanceList.forEach { inner ->
                        inner.value.userSettles =
                            linkedMapOf<String, Balance>().apply { putAll(temp.filter { t -> t.key != inner.key }) }
                    }
                }
                RepositoryImpl().getSpendsAfterLastSettle(groupId).collect {
                    balanceList.iterator().forEach { (k, v) ->
                        it.forEach { sp ->
                            v.amount = v.amount + sp.calculateOwes(k)
                            v.userSettles = v.userSettles ?: linkedMapOf()
                            balanceList.keys.forEach { keys ->
                                if ((sp.spend.spentBy == keys || sp.splits.find { s -> s.userId == keys } != null)) {
                                    v.userSettles?.get(keys)?.let { vus ->
                                        vus.amount =
                                            (v.userSettles!![keys]?.amount
                                                ?: 0.0) - sp.calculateOwes(
                                                keys
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
                balances.value = balanceList
            }
        }
    }

    override fun onSettleUpClicked(
        groupId: String,
        payerId: String,
        payeeId: String
    ) {
        onSettleUpClick(groupId, payerId, payeeId)
    }

}
