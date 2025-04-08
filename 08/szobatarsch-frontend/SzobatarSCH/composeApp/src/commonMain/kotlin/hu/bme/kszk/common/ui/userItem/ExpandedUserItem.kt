package hu.bme.kszk.common.ui.userItem

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
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
fun UserItemExpanded(user: UserResponse, onCollapse: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp).clickable { onCollapse() }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleAvatar(user.name)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = user.nickname, style = MaterialTheme.typography.body2)
            }
            Spacer(modifier = Modifier.weight(1f))
            RotatingArrow(expanded = true) // ✅ Animated arrow
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Profile description:", fontWeight = FontWeight.SemiBold)
        Text(text = user.profileDescription, style = MaterialTheme.typography.body2)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = { /* Profile */ }) {
                Text("Profile")
            }
            Button(onClick = { /* Add bro */ }) {
                Text("Add bro")
            }
        }
    }
}

@Composable
@Preview
fun UserItemExpandedPreview(){
    UserItemExpanded(UserResponse(
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