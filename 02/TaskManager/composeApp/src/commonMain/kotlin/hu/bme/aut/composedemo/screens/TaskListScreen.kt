package hu.bme.aut.composedemo.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.bme.aut.composedemo.models.Task
import hu.bme.aut.composedemo.navigation.Screen
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun TaskListScreen(
    navController: NavController,
    tasks: List<Task>,
    onTasksUpdated: (List<Task>) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(tasks, key = { it.id }) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Screen.Details.createRoute(task.id)) }
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(task.title, modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = { isChecked ->
                                    onTasksUpdated(tasks.map {
                                        if (it.id == task.id) it.copy(isCompleted = isChecked) else it
                                    })
                                }
                            )
                            IconButton(onClick = {
                                taskToDelete = task
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    val newTask = Task(UUID.randomUUID().toString(), "New Task", false)
                    onTasksUpdated(tasks + newTask)
                }) {
                    Text("Add Task")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    navController.navigate(Screen.Settings.route)
                }) {
                    Text("Settings")
                }
            }
        }
    }

    if (showDialog && taskToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    onTasksUpdated(tasks.filter { it.id != taskToDelete!!.id })
                    showDialog = false
                    scope.launch {
                        val deletedTask = taskToDelete!! // Store deleted task separately
                        val updatedTasks = tasks.filter { it.id != deletedTask.id } // Remove task
                        onTasksUpdated(updatedTasks) // Update task list immediately

                        val result = snackbarHostState.showSnackbar(
                            message = "Task deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            // Restore task only if it was deleted
                            onTasksUpdated(updatedTasks + deletedTask)
                        }
                    }


                }) { Text("Delete") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("Cancel") }
            },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") }
        )
    }
}
