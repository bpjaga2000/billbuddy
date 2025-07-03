package data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import constants.SplitType

data class EditSpendTabDetails(
    val userId: String,
    val name: String,
    val type: Int,
    val value: MutableState<String> = mutableStateOf(""),
    val onChecked: ((Boolean) -> Unit) = {
        if (!it)
            value.value = ""
        else if(type == SplitType.EQUAL)
            value.value = "1"
    },
)