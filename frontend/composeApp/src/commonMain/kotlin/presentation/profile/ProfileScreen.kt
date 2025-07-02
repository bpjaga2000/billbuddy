package presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import presentation.common.TextEdit

@Composable
fun ProfileScreen(component: ProfileComponent, modifier: Modifier = Modifier) {

    var editable by remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }

    Box(
        modifier = modifier.then(Modifier.fillMaxSize()),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null,
            modifier = Modifier.padding(10.dp).size(125.dp).zIndex(1.5f)
        )
        Column(
            modifier = Modifier.padding(top = 100.dp).fillMaxSize().drawBehind {
                drawPath(
                    Path().apply {
                        addArc(Rect(Offset(0f, 0f), Size(size.width, 300f)), 0f, -180f)
                        lineTo(0f, size.height)
                        lineTo(size.width, size.height)
                        lineTo(size.width, 300 - size.height)
                    },
                    color = Color.LightGray,
                )
            }.padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ) {
            TextEdit(name, "Name", editable)
            TextEdit(email, "Email", editable)
            TextEdit(password, "Password", editable)
            TextEdit(phone, "Phone", editable)
        }

        if (!editable)
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(35.dp),
                onClick = {
                    editable = true
                },
                content = {
                    Icon(
                        imageVector = Icons.Sharp.Edit, contentDescription = "edit profile"
                    )
                }
            )
        else
            TextButton(
                modifier = Modifier.align(Alignment.BottomCenter).padding(20.dp)
                    .background(Color.Cyan, RoundedCornerShape(24.dp)).fillMaxWidth(),
                onClick = {
                    editable = false
//                    component.onEdit()
                },
                content = {
                    Text("Save")
                })
    }

}