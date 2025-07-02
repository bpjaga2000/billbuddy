package presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import data.model.dto.ProfileDto

@Composable
fun SearchMemberItem(
    member: ProfileDto,
    onCheckChanged: (userId: String, isChecked: Boolean) -> Unit
) {

    var isChecked by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Checkbox(isChecked, {
            isChecked = !isChecked
            onCheckChanged(member.id, it)
        })
        Text(member.email)
    }
}