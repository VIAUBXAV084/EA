package hu.bme.kszk.discover.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import hu.bme.kszk.common.ui.RotatingArrow
import hu.bme.kszk.common.ui.roomItem.RoomItem
import hu.bme.kszk.common.ui.userItem.UserItem
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import szobatarsch.composeapp.generated.resources.Res
import szobatarsch.composeapp.generated.resources.potential_people
import szobatarsch.composeapp.generated.resources.potential_rooms

@Composable
fun DiscoverScreenRoot(
    navController: NavHostController,
    userId: String,
    viewModel: DiscoverViewModel = koinViewModel()
){
    LaunchedEffect(Unit){
        viewModel.onAction(DiscoverScreenAction.SetUserId(userId))
    }

    DiscoverScreen(
        viewModel = viewModel,
        onAction = { action ->
            when(action) {
                is DiscoverScreenAction.GoBack -> navController.navigateUp()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )


}

@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel = koinViewModel(), onAction: (DiscoverScreenAction) -> Unit = {}) {
    val state by viewModel.state.collectAsState()


    var expandedRoomId by remember { mutableStateOf<String?>(null) }
    var expandedUserId by remember { mutableStateOf<String?>(null) }

    var expandedRoomSection by remember { mutableStateOf(false) }
    var expandedUserSection by remember { mutableStateOf(false) }

    if(state.isLoading){
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeWidth = 10.dp,
                color = Color.Gray,
            )
        }
    }
    else if(state.errorMessage != null){
        Box(Modifier.fillMaxSize()) {
            Text(state.errorMessage!!.name, color = Color.Red, modifier = Modifier.align(Alignment.Center))
        }
    }
    else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Discover", fontWeight = FontWeight.Bold)
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { Divider(modifier = Modifier.fillMaxWidth()) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedRoomSection = !expandedRoomSection }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(Res.string.potential_rooms),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).weight(1f)
                    )
                    RotatingArrow(expanded = expandedRoomSection) // ✅ Animated arrow
                }
            }

            item { Divider(modifier = Modifier.fillMaxWidth()) }

            if (expandedRoomSection) {
                items(state.rooms) { room ->
                    RoomItem(
                        room = room,
                        isExpanded = expandedRoomId == room.roomUUid,
                        onToggle = {
                            expandedRoomId =
                                if (expandedRoomId == room.roomUUid) null else room.roomUUid
                        }
                    )
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }

            item { Divider(modifier = Modifier.fillMaxWidth()) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedUserSection = !expandedUserSection }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(Res.string.potential_people),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).weight(1f)
                    )
                    RotatingArrow(expanded = expandedUserSection) // ✅ Animated arrow
                }
            }


            if (expandedUserSection) {
                items(state.users) { user ->
                    UserItem(
                        user = user,
                        isExpanded = expandedUserId == user.userUuid,
                        onToggle = {
                            expandedUserId =
                                if (expandedUserId == user.userUuid) null else user.userUuid
                        }
                    )
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
