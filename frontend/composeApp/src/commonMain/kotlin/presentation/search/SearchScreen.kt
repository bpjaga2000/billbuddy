package presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import presentation.common.SearchMemberItem
import presentation.common.TextEdit

@Composable
fun SearchScreen(component: SearchComponent, modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        TextEdit(
            component.searchString,
            onValueChange = { component.onSearchClicked(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        LazyColumn {
            items(
                component.searchResults.value.size,
                { it -> component.searchResults.value[it].id }) { it ->
                SearchMemberItem(component.searchResults.value[it]) { it, isChecked ->
                    component.onSearchResultTapped(it, isChecked)
                }
            }
        }
        TextButton(onClick = { component.onAddMoreClicked() }) {
            Text("Add More")
        }
    }

}