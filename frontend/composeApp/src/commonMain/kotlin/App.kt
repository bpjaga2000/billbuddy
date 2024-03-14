import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.login.LoginScreen
import ui.BillBuddyTheme

@Composable
@Preview
fun App() {
    BillBuddyTheme {
        LoginScreen()
    }
}