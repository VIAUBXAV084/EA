package hu.bme.aut.kmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import permissionproject.composeapp.generated.resources.Res
import permissionproject.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
        val controller: PermissionsController = remember(factory) { factory.createPermissionsController() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()
        var permissionState by remember { mutableStateOf("") }

        BindEffect(controller)

        LaunchedEffect(true){
            if (controller.isPermissionGranted(Permission.CAMERA)){
                permissionState = "Camera permission is granted!"
            } else {
                permissionState = "Missing camera permission!"
            }
        }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                coroutineScope.launch {
                    try {
                        controller.providePermission(Permission.CAMERA)
                        permissionState = "Permission granted!"
                    } catch (e : DeniedException) {
                        permissionState = "Permission denied"
                    } catch (e : DeniedAlwaysException) {
                        permissionState = "Always denied"
                    } catch (e : RequestCanceledException) {
                        permissionState = "Request cancelled"

                    }
                }
            }) {
                Text("Request permission!")
            }
            Text(permissionState)
        }
    }
}