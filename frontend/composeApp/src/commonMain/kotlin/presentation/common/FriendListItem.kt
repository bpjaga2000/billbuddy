package presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FriendListItem(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.then(
            Modifier.padding(4.dp)
                .background(Color.LightGray, RoundedCornerShape(32.dp))
                .padding(vertical = 8.dp, horizontal = 24.dp)
        ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Icon(Icons.Default.Person, contentDescription = null)
        Text("Friend")
        Text("Owes/owed $100")
    }
}