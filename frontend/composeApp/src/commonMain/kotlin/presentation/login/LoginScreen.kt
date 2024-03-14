package presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var email = rememberTextFieldState("")
    var password = rememberTextFieldState("")
    val focusRequester = remember { FocusRequester() }
    val coroutine = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val pageSize = 4
    val pagerState = rememberPagerState { pageSize }
    Column(
        modifier = modifier.then(
            Modifier.fillMaxSize().background(MaterialTheme.colors.background)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier.weight(0.3f)
        ) {
            HorizontalPager(
                pagerState,
            ) {

                Image(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().align(Alignment.Center)
                )
            }
            Row(
                modifier = Modifier.align(Alignment.BottomCenter),//.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pageSize) {
                    Spacer(
                        modifier = Modifier.size(10.dp)
                            .border(
                                BorderStroke(
                                    5.dp,
                                    if (it == pagerState.currentPage) Color.Black else Color.LightGray
                                ),
                                shape = RoundedCornerShape(50.dp)
                            ).clickable {
                                coroutine.launch {
                                    pagerState.animateScrollToPage(it)
                                }
                            }
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.7f).background(
                color = Color.LightGray,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.weight(0.2f))

            BasicTextField2(
                email,
                decorator = {
                    AnimatedVisibility(email.text.isBlank()) {
                        Text(
                            "Enter your email",
                            color = Color.Blue,
                            modifier = Modifier.fillMaxHeight()
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                    it()
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(50.dp))
                    .width(300.dp)
                    .padding(vertical = 8.dp, horizontal = 30.dp)
                    .height(40.dp)
            )

            Spacer(modifier = Modifier.weight(0.1f))

            BasicSecureTextField(
                password,
                decorator = {
                    AnimatedVisibility(password.text.isEmpty()) {
                        Text("Enter your password")
                    }
                    it()
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(50.dp))
                    .width(300.dp)
                    .padding(vertical = 8.dp, horizontal = 30.dp)
                    .height(40.dp)
            )

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .weight(0.3f),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {}) {
                    Text("Register")
                }

                Button(onClick = {}) {
                    Text("Sign In")
                }
            }

            Spacer(modifier = Modifier.weight(0.2f))

        }
    }
}