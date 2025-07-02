package presentation.creategroup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import data.GroupTags
import presentation.common.TextEdit

@Composable
fun CreateGroupScreen(component: CreateGroupComponent, modifier: Modifier = Modifier) {
    val groupName = remember { mutableStateOf("") }
    var groupTag by remember { mutableStateOf(GroupTags.OTHER) }
    var expansion by remember { mutableStateOf(true) }
    val isLoading by component.isLoading
    Box {
        Column(
            modifier = modifier.then(Modifier.fillMaxSize()),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextEdit(groupName, "Group Name", editable = true)
            Column {
                Text(
                    modifier = modifier
                        .width(300.dp)
                        .height(40.dp)
                        .border(
                            BorderStroke(1.dp, Color.Gray),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable { expansion = !expansion },
                    textAlign = TextAlign.Center,
                    text = groupTag.toString()
                )
                DropdownMenu(expanded = expansion, onDismissRequest = {
                    expansion = false
                }) {
                    repeat(GroupTags.entries.size) {
                        DropdownMenuItem(
                            {
                                Text(GroupTags.entries[it].toString())
                            },
                            {
                                groupTag = GroupTags.entries[it]
                                expansion = false
                            }
                        )
                    }
                }
            }
            TextButton({ component.createGroup(groupName.value, groupTag) }) {
                Text("Create Group")
            }
        }
        if (isLoading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).size(50.dp))
    }
}