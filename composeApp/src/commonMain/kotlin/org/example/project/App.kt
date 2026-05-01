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
import kotlinx.coroutines.launch
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
    val appSettings = remember { AppSettings.getInstance() }
    var isDark by remember { mutableStateOf(appSettings.isDarkMode) }

    AppTheme(darkTheme = isDark) {
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
                        AboutScreen(
                            isDark = isDark,
                            onDarkThemeChange = {
                                isDark = it
                                appSettings.isDarkMode = it
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AboutScreen(
    isDark: Boolean,
    onDarkThemeChange: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    var catFact by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun loadFact() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                val result = fetchCatFact()
                catFact = result.fact
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadFact()
    }

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
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(Res.string.dark_theme_label))
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isDark,
                onCheckedChange = onDarkThemeChange
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        PermissionRequestSection()

        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.cat_fact_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (errorMessage != null) {
                    Text(
                        text = stringResource(Res.string.network_error, errorMessage!!),
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(
                        text = catFact ?: stringResource(Res.string.loading),
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(onClick = { loadFact() }, enabled = !isLoading) {
                    Text(stringResource(Res.string.update_fact))
                }
            }
        }
    }
}
