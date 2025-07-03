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
import com.russhwolf.settings.get
import constants.SplitType
import data.SpendTags
import data.model.GroupMemberSplit
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import presentation.editspends.editspendstab.DefaultEditSpendsTabComponent
import presentation.editspends.editspendstab.EditSpendsTabComponent
import utils.DataStore
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
    val spentBy: MutableState<String>
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
    override val spentBy = mutableStateOf("")
    override val spentAt = mutableStateOf(Clock.System.now().epochSeconds)
    override var groupMembers: List<GroupMemberSplit> = listOf()
    private val navigation = PagesNavigation<Config>()
    override val groupName = mutableStateOf("")
    override var isValid = false

    init {
        runBlocking(Dispatchers.IO) {
            RepositoryImpl().getGroupById(groupId).collect {
                groupName.value = it.name
            }
            RepositoryImpl().getGroupMemberDetails(groupId).collect {
                groupMembers = it.map { member ->
                    GroupMemberSplit(
                        member.userId,
                        member.userName,
                        false,
                        0f
                    )
                }
            }
            spentBy.value =
                groupMembers.find { it.userId == DataStore.settings.get<String>("id") }?.userId
                    ?: groupMembers.first().userId
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
        val splitList =
            pageStack.value.items[pageStack.value.selectedIndex].instance?.splitDetails?.value?.filter { f ->
                f.isChecked.value
            }.orEmpty()
        if (pageStack.value.selectedIndex + 1 == SplitType.EQUAL)
            splitList.forEach {
                it.value.value = "1"
            }
        isValid = spendName.value.isNotBlank() &&
                (amount.value.toFloatOrNull() ?: 0f) != 0f &&
                spentBy.value.isNotBlank() &&
                spentAt.value != 0L &&
                when (pageStack.value.selectedIndex + 1) {
                    SplitType.EQUAL -> {
                        splitList.isNotEmpty()
                    }

                    SplitType.AMOUNT -> {
                        var total = 0f
                        splitList.forEach {
                            total = total + (it.value.value.toFloatOrNull() ?: 0f)
                        }
                        total == (amount.value.toFloatOrNull() ?: 0f)
                    }

                    SplitType.SHARE -> {
                        var total = 0f
                        splitList.forEach {
                            total = total + (it.value.value.toFloatOrNull() ?: 0f)
                        }
                        total > 0f
                    }

                    SplitType.RATIO -> {
                        var total = 0f
                        splitList.forEach {
                            total = total + (it.value.value.toFloatOrNull() ?: 0f)
                        }
                        total == 1f
                    }

                    SplitType.DIFFERENCE -> {
                        true
                    }

                    else -> false

                }

        if (isValid)
            componentContext.componentCoroutineScope().launch(Dispatchers.IO) {
                RepositoryImpl().saveSpend(
                    spendName.value,
                    amount.value,
                    spendTags.value,
                    groupId,
                    spentBy.value,
                    spentAt.value
                ).collect { id ->
                    if (id.isNotBlank())
                        RepositoryImpl().saveSpendSplits(
                            splitList,
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