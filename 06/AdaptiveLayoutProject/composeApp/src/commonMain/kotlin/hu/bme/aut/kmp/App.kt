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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import adaptivelayoutproject.composeapp.generated.resources.Res
import adaptivelayoutproject.composeapp.generated.resources.compose_multiplatform
import androidx.annotation.StringDef
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        SupportingPaneScaffoldExample()
    }
}

@Composable
fun WindowSizeCompose() {
    val windowInfo = currentWindowAdaptiveInfo()
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(windowInfo.toString())
    }
}

@Composable
fun WriteConstraints(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Width: ${this@BoxWithConstraints.minWidth}-${this@BoxWithConstraints.maxWidth}")
            Text("Height: ${this@BoxWithConstraints.minHeight}-${this@BoxWithConstraints.maxHeight}")
        }
    }
}

@Composable
fun AdaptiveSimple() {
    WriteConstraints(
        modifier = Modifier.fillMaxSize()
    )
}

data class MyItem(
    val name: String,
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListDetailPaneScaffoldExample() {
    val itemList = listOf(
        MyItem("Email 1"),
        MyItem("Email 2"),
        MyItem("Email 3"),
    )
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<MyItem>()
    val scope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        value = scaffoldNavigator.scaffoldValue,
        directive = PaneScaffoldDirective.Default,
        listPane = {
            AnimatedPane {
                LazyColumn {
                    items(itemList) {
                        ListTile(
                            title = { Text(it.name) },
                            onClick = {
                                scope.launch {
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        it
                                    )
                                }
                            },
                        )
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                val content = @Composable {
                    scaffoldNavigator.currentDestination?.content?.let {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Detail pane: ${it.name}")
                        }
                    }
                }
                if (scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Hidden) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Detail Page") },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            scaffoldNavigator.navigateBack()
                                        }
                                    ) {
                                        Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
                                    }
                                }
                            )
                        }
                    ) {
                        Box(modifier = Modifier.fillMaxSize().padding(it)) {
                            content()
                        }
                    }
                } else {
                    content()
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SupportingPaneScaffoldExample() {
    val itemList = listOf(
        MyItem("Email 1"),
        MyItem("Email 2"),
        MyItem("Email 3"),
    )
    val scaffoldNavigator = rememberSupportingPaneScaffoldNavigator<MyItem>()
    val scope = rememberCoroutineScope()

    SupportingPaneScaffold(
        value = scaffoldNavigator.scaffoldValue,
        directive = PaneScaffoldDirective.Default,
        supportingPane = {
            AnimatedPane {
                LazyColumn {
                    items(itemList) {
                        ListTile(
                            title = { Text(it.name) },
                            onClick = {
                                scope.launch {
                                    scaffoldNavigator.navigateTo(
                                        SupportingPaneScaffoldRole.Main
                                    )
                                }
                            },
                        )
                    }
                }
            }
        },
        mainPane = {
            AnimatedPane {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Main Page") }
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(it),
                        contentAlignment = Alignment.Center
                    ) {
                       Column {
                           Text("Main Content")
                           if (scaffoldNavigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden){
                               Button(
                                   onClick = {
                                       scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                   }
                               ){
                                   Text("Go to Supporting")
                               }
                           }
                       }
                    }
                }
            }
        },
    )
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    SETTINGS("Settings", Icons.Default.Settings),
}

@Composable
fun NavigationSuiteScaffoldExample() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = { Icon(it.icon, it.label) },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(currentDestination.label)
        }
    }
}