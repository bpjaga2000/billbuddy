package presentation.home

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import data.model.Balance
import data.repository.RepositoryImpl
import dev.bpj4.billbuddy.queries.GroupQueriesQueries
import dev.bpj4.billbuddy.tableandmigrations.Groups
import kotlinx.coroutines.launch
import utils.DispatcherUtils.componentCoroutineScope
import utils.getSqlDriver

interface HomeComponent {

    var groups: Value<List<Groups>>
    var friendBalances: Value<List<Balance>>

    fun onGroupClick(group: Groups?)

}

class DefaultHomeComponent(
    val componentContext: ComponentContext,
    val onSpendClicked: () -> Unit
) : HomeComponent, ComponentContext by componentContext {

    override var groups: Value<List<Groups>> = MutableValue(listOf())
    override var friendBalances: Value<List<Balance>> = MutableValue(listOf())

    init {
        componentContext.componentCoroutineScope().launch {
            RepositoryImpl().getGroups().collect {
                groups = MutableValue(it)
            }

            RepositoryImpl().getFriendBalances().collect {
                friendBalances = MutableValue(it)
            }
        }
    }

    override fun onGroupClick(group: Groups?) {
        onSpendClicked()
    }

}