package presentation.editspends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import presentation.common.TextEdit
import presentation.editspends.editspendstab.EditSpendsTab

@OptIn(ExperimentalDecomposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun EditSpendsScreen(component: EditSpendsComponent, modifier: Modifier = Modifier) {

    val spendName = remember { mutableStateOf("") }
    val amount = remember { component.amount }
    val types = listOf("Equal", "Amount", "Share", "Ratio", "Difference")
    val selection by remember { component.selection }

    Column {
        Text("Group Name")
        TextEdit(spendName, "Spend name")
        TextEdit(amount, "Amount")

        ScrollableTabRow(
            selection,
            modifier.fillMaxWidth()
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
