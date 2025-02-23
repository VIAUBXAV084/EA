package hu.bme.aut.composedemo.navigation

sealed class Screen(val route: String) {
    object TaskList: Screen("taskList")
    object Settings: Screen("settings")
    object Details: Screen("taskDetails/{taskId}") {
        fun createRoute(taskId: String) = "taskDetails/$taskId"
    }
}