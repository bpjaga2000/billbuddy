package presentation.register

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun RegisterScreen(component: RegisterComponent, modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordReenter by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val pageSize = 4
    val pagerState = rememberPagerState { pageSize }
    val coroutine = rememberCoroutineScope()


    Column(
        modifier = modifier.then(
            Modifier.imePadding().background(MaterialTheme.colors.background)
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

            BasicTextField(
                value = email,
                onValueChange = { email = it },
                decorationBox = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (email.isEmpty()) {
                            Text("Enter your email")
                        }
                        it()
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .width(300.dp)
                    .padding(vertical = 8.dp, horizontal = 30.dp)
                    .height(40.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.weight(0.1f))

            BasicTextField(
                value = password,
                onValueChange = { password = it },
                decorationBox = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (password.isEmpty()) {
                            Text("Enter your password")
                        }
                        it()
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .width(300.dp)
                    .padding(vertical = 8.dp, horizontal = 30.dp)
                    .height(40.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.weight(0.1f))

            BasicTextField(
                value = passwordReenter,
                onValueChange = { passwordReenter = it },
                decorationBox = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (passwordReenter.isEmpty()) {
                            Text("Re-enter your password")
                        }
                        it()
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .width(300.dp)
                    .padding(vertical = 8.dp, horizontal = 30.dp)
                    .height(40.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    component.onRegisterClicked(
                        email,
                        password
                    )
                })
            )

            Spacer(modifier = Modifier.weight(0.1f))

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .weight(0.1f),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    component.onLoginClicked()
                }) {
                    Text("Sign In")
                }

                Button(onClick = {
                    component.onRegisterClicked(email, password)
                }) {
                    Text("Register")
                }
            }
            Spacer(modifier = Modifier.weight(0.2f))

        }
    }
}