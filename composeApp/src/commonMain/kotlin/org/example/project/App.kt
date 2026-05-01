package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinproject.composeapp.generated.resources.*

data class ShoppingItem(
    val name: String,
    val quantity: Int,
    val isChecked: Boolean = false
)

sealed class Screen(val route: String, val resourceId: org.jetbrains.compose.resources.StringResource, val icon: ImageVector) {
    object Home : Screen("home", Res.string.nav_home, Icons.Default.Home)
    object Work : Screen("work", Res.string.nav_work, Icons.Default.ShoppingCart)
    object About : Screen("about", Res.string.nav_about, Icons.Default.Info)
}

@Composable
@Preview
fun App() {
    AppTheme {
        val navController = rememberNavController()
        val items = listOf(Screen.Home, Screen.Work, Screen.About)

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(stringResource(screen.resourceId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        ShoppingListScreen()
                    }
                    composable(Screen.Work.route) {
                        ShoppingListScreen(
                            initialItems = listOf(
                                ShoppingItem(stringResource(Res.string.apples), 5),
                                ShoppingItem(stringResource(Res.string.eggs), 10)
                            )
                        )
                    }
                    composable(Screen.About.route) {
                        AboutScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.about_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.about_text),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
