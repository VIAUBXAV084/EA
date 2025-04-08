package hu.bme.kszk.myRoom.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.OutlinedButton
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
import androidx.navigation.NavController
import hu.bme.kszk.common.ui.ShowToast
import hu.bme.kszk.service.api.dto.users.UserResponse
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyRoomScreenRoot(
    navController: NavController,
    userId: String,
    viewModel: MyRoomViewModel = koinViewModel()
){
    LaunchedEffect(Unit){
        viewModel.onAction(MyRoomScreenAction.SetUserId(userId))
    }

    MyRoomScreen(
        viewModel = viewModel,
        onAction = { action ->
            when(action) {
                is MyRoomScreenAction.GoBack -> navController.navigateUp()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyRoomScreen(viewModel: MyRoomViewModel = koinViewModel(), onAction: (MyRoomScreenAction) -> Unit = {}) {
    val state by viewModel.state.collectAsState()

    var selectedUser by remember { mutableStateOf<UserResponse?>(null) }
    val expanded = remember { mutableStateOf(false) }
    var showSuccessfulInvite by remember { mutableStateOf(false) }

    if (showSuccessfulInvite) {
        ShowToast("Sikeres meghívás")
        showSuccessfulInvite = false
    }

    if(state.isLoading){
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeWidth = 10.dp,
                color = Color.Gray
            )
        }
    }
    else if(state.errorMessage != null){
        Box(Modifier.fillMaxSize()) {
            Text(state.errorMessage!!.name, color = Color.Red, modifier = Modifier.align(Alignment.Center))
        }
    }
    else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item { Text("Saját szoba/tagok meghívása", style = MaterialTheme.typography.h6) }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Szoba neve
            state.room?.let { currentRoom ->
                item {
                    OutlinedTextField(
                        value = currentRoom.nickname,
                        onValueChange = { newName -> viewModel.onAction(MyRoomScreenAction.UpdateRoomName(newName)) },
                        label = { Text("Szoba neve") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.clickable { viewModel.onAction(MyRoomScreenAction.UpdateRoomName("")) })
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }


            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Rövid leírás
            state.room?.let { currentRoom ->
                item {
                    OutlinedTextField(
                        value = currentRoom.roomDescription,
                        onValueChange = { newDescription ->
                            viewModel.onAction(MyRoomScreenAction.UpdateRoomDescription(
                                newDescription
                            ))
                        },
                        label = { Text("Rövid leírás") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.clickable { viewModel.onAction(MyRoomScreenAction.UpdateRoomDescription("")) })
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Prefereált szobaszám
            state.room?.let { currentRoom ->
                item {
                    OutlinedTextField(
                        value = currentRoom.roomNumber?.toString() ?: "",
                        onValueChange = { newNumber-> newNumber.toIntOrNull()?.let {
                            viewModel.onAction(MyRoomScreenAction.UpdateRoomNumber(
                                newNumber.ifBlank { null })) } ?:
                        viewModel.onAction(MyRoomScreenAction.UpdateRoomNumber(currentRoom.roomNumber?.toString() ?: ""))
                        } ,
                        label = { Text("Szobaszám") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.clickable { viewModel.onAction(MyRoomScreenAction.UpdateRoomNumber(null)) })
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { Text("Tagok", fontWeight = FontWeight.Bold) }

            items(state.members){ user ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = "User")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(user.name)
                }
                Divider(modifier = Modifier.fillMaxWidth())
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                OutlinedButton(
                    onClick = { viewModel.saveModification() },
                ) {
                    Text("Változások mentése")
                }
            }

            item { Text("Új tag hozzáadása", fontWeight = FontWeight.Bold) }

            // Lenyíló lista a meghívható tagokkal
            item {
                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = !expanded.value }) {
                    OutlinedTextField(
                        value = selectedUser?.name ?: "",
                        onValueChange = {},
                        label = { Text("Válassz felhasználót") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }) {
                        state.invitableUsers.forEach { user ->
                            DropdownMenuItem(content = { Text(user.name) }, onClick = {
                                selectedUser = user
                                expanded.value = false
                                //viewModel.inviteUser(user) // Meghívás TODO
                                showSuccessfulInvite = true
                            })
                            Divider(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}
