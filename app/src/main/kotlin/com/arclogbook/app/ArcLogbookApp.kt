package com.arclogbook.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.arclogbook.common.ui.component.ModeToggle
import com.arclogbook.common.ui.component.ViewMode
import com.arclogbook.app.navigation.Screen
import com.arclogbook.app.ui.DashboardScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArcLogbookApp() {
    val navController = rememberNavController()
    var currentMode by remember { mutableStateOf(ViewMode.SIMPLE) }
    
    val items = listOf(
        Screen.Dashboard,
        Screen.Logbook, 
        Screen.Intel,
        Screen.Vuln,
        Screen.Osint,
        Screen.Metadata
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ArcLogbook") },
                actions = {
                    ModeToggle(
                        currentMode = currentMode,
                        onModeChange = { currentMode = it }
                    )
                }
            )
        },
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(mode = currentMode)
            }
            composable(Screen.Logbook.route) {
                LogbookScreen(mode = currentMode)
            }
            composable(Screen.Intel.route) {
                IntelScreen(mode = currentMode)
            }
            composable(Screen.Vuln.route) {
                VulnScreen(mode = currentMode)
            }
            composable(Screen.Osint.route) {
                OsintScreen(mode = currentMode)
            }
            composable(Screen.Metadata.route) {
                MetadataScreen(mode = currentMode)
            }
        }
    }
}

@Composable
fun LogbookScreen(mode: ViewMode) {
    Text("Logbook - Mode: ${mode.name}")
}

@Composable  
fun IntelScreen(mode: ViewMode) {
    Text("Threat Intel - Mode: ${mode.name}")
}

@Composable
fun VulnScreen(mode: ViewMode) {
    Text("Vulnerabilities - Mode: ${mode.name}")
}

@Composable
fun OsintScreen(mode: ViewMode) {
    Text("OSINT - Mode: ${mode.name}")
}

@Composable
fun MetadataScreen(mode: ViewMode) {
    Text("Metadata Extractor - Mode: ${mode.name}")
}