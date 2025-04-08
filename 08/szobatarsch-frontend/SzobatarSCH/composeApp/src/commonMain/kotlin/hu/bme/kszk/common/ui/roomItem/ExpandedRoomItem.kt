package hu.bme.kszk.common.ui.roomItem

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
import hu.bme.kszk.common.ui.CircleAvatar
import hu.bme.kszk.common.ui.RotatingArrow
import hu.bme.kszk.service.api.dto.rooms.RoomResponse

@Composable
fun RoomItemExpanded(room: RoomResponse, onCollapse: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp).clickable { onCollapse() }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleAvatar(room.nickname)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = room.nickname, fontWeight = FontWeight.Bold)
                //Text(text = "${room.members}/${room.maxMembers}")
            }
            Spacer(modifier = Modifier.weight(1f))
            RotatingArrow(expanded = true) // ✅ Animated arrow
        }
        Spacer(modifier = Modifier.height(8.dp))
        room.roomDescription.let {
            Text(text = "Room description", fontWeight = FontWeight.SemiBold)
            Text(text = it, style = MaterialTheme.typography.body2)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = { /* Elrejtés */ }) {
                Text("Hide room")
            }
            Button(onClick = { /* Csatlakozás */ }) {
                Text("Join room")
            }
        }
    }

}
