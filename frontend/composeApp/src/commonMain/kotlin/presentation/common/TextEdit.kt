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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import utils.allRegex
import utils.amountRegex
import utils.emailRegex
import utils.filter
import utils.nameRegex
import utils.numberRegex

@Composable
fun TextEdit(
    text: MutableState<String>,
    placeholder: String = "",
    onValueChange: (String) -> Unit = {},
    editable: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = text.value,
        onValueChange = {
            filter(
                it, when (keyboardOptions.keyboardType) {
                    KeyboardType.Email -> emailRegex
                    KeyboardType.Number -> numberRegex
                    KeyboardType.Decimal -> amountRegex
                    KeyboardType.Ascii -> nameRegex
                    else -> allRegex
                }
            ).let { filtered ->
                onValueChange(filtered)
                text.value = filtered
            }
        },
        keyboardOptions = keyboardOptions,
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
        visualTransformation = visualTransformation,
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