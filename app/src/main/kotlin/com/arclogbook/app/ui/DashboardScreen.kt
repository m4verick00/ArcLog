package com.arclogbook.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arclogbook.common.ui.component.ModeSection
import com.arclogbook.common.ui.component.ViewMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    mode: ViewMode,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ModeSection(
                title = "System Status",
                mode = mode,
                simpleContent = {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("All systems operational", style = MaterialTheme.typography.bodyLarge)
                            Text("Last scan: 2 hours ago", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                },
                advancedContent = {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("System Status", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            StatusItem("Threat Intel Sync", "Active", true)
                            StatusItem("Metadata Indexer", "Idle", true)
                            StatusItem("AI Assistant", "Ready", true)
                        }
                    }
                },
                expertContent = {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Detailed System Metrics", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("CPU Usage: 15%", style = MaterialTheme.typography.bodyMedium)
                            Text("Memory: 234MB / 2GB", style = MaterialTheme.typography.bodyMedium)
                            Text("Database Size: 45.2MB", style = MaterialTheme.typography.bodyMedium)
                            Text("Cache Hit Ratio: 89.3%", style = MaterialTheme.typography.bodyMedium)
                            Text("Network Requests: 23 (last hour)", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            )
        }
        
        item {
            ModeSection(
                title = "Recent Activity",
                mode = mode,
                simpleContent = {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("3 new incidents logged today", style = MaterialTheme.typography.bodyLarge)
                            Text("12 threat indicators processed", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                },
                advancedContent = {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Activity Summary", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            ActivityItem("New incident: Phishing attempt", "2 hours ago")
                            ActivityItem("Metadata extracted: IMG_2023.jpg", "4 hours ago")
                            ActivityItem("Threat intel updated", "6 hours ago")
                        }
                    }
                },
                expertContent = {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Raw Activity Log", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("[14:23:45] WorkManager: ThreatIntelSyncWorker completed", 
                                style = MaterialTheme.typography.bodyMedium)
                            Text("[14:20:12] Database: Indexed 47 new threat indicators",
                                style = MaterialTheme.typography.bodyMedium)
                            Text("[14:15:33] Metadata: Extracted GPS from IMG_2023.jpg",
                                style = MaterialTheme.typography.bodyMedium)
                            Text("[14:10:55] Auth: Biometric authentication successful",
                                style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun StatusItem(
    name: String,
    status: String,
    isHealthy: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, style = MaterialTheme.typography.bodyMedium)
        Text(
            status,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isHealthy) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun ActivityItem(
    description: String,
    timestamp: String
) {
    Column {
        Text(description, style = MaterialTheme.typography.bodyMedium)
        Text(timestamp, style = MaterialTheme.typography.bodySmall, 
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
    }
}