package presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun RegisterScreen(component: RegisterComponent, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.then(
            Modifier.fillMaxSize().background(MaterialTheme.colors.background)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier.weight(0.1f)
        ) {

        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.9f).background(
                color = Color.LightGray,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {}
    }
}