package presentation.editspends.editspendstab

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import presentation.common.EditSpendItem

@Composable
fun EditSpendsTab(component: EditSpendsTabComponent, modifier: Modifier = Modifier) {
    LazyColumn {
        items(component.splitDetails.value.size, { it -> component.splitDetails.value[it].userId }) {
            EditSpendItem(component.splitDetails.value[it], component.type)
        }
    }
}
