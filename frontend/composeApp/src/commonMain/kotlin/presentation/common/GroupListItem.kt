package presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.GroupWithOwes
import kotlin.math.absoluteValue

@Composable
fun GroupListItem(group: GroupWithOwes, onGroupClicked: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.then(
            Modifier.padding(vertical = 8.dp).wrapContentSize().clickable { onGroupClicked() }),
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth()
        ) {
            Column {
                Text(group.group.name)
                Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())
                if (group.owe > 0)
                    Text("You owe ${group.owe}")
                else if (group.owe < 0)
                    Text("You are owed ${group.owe.absoluteValue}")
                else
                    Text("Settled")
                Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())
            }
        }
    }
}