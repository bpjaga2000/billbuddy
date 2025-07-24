package presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.lifecycle.doOnStart
import data.Repository
import data.model.Balance
import data.model.GroupWithOwes
import data.remote.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DispatcherUtils.componentCoroutineScope
import utils.calculateOwes
import utils.checkGroupSettlesAndSync

interface HomeComponent {

    var groups: MutableState<List<GroupWithOwes>>
    var friendBalances: MutableState<List<Balance>>
    fun onFriendBalanceClicked(payeeId: String)
    fun onGroupClick(groupId: String)
    fun onCreateGroupClicked()
    fun onLogoutClick()
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onSpendClicked: (groupId: String) -> Unit,
            onCreateGroupClicked: () -> Unit,
            onLogoutClick: () -> Unit,
            onFriendBalanceClick: (String, String) -> Unit
        ): HomeComponent
    }
}

class DefaultHomeComponent(
    val componentContext: ComponentContext,
    private val onSpendClicked: (groupId: String) -> Unit,
    private val onCreateGroupClicked: () -> Unit,
    private val onLogoutClick: () -> Unit,
    private val onFriendBalanceClick: (String, String) -> Unit,
    private val repository: Repository
) : HomeComponent, ComponentContext by componentContext {

    override var groups: MutableState<List<GroupWithOwes>> = mutableStateOf(listOf())
    override var friendBalances: MutableState<List<Balance>> = mutableStateOf(listOf())

    override fun onFriendBalanceClicked(payeeId: String) {
        onFriendBalanceClick(currentUserId, payeeId)
    }

    private val currentUserId = repository.getSettings().getString("id", "")

    init {

        lifecycle.doOnStart(true) {
            componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
                loadData()
                repository.upSync().collect { upSync ->
                    when (upSync) {
                        is ApiResult.Success -> {
                            repository.sync().collect { sync ->
                                when (sync) {
                                    is ApiResult.Success -> {
                                        repository.saveSyncData(sync.data)
                                            .collectLatest { isDone ->
                                                if (isDone) {
//                                    _isLoading.value = false
                                                    loadData()
                                                }
                                            }
                                    }

                                    is ApiResult.Error -> withContext(Dispatchers.Main) {
//                        _isLoading.value = false
                                        loadData()
                                    }

                                    is ApiResult.Loading -> {}
                                }
                            }
                        }

                        is ApiResult.Error -> withContext(Dispatchers.Main) {
//                        _isLoading.value = false
                            loadData()
                        }

                        is ApiResult.Loading -> {}
                    }
                }
            }
        }
        lifecycle.doOnResume {
            componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
                loadData()
            }
        }
    }

    override fun onGroupClick(groupId: String) {
        onSpendClicked(groupId)
    }

    override fun onCreateGroupClicked() {
        onCreateGroupClicked.invoke()
    }

    suspend fun loadData() {
        val groupsList = arrayListOf<GroupWithOwes>()
        val balances = arrayListOf<Balance>()
        repository.getAllGroups().collect { g ->
            groupsList.addAll(g.map { GroupWithOwes(it, 0.0) })
            g.forEach { gId ->
                checkGroupSettlesAndSync(repository, gId.id).collect {

                }
            }
        }
        repository.getAllUsers().collect {
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
                repository.getSpendsAfterLastSettle(group.group.id)
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

    override fun onLogoutClick() {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            repository.upSync().collect {
                if (it !is ApiResult.Loading)
                    repository.logout().collect {
                        when (it) {
                            is ApiResult.Success -> {
                                withContext(Dispatchers.Main) {
                                    repository.clearDb().collect {
                                        if (it) {
                                            repository.getSettings().clear()
                                            onLogoutClick.invoke()
                                        }
                                    }
                                }
                            }

                            else -> {}
                        }
                    }
            }
        }
    }

    class Factory(
        val repository: Repository,
    ) : HomeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext, onSpendClicked: (groupId: String) -> Unit,
            onCreateGroupClicked: () -> Unit,
            onLogoutClick: () -> Unit,
            onFriendBalanceClick: (String, String) -> Unit
        ): HomeComponent {
            return DefaultHomeComponent(
                componentContext,
                onSpendClicked,
                onCreateGroupClicked,
                onLogoutClick,
                onFriendBalanceClick,
                repository
            )
        }
    }

}