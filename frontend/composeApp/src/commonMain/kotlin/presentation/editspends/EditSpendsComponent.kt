@file:OptIn(ExperimentalTime::class)

package presentation.editspends

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import constants.SplitType
import data.SpendTags
import data.model.EditSpendDetails
import data.model.GroupMemberSplit
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import presentation.editspends.editspendstab.DefaultEditSpendsTabComponent
import presentation.editspends.editspendstab.EditSpendsTabComponent
import utils.DispatcherUtils.componentCoroutineScope
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface EditSpendsComponent {

    val pageStack: Value<ChildPages<*, EditSpendsTabComponent>>
    var selection: MutableState<Int>
    val groupName: MutableState<String>
    val spendName: MutableState<String>
    val amount: MutableState<String>
    val spendTags: MutableState<SpendTags>
    val spentBy: MutableState<List<EditSpendDetails>>
    val spentAt: MutableState<Long>
    var isValid: Boolean
    var groupMembers: List<GroupMemberSplit>
    fun onSaveClicked()
    fun onPageSelected(index: Int)
}

class DefaultEditSpendsComponent(
    private val componentContext: ComponentContext,
    private val groupId: String,
    override var selection: MutableState<Int> = mutableStateOf(0),
    private val onSaved: () -> Unit
) : EditSpendsComponent, ComponentContext by componentContext {

    override val spendName = mutableStateOf("")
    override val amount = mutableStateOf("")
    override val spendTags = mutableStateOf<SpendTags>(SpendTags.OTHER)
    override val spentBy = mutableStateOf(listOf<EditSpendDetails>())
    override val spentAt = mutableStateOf(Clock.System.now().epochSeconds)
    override var groupMembers: List<GroupMemberSplit> = listOf()
    private val navigation = PagesNavigation<Config>()
    override val groupName = mutableStateOf("")
    override var isValid = false

    init {
        runBlocking(Dispatchers.Default) {
            RepositoryImpl().getGroupById(groupId).collect {
                groupName.value = it.name
            }
            RepositoryImpl().getGroupMemberDetails(groupId).collect {
                groupMembers = it.map { member ->
                    GroupMemberSplit(
                        member.userId,
                        member.userName,
                        false,
                        0.0
                    )
                }
            }
            spentBy.value = groupMembers.map { member -> EditSpendDetails(member.userId, member.userName) }
            spentAt.value = Clock.System.now().epochSeconds
        }
    }

    override val pageStack: Value<ChildPages<*, EditSpendsTabComponent>> = childPages(
        source = navigation,
        serializer = Config.serializer(),
        initialPages = {
            Pages(
                items = List(5) { index -> Config(index + 1) },
                selectedIndex = selection.value
            )
        }
    ) { config, childComponentContext ->
        DefaultEditSpendsTabComponent(
            componentContext = childComponentContext,
            peopleInvolved = groupMembers,
            type = config.type,
            amount = amount.value
        )

    }

    override fun onSaveClicked() {
        val lenderList = spentBy.value.filter { f -> f.value.value.isNotBlank() }
        val borrowerList =
            pageStack.value.items[pageStack.value.selectedIndex].instance?.splitDetails?.value?.filter { f ->
                f.value.value.isNotBlank()
            }.orEmpty()
        isValid = spendName.value.isNotBlank() &&
                (amount.value.toDoubleOrNull() ?: 0f) != 0f &&
                lenderList.isNotEmpty() &&
                lenderList.sumOf { it.value.value.toDoubleOrNull() ?: 0.0 } == amount.value.toDoubleOrNull() &&
                spentAt.value != 0L &&
                when (pageStack.value.selectedIndex + 1) {
                    SplitType.EQUAL -> {
                        borrowerList.isNotEmpty()
                    }

                    SplitType.AMOUNT -> {
                        var total = 0.0
                        borrowerList.forEach {
                            total = total + (it.value.value.toDoubleOrNull() ?: 0.0)
                        }
                        total == (amount.value.toDoubleOrNull() ?: 0f)
                    }

                    SplitType.SHARE -> {
                        var total = 0.0
                        borrowerList.forEach {
                            total = total + (it.value.value.toDoubleOrNull() ?: 0.0)
                        }
                        total > 0.0
                    }

                    SplitType.RATIO -> {
                        var total = 0.0
                        borrowerList.forEach {
                            total = total + (it.value.value.toDoubleOrNull() ?: 0.0)
                        }
                        total == 1.0
                    }

                    SplitType.DIFFERENCE -> {
                        true
                    }

                    else -> false

                }

        if (isValid)
            componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
                RepositoryImpl().saveSpend(
                    spendName.value,
                    amount.value,
                    spendTags.value,
                    groupId,
                    spentAt.value
                ).collect { id ->
                    if (id.isNotBlank())
                        RepositoryImpl().saveSpendSplits(
                            lenderList,
                            borrowerList,
                            id,
                            groupId,
                            pageStack.value.selectedIndex + 1
                        ).collect {
                            if (it)
                                withContext(Dispatchers.Main) {
                                    onSaved()
                                }
                        }
                }
            }
        //TODO else
    }

    override fun onPageSelected(index: Int) {
        selection.value = index
        navigation.select(index)
    }

    @Serializable
    private data class Config(val type: Int)
}