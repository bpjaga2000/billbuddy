package presentation.home

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.bpj4.billbuddy.queries.GroupQueriesQueries
import dev.bpj4.billbuddy.tableandmigrations.Groups
import utils.getSqlDriver

interface HomeComponent {

    fun fetchGroups(): Value<List<Groups>>

    fun onGroupClick(group: Groups?)

}

class DefaultHomeComponent(
    val componentContext: ComponentContext,
    val onSpendClicked: () -> Unit
) : HomeComponent, ComponentContext by componentContext {

    override fun fetchGroups(): Value<List<Groups>> =
        MutableValue(getSqlDriver()?.let {
            GroupQueriesQueries(
                it, Groups.Adapter(
                    EnumColumnAdapter(),
                    IntColumnAdapter
                )
            ).selectAllGroups().executeAsList()
        } ?: listOf())

    override fun onGroupClick(group: Groups?) {
        onSpendClicked()
    }

}