package hu.bme.kszk.screen


import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import hu.bme.kszk.discover.ui.DiscoverScreenRoot
import hu.bme.kszk.myRoom.ui.MyRoomScreenRoot
import hu.bme.kszk.myRoom.ui.MyRoomViewModel
import hu.bme.kszk.service.api.ApiServiceImpl
import hu.bme.kszk.service.api.ApiServiceInterface
import hu.bme.kszk.service.api.getHttpClientEngine
import hu.bme.kszk.util.onError
import hu.bme.kszk.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@Composable
fun NavComponent(navController: NavHostController = rememberNavController()) {
    val screens = listOf(
        Screen.Rooms,
        Screen.MyRoom,
        Screen.Friends,
        Screen.Notifications,
        Screen.Profile
    )

    var navigationSelectedItem by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(navigationSelectedItem) {
        val api = ApiServiceImpl(getHttpClientEngine())
        screens.forEach { screen ->
            api.getAllUser().onSuccess { screen.userId = it.first { it.schacc == "tester" }.userUuid }
                .onError {  }
        }
    }




    Scaffold(
        bottomBar = {
            BottomNavigation {
                screens.forEachIndexed  { index, screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = index == navigationSelectedItem,
                        onClick = { navController.navigate(screen); navigationSelectedItem = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Rooms,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Screen.Rooms> { backStackEntry ->
                val userId = backStackEntry.toRoute<Screen.Rooms>().userId ?: return@composable  // backStackEntry.arguments?.getString("userId") ?: "1b31408a-b4be-4c47-9474-0d1b8d98799d"//return@composable
                DiscoverScreenRoot(navController, userId)
            }
            composable<Screen.MyRoom> { backStackEntry ->
                val userId = backStackEntry.toRoute<Screen.MyRoom>().userId ?: return@composable  // backStackEntry.arguments?.getString("userId") ?: "1b31408a-b4be-4c47-9474-0d1b8d98799d"//return@composable
                MyRoomScreenRoot(navController, userId)
            }
            composable<Screen.Friends> { backStackEntry ->
                val userId = backStackEntry.toRoute<Screen.Friends>().userId ?: return@composable  // backStackEntry.arguments?.getString("userId") ?: "1b31408a-b4be-4c47-9474-0d1b8d98799d"//return@composable
                FriendsScreen(navController, userId)
            }
            composable<Screen.Notifications> { backStackEntry ->
                val userId = backStackEntry.toRoute<Screen.Notifications>().userId ?: return@composable  // backStackEntry.arguments?.getString("userId") ?: "1b31408a-b4be-4c47-9474-0d1b8d98799d"//return@composable
                NotificationsScreen(navController, userId)
            }
            composable<Screen.Profile> { backStackEntry ->
                val userId = backStackEntry.toRoute<Screen.Profile>().userId ?: return@composable  // backStackEntry.arguments?.getString("userId") ?: "1b31408a-b4be-4c47-9474-0d1b8d98799d"//return@composable
                ProfileScreen(navController, userId)
            }
        }
    }
}
