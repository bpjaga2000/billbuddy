package data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class EditSpendTabDetails(
    val userId: String = "",
    val name: String = "",
    val isChecked: MutableState<Boolean> = mutableStateOf(false),
    val value: MutableState<String> = mutableStateOf(""),
    val onChecked: ((Boolean) -> Unit) = {
        isChecked.value = it
        if (!it) value.value = ""
    },
)