package hu.bme.aut.composedemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.composedemo.models.Task
import hu.bme.aut.composedemo.navigation.Screen
import hu.bme.aut.composedemo.screens.SettingsScreen
import hu.bme.aut.composedemo.screens.TaskDetailsScreen
import hu.bme.aut.composedemo.screens.TaskListScreen

@Composable
fun App() {
    val navController = rememberNavController()
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isDarkMode by remember { mutableStateOf(false) } // Move state here

    NavHost(navController = navController, startDestination = Screen.TaskList.route) {
        composable(Screen.TaskList.route) {
            TaskListScreen(navController, tasks) { updatedTasks ->
                tasks = updatedTasks
            }
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController, isDarkMode) { newMode ->
                isDarkMode = newMode // Update state here
            }
        }
        composable(Screen.Details.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: return@composable
            TaskDetailsScreen(taskId, navController, tasks) { updatedTask ->
                tasks = tasks.map { if (it.id == updatedTask.id) updatedTask else it }
            }
        }
    }
}

