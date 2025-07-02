@file:OptIn(ExperimentalFoundationApi::class)

package presentation.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import data.model.Balance

@Composable
fun BalanceItem(lender: Balance, borrowers: List<Balance>, modifier: Modifier = Modifier) {

    val isCollapsed = mutableStateOf(true)

    Row {

        Icon(
            imageVector = if (isCollapsed.value) Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
            contentDescription = null
        )

        Text(
            "${lender.name} is owed ₹${lender.amount}",
            Modifier.clickable { isCollapsed.value = !isCollapsed.value }
        )

    }

    if (!isCollapsed.value)
        LazyColumn {
            items(count = borrowers.size, key = { index -> borrowers[index].userId }) { index ->
                Text("${borrowers[index].name} owes ₹${borrowers[index].amount}")
            }
        }

}