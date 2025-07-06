package presentation.editspends

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import data.SpendTags
import io.ktor.util.date.GMTDate
import presentation.common.TextEdit
import presentation.editspends.editspendstab.EditSpendsTab

@OptIn(
    ExperimentalDecomposeApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun EditSpendsScreen(component: EditSpendsComponent, modifier: Modifier = Modifier) {

    val spendName = remember { component.spendName }
    val groupName by remember { component.groupName }
    val spendTags by remember { component.spendTags }
    val spentAt by remember { component.spentAt }
    var spentBy by remember { component.spentBy }
    val amount = remember { component.amount }
    val selection by remember { component.selection }
    val datePickerState = rememberDatePickerState()
    var isSpendTagExpanded by remember { mutableStateOf(false) }
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isSpendByExpanded by remember { mutableStateOf(false) }
    val types = listOf("Equal", "Amount", "Share", "Ratio", "Difference")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(groupName)
        TextEdit(spendName, "Spend name")
        TextEdit(amount, "Amount")
        Column(
            Modifier
                .width(300.dp)
                .height(40.dp)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(50.dp)
                )
                .clickable { isSpendTagExpanded = !isSpendTagExpanded },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = spendTags.toString()
            )
            DropdownMenu(
                isSpendTagExpanded,
                { isSpendTagExpanded = false },
                modifier = Modifier.width(280.dp).align(Alignment.CenterHorizontally)
            ) {
                repeat(SpendTags.entries.size) {
                    DropdownMenuItem(
                        {
                            Text(SpendTags.entries[it].toString())
                        },
                        {
                            component.spendTags.value = SpendTags.entries[it]
                            isSpendTagExpanded = false
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
        Column(
            Modifier
                .width(300.dp)
                .height(40.dp)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(50.dp)
                )
                .clickable { isSpendByExpanded = !isSpendByExpanded },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = component.groupMembers.find { it.userId == spentBy }!!.userName
            )
            DropdownMenu(
                isSpendByExpanded,
                { isSpendByExpanded = false },
                modifier = Modifier.width(280.dp).align(Alignment.CenterHorizontally)
            ) {
                repeat(component.groupMembers.size) {
                    DropdownMenuItem(
                        {
                            Text(component.groupMembers[it].userName)
                        },
                        {
                            component.spentBy.value = component.groupMembers[it].userId
                            isSpendByExpanded = false
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Text(
            text = GMTDate(spentAt).toString(),
            Modifier.width(300.dp)
                .height(40.dp)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(50.dp)
                ).clickable { isDatePickerVisible = !isDatePickerVisible })
        AnimatedVisibility(isDatePickerVisible) {
            DatePickerDialog(
                { isDatePickerVisible = false },
                {
                    TextButton({
                        isDatePickerVisible = false
                        component.spentAt.value = datePickerState.selectedDateMillis!!
                    }) { Text("Confirm") }
                }
            ) {
                DatePicker(datePickerState)
            }
        }

        ScrollableTabRow(
            selection,
            Modifier.fillMaxWidth(),
            edgePadding = 0.dp
        ) {
            types.forEachIndexed { index, title ->
                Tab(
                    selection == index,
                    onClick = { component.onPageSelected(index) },
                    text = { Text(types[index]) },
                    modifier = Modifier.padding(0.dp).align(Alignment.CenterHorizontally)
                )
            }
        }
        ChildPages(
            pages = component.pageStack,
            onPageSelected = component::onPageSelected,
            scrollAnimation = PagesScrollAnimation.Default
        ) { _, page ->
            EditSpendsTab(page)
        }
    }

}
