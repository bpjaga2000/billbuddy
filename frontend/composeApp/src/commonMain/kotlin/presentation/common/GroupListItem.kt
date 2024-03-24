package presentation.common

import androidx.compose.foundation.layout.Box
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

@Composable
fun GroupListItem(modifier: Modifier = Modifier) {
    val balance by remember { mutableIntStateOf(1) }
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.then(Modifier.padding(20.dp).wrapContentSize()),
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth()
        ) {
            Column {
                Text("Title")
                Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())
                if (balance > 0)
                    Text("You owe $100")
                else if (balance < 0)
                    Text("You are owed $100")
                else
                    Text("Settled")
                Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())
            }
        }
    }
}