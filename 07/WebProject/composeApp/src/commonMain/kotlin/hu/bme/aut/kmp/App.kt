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
import coil3.compose.AsyncImage
import com.russhwolf.settings.Settings
import com.russhwolf.settings.int
import org.jetbrains.compose.resources.painterResource

import webproject.composeapp.generated.resources.Res
import webproject.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    MaterialTheme {
        SettingsApp()
    }
}

@Composable
fun TestJSIntegration(){
    val myObj = remember { MyClass("Hello") }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(myObj.testVal)
        Text(myVariable.toString())
        Text(getCurrentUrl())
    }
}

@Composable
fun ImageApp(){
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = "https://thispersondoesnotexist.com/",
            contentDescription = null,
        )
        AsyncImage(
            model = "https://renderform.io/_next/static/chunks/images/landing/example-template-in-editor_1920.png",
            contentDescription = null,
        )
    }
}

@Composable
fun SettingsApp(){
    val settings = remember { Settings() }
    var counterSetting by settings.int("count", 0)
    var counterState by remember { mutableStateOf(counterSetting) }

    LaunchedEffect(counterState){
        counterSetting = counterState
    }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                counterState++
            }
        ){
            Text("Increase Counter!")
        }
        Text("Current count: $counterSetting")
    }
}