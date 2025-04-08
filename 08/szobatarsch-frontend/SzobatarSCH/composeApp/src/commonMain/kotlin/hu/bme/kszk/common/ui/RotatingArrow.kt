package hu.bme.kszk.common.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import kotlinx.coroutines.delay

@Composable
fun RotatingArrow(expanded: Boolean, modifier: Modifier = Modifier) {
    var delayedExpanded by remember { mutableStateOf(expanded) }

    // Introduce a delay before starting the animation
    LaunchedEffect(expanded) {
        delayedExpanded = !expanded
        delay(300) // ✅ Delay animation start by 300ms
        delayedExpanded = expanded
    }

    val rotation by animateFloatAsState(
        targetValue = if (delayedExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "Arrow Rotation"
    )

    Icon(
        imageVector = Icons.Default.ArrowDropDown,
        contentDescription = if (expanded) "Collapse" else "Expand",
        modifier = modifier.rotate(rotation)
    )
}


@Preview
@Composable
fun RotatingArrowUpPreview(){
    RotatingArrow(true)
}

@Preview
@Composable
fun RotatingArrowDownPreview(){
    RotatingArrow(false)
}