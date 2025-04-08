package hu.bme.kszk.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
sealed class Screen(var userId: String?=null, @Transient val icon: ImageVector = Icons.Default.Search, val label: String) {
    @Serializable
    data object Rooms : Screen(icon = Icons.Default.Search, label = "Szobák")

    @Serializable
    data object MyRoom : Screen(icon = Icons.Default.Star, label = "Saját szoba")

    @Serializable
    data object Friends : Screen(icon =  Icons.Default.Person, label = "Haverok")

    @Serializable
    data object Notifications : Screen(icon = Icons.Default.Notifications, label = "Értesítések")

    @Serializable
    data object Profile : Screen(icon =  Icons.Default.Search, label = "Profil")
}


@Composable
fun FriendsScreen(navController: NavHostController, userId: String) {
    Text("Haverok képernyő\nUser ID: $userId", Modifier.fillMaxSize().padding(16.dp))
}

@Composable
fun NotificationsScreen(navController: NavHostController, userId: String) {
    Text("Értesítések képernyő\nUser ID: $userId", Modifier.fillMaxSize().padding(16.dp))
}

@Composable
fun ProfileScreen(navController: NavHostController, userId: String) {
    Text("Profil képernyő\nUser ID: $userId", Modifier.fillMaxSize().padding(16.dp))
}
