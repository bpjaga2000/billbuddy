package presentation.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import data.model.Balance
import data.remote.ApiResult
import data.repository.RepositoryImpl
import dev.bpj4.billbuddy.tableandmigrations.Groups
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DataStore
import utils.DispatcherUtils.componentCoroutineScope

interface HomeComponent {

    var groups: MutableValue<List<Groups>>
    var friendBalances: MutableValue<List<Balance>>

    fun onGroupClick(groupId: String)
    fun onCreateGroupClicked()

}

class DefaultHomeComponent(
    val componentContext: ComponentContext,
    private val onSpendClicked: (groupId: String) -> Unit,
    private val onCreateGroupClicked: () -> Unit
) : HomeComponent, ComponentContext by componentContext {

    override var groups: MutableValue<List<Groups>> = MutableValue(listOf())
    override var friendBalances: MutableValue<List<Balance>> = MutableValue(listOf())

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
            RepositoryImpl().sync(DataStore.settings.getString("id", "")).collect { sync ->
                when (sync) {
                    is ApiResult.Success -> {
                        RepositoryImpl().saveSyncData(sync.data).collectLatest { isDone ->
                            if (isDone) {
//                                    _isLoading.value = false
                                RepositoryImpl().getGroups().collect { g ->
                                    groups.update { g  }
                                }

                                RepositoryImpl().getFriendBalances().collect { f ->
                                    friendBalances.update { f }
                                }
                            }
                        }
                    }

                    is ApiResult.Error -> withContext(Dispatchers.Main) {
//                        _isLoading.value = false
                    }

                    is ApiResult.Loading -> {}
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

}