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
import data.model.EditSpendTabDetails
import data.model.GroupMemberSplit
import data.repository.RepositoryImpl
import dev.bpj4.billbuddy.tableandmigrations.SpendSplits
import dev.bpj4.billbuddy.tableandmigrations.Spends
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import presentation.editspends.editspendstab.DefaultEditSpendsTabComponent
import presentation.editspends.editspendstab.EditSpendsTabComponent
import utils.DataStore
import utils.DispatcherUtils.componentCoroutineScope
import utils.checkGroupSettlesAndSync
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface EditSpendsComponent {

    var pageStack: MutableState<Value<ChildPages<*, EditSpendsTabComponent>>?>
    var selection: MutableState<Int>
    val groupName: MutableState<String>
    val spendName: MutableState<String>
    val amount: MutableState<String>
    val spendTags: MutableState<SpendTags>
    val spentBy: MutableState<String>
    val spentAt: MutableState<Long>
    var isValid: Boolean
    var groupMembers: List<GroupMemberSplit>
    var spend: Spends?
    var spendSplit: List<SpendSplits>?
    fun onSaveClicked()
    fun onPageSelected(index: Int)
}

class DefaultEditSpendsComponent(
    private val componentContext: ComponentContext,
    private val groupId: String,
    private val spendId: String?,
    override var selection: MutableState<Int> = mutableStateOf(0),
    private val onSaved: () -> Unit
) : EditSpendsComponent, ComponentContext by componentContext {

    override val spendName = mutableStateOf("")
    override val amount = mutableStateOf("")
    override val spendTags = mutableStateOf<SpendTags>(SpendTags.OTHER)
    override val spentBy = mutableStateOf("")
    override val spentAt = mutableStateOf(Clock.System.now().epochSeconds)
    override var groupMembers: List<GroupMemberSplit> = listOf()
    override var spend: Spends? = null
    override var spendSplit: List<SpendSplits>? = null
    private val navigation = PagesNavigation<Config>()
    override val groupName = mutableStateOf("")
    override var isValid = false
    private var splitDetails: List<EditSpendTabDetails>? = null
    private var currentUser = DataStore.settings.get<String>("id")!!
    override var pageStack: MutableState<Value<ChildPages<*, EditSpendsTabComponent>>?> =
        mutableStateOf(null)

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
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
            spentBy.value = currentUser
            spentAt.value = Clock.System.now().epochSeconds
            if (spendId != null) {
                RepositoryImpl().getSpendAndSplitWithSpendId(spendId).collect {
                    spend = it.spend
                    spendSplit = it.splits
                    spendName.value = it.spend.name
                    amount.value = it.spend.totalAmount.toString()
                    spendTags.value = it.spend.tag
                    spentAt.value = it.spend.spentAt
                    spentBy.value = it.spend.spentBy
                    splitDetails =
                        it.splits.map { b ->
                            EditSpendTabDetails(
                                b.userId,
                                groupMembers.find { gm -> gm.userId == b.userId }!!.userName,
                                b.splitType.toInt(),
                                mutableStateOf(b.value_.toString())
                            )
                        }
                    selection.value = splitDetails!![0].type - 1
                    pageStack.value = getPageStack()
                }
            } else pageStack.value = getPageStack()
        }
    }


    override fun onSaveClicked() {
        pageStack.value?.let { pageStack ->
            val payeeList =
                pageStack.value.items[pageStack.value.selectedIndex].instance?.splitDetails?.value?.filter { f ->
                    f.value.value.isNotBlank()
                }.orEmpty()
            val splitType = pageStack.value.selectedIndex + 1


            isValid = spendName.value.isNotBlank() &&
                    (amount.value.toDoubleOrNull() ?: 0f) != 0f &&
                    spentBy.value.isNotBlank() &&
                    spentAt.value != 0L &&
                    when (splitType) {
                        SplitType.EQUAL -> {
                            payeeList.isNotEmpty()
                        }

                        SplitType.AMOUNT -> {
                            var total = 0.0
                            payeeList.forEach {
                                total = total + (it.value.value.toDoubleOrNull() ?: 0.0)
                            }
                            total == (amount.value.toDoubleOrNull() ?: 0f)
                        }

                        SplitType.SHARE -> {
                            var total = 0.0
                            payeeList.forEach {
                                total = total + (it.value.value.toDoubleOrNull() ?: 0.0)
                            }
                            total > 0.0
                        }

                        SplitType.RATIO -> {
                            var total = 0.0
                            payeeList.forEach {
                                total = total + (it.value.value.toDoubleOrNull() ?: 0.0)
                            }
                            total == 1.0
                        }

                        SplitType.DIFFERENCE -> {
                            true
                        }

                        else -> false

                    }


            if (!isValid)
            //TODO toast
                return

            componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
                if (spendId == null) {
                    RepositoryImpl().saveSpend(
                        spendName.value,
                        amount.value,
                        spendTags.value,
                        groupId,
                        spentAt.value,
                        spentBy.value
                    ).collect { id ->
                        if (id.isNotBlank())
                            RepositoryImpl().saveSpendSplits(
                                payeeList,
                                id,
                                groupId,
                                splitType,
                            ).collect {
                                if (it) {
                                    checkGroupSettlesAndSync(groupId).collect {
                                    }
                                    withContext(Dispatchers.Main) {
                                        onSaved()
                                    }
                                }
                            }
                    }
                } else {

                    var isDifferent = false
                    if (!isDifferent)
                        spendSplit!!.forEach { b ->
                            if (!isDifferent) {
                                val new = payeeList.find { it.userId == b.userId }
                                isDifferent =
                                    isDifferent || new == null || b.value_ != new.value.value.toDoubleOrNull() || b.splitType != splitType.toLong()
                            }
                        }
                    if (isDifferent || spend!!.name != spendName.value || spend!!.totalAmount != amount.value.toDoubleOrNull() || spend!!.tag != spendTags.value || spend!!.spentAt != spentAt.value || spend!!.spentBy != spentBy.value) {
                        RepositoryImpl().groupSettleCorrection(groupId, spend!!.updatedAt).collect {
                            RepositoryImpl().updateSpend(
                                spend!!.copy(
                                    name = spendName.value,
                                    totalAmount = amount.value.toDoubleOrNull()
                                        ?: spend!!.totalAmount,
                                    tag = spendTags.value,
                                    spentAt = spentAt.value,
                                    updatedAt = Clock.System.now().epochSeconds,
                                    updatedBy = currentUser
                                )
                            ).collect {
                                if (isDifferent) {
                                    RepositoryImpl().updateSpendSplits(
                                        payeeList,
                                        splitType,
                                        spend!!
                                    ).collect {
                                        if (it) {
                                            checkGroupSettlesAndSync(groupId).collect {
                                            }
                                            withContext(Dispatchers.Main) {
                                                onSaved()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPageSelected(index: Int) {
        selection.value = index
        navigation.select(index)
    }

    private fun getPageStack() = childPages(
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
            splitDetails =
                if (spendId != null)
                    mutableStateOf(splitDetails!!)
                else mutableStateOf(List(groupMembers.size) { it ->
                    EditSpendTabDetails(
                        groupMembers[it].userId,
                        groupMembers[it].userName,
                        config.type
                    )
                }),
            type = config.type,
            amount = amount.value,
        )

    }

    @Serializable
    private data class Config(val type: Int)
}