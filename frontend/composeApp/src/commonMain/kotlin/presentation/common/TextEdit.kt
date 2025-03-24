package presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TextEdit(
    text: MutableState<String>,
    placeholder: String = "",
    editable: Boolean = true,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = text.value,
        onValueChange = { text.value = it },
        enabled = editable,
        decorationBox = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (text.value.isEmpty()) {
                    Text(placeholder)
                }
                it()
            }
        },
        modifier = modifier.then(
            Modifier
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(50.dp)
                )
                .width(300.dp)
                .padding(vertical = 8.dp, horizontal = 30.dp)
                .height(40.dp)
        )
    )
}