package presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.Balance
import data.model.GroupWithOwes
import data.remote.ApiResult
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DataStore
import utils.DispatcherUtils.componentCoroutineScope
import utils.calculateOwes
import utils.checkGroupSettles

interface HomeComponent {

    var groups: MutableState<List<GroupWithOwes>>
    var friendBalances: MutableState<List<Balance>>
    fun onFriendBalanceClicked(payeeId: String)
    fun onGroupClick(groupId: String)
    fun onCreateGroupClicked()
    fun onLogoutClick()
}

class DefaultHomeComponent(
    val componentContext: ComponentContext,
    private val onSpendClicked: (groupId: String) -> Unit,
    private val onCreateGroupClicked: () -> Unit,
    private val onLogoutClick: () -> Unit,
    private val onFriendBalanceClick: (String, String) -> Unit
) : HomeComponent, ComponentContext by componentContext {

    override var groups: MutableState<List<GroupWithOwes>> = mutableStateOf(listOf())
    override var friendBalances: MutableState<List<Balance>> = mutableStateOf(listOf())

    override fun onFriendBalanceClicked(payeeId: String) {
        onFriendBalanceClick(currentUserId, payeeId)
    }

    private val currentUserId = DataStore.settings.getString("id", "")

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            val groupsList = arrayListOf<GroupWithOwes>()
            RepositoryImpl().sync(currentUserId).collect { sync ->
                when (sync) {
                    is ApiResult.Success -> {
                        RepositoryImpl().saveSyncData(sync.data).collectLatest { isDone ->
                            if (isDone) {
//                                    _isLoading.value = false
                                RepositoryImpl().getAllGroups().collect { g ->
                                    groupsList.addAll(g.map { GroupWithOwes(it, 0.0) })
                                    g.forEach {gId ->
                                        checkGroupSettles(gId.id).collect {

                                        }
                                    }
                                }
                            }
                        }
                    }

                    is ApiResult.Error -> withContext(Dispatchers.Main) {
//                        _isLoading.value = false
                    }

                    is ApiResult.Loading -> {}
                }
                val balances = arrayListOf<Balance>()
                RepositoryImpl().getAllUsers().collect {
                    balances.addAll(
                        it.map { user ->
                            Balance(
                                null,
                                user.id,
                                user.name,
                                0.0,
                                null
                            )
                        }.filter { f -> f.userId != currentUserId }
                    )
                    groupsList.forEach { group ->
                        RepositoryImpl().getSpendsAfterLastSettle(group.group.id)
                            .collect { spendWithSplit ->
                                spendWithSplit.forEach { sp ->
                                    group.owe = group.owe + sp.calculateOwes(currentUserId)
                                    balances.forEach { f ->
                                        if ((sp.spend.spentBy == f.userId && sp.splits.find { it.userId == currentUserId } != null)
                                            || (sp.spend.spentBy == currentUserId && sp.splits.find { it.userId == f.userId } != null))
                                            f.amount =
                                                f.amount + sp.calculateOwes(f.userId)
                                    }
                                }
                            }
                    }
                    friendBalances.value = balances.filter { f -> f.amount != 0.0 }
                    groups.value = groupsList
                }
            }
        }
    }

    override fun onGroupClick(groupId: String) {
        onSpendClicked(groupId)
    }

    override fun onCreateGroupClicked() {
        onCreateGroupClicked.invoke()
    }

    override fun onLogoutClick() {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            RepositoryImpl().logout().collect {
                when (it) {
                    is ApiResult.Success -> {
                        withContext(Dispatchers.Main) {
                            RepositoryImpl().clearDb()
                            DataStore.settings.clear()
                            onLogoutClick.invoke()
                        }
                    }

                    else -> {}
                }
            }
        }
    }

}