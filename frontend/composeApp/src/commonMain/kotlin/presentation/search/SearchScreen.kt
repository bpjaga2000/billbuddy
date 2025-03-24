package presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import presentation.common.GroupMemberItem
import presentation.common.TextEdit

@Composable
fun SearchScreen(component: SearchComponent, modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        TextEdit(component.searchString)
        LazyColumn {
            items(
                component.searchResults.value.size,
                { it -> component.searchResults.value[it].userId }) { it ->
                GroupMemberItem(component.searchResults.value[it]) {
                    component.onSearchResultTapped(it)
                }
            }
        }
        TextButton(onClick = { component.onAddMoreClicked() }) {
            Text("Add More")
        }
    }

}