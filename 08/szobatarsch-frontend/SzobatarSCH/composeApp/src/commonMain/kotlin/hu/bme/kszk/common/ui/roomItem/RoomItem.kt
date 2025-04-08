package hu.bme.kszk.common.ui.roomItem

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.bme.kszk.service.api.dto.rooms.RoomResponse


@Composable
fun RoomItem(room: RoomResponse, isExpanded: Boolean, onToggle: () -> Unit) {
    AnimatedContent(
        targetState = isExpanded,
        transitionSpec = {
            (fadeIn() + scaleIn()).togetherWith(fadeOut() + scaleOut())
        },
        label = "Room Expand Animation"
    ) { expanded ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .animateContentSize(animationSpec = spring( // ✅ Spring animation
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )), // ✅ Smooth height expansion/collapse
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp
        ) {
            if (expanded) {
                RoomItemExpanded(room, onToggle)
            } else {
                RoomItemCollapsed(room, onToggle)
            }
        }
    }
}