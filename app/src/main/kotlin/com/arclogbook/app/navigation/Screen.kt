package com.arclogbook.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.arclogbook.app.R

sealed class Screen(
    val route: String,
    val resourceId: Int,
    val icon: ImageVector
) {
    object Dashboard : Screen("dashboard", R.string.dashboard, Icons.Default.Dashboard)
    object Logbook : Screen("logbook", R.string.logbook, Icons.Default.Book)
    object Intel : Screen("intel", R.string.intel, Icons.Default.Security)
    object Vuln : Screen("vuln", R.string.vuln, Icons.Default.BugReport)
    object Darkweb : Screen("darkweb", R.string.darkweb, Icons.Default.Visibility)
    object Osint : Screen("osint", R.string.osint, Icons.Default.Search)
    object Metadata : Screen("metadata", R.string.metadata, Icons.Default.Info)
    object AI : Screen("ai", R.string.ai, Icons.Default.SmartToy)
    object Connectors : Screen("connectors", R.string.connectors, Icons.Default.Link)
    object Settings : Screen("settings", R.string.settings, Icons.Default.Settings)
}