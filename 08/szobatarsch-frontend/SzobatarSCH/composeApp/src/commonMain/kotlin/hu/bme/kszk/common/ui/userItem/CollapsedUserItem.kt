package hu.bme.kszk.common.ui.userItem

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.kszk.common.ui.CircleAvatar
import hu.bme.kszk.common.ui.RotatingArrow
import hu.bme.kszk.service.api.dto.users.UserResponse


@Composable
fun UserItemCollapsed(user: UserResponse, onExpand: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onExpand() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleAvatar(user.name)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = user.nickname, style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.weight(1f))
        RotatingArrow(expanded = false) // ✅ Animated arrow
    }
}

@Composable
@Preview
fun UserItemCollapsedPreview(){
    UserItemCollapsed(UserResponse(
        userUuid = "1",
        schacc = "user123",
        name = "Kovács Péter",
        email = "peter.kovacs@example.com",
        room = null,
        nickname = "Peti",
        profileDescription = "Szeretek sportolni és olvasni.",
        gender = "MALE",
        major = "Informatika",
        wantsCoedRoom = true,
        isMentor = false,
        authSchId = "123456",
        isSearchingRoom = false
    )){}
}