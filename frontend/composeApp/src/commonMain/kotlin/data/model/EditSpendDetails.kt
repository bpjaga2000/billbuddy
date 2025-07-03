package data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class EditSpendDetails(
    val userId: String,
    val name: String,
    val value: MutableState<String> = mutableStateOf("")
)
