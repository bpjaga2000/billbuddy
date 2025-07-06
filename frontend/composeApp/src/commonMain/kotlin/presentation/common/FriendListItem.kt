package presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.model.Balance
import kotlin.math.absoluteValue

@Composable
fun FriendListItem(balance: Balance, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.then(
            Modifier
                .clickable { onClick() }
                .padding(4.dp)
                .background(Color.LightGray, RoundedCornerShape(32.dp))
                .padding(vertical = 4.dp, horizontal = 16.dp)
                .width(100.dp)
        ),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Person, contentDescription = null)
        Column(
            Modifier.padding(4.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(balance.name, maxLines = 1)
            Text(
                balance.amount.absoluteValue.toString(),
                color = if (balance.amount > 0) Color.Green else Color.Red,
                maxLines = 1
            )
        }
    }
}