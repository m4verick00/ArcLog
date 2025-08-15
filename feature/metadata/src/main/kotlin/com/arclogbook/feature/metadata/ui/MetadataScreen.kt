package com.arclogbook.feature.metadata.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arclogbook.common.ui.component.ModeSection
import com.arclogbook.common.ui.component.ViewMode
import com.arclogbook.database.entity.MetadataExtractionEntity

@Composable
fun MetadataScreen(
    mode: ViewMode,
    viewModel: MetadataViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.extractMetadata(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { filePickerLauncher.launch("*/*") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Upload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Select File")
            }
            
            OutlinedButton(
                onClick = { /* TODO: Implement batch processing */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.FolderOpen, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Batch")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Results list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.extractions) { extraction ->
                MetadataCard(
                    extraction = extraction,
                    mode = mode,
                    onLocationClick = { lat, lng ->
                        viewModel.openLocation(context, lat, lng)
                    }
                )
            }
        }
    }
    
    // Loading indicator
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetadataCard(
    extraction: MetadataExtractionEntity,
    mode: ViewMode,
    onLocationClick: (Double, Double) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // File info
            ModeSection(
                title = extraction.fileName,
                mode = mode,
                simpleContent = {
                    Column {
                        Text("Size: ${formatFileSize(extraction.fileSize)}")
                        Text("Type: ${extraction.mimeType}")
                        if (extraction.hasGpsData) {
                            Text("📍 GPS location found", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                },
                advancedContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        InfoRow("Size", formatFileSize(extraction.fileSize))
                        InfoRow("Type", extraction.mimeType)
                        InfoRow("SHA256", extraction.sha256Hash.take(16) + "...")
                        InfoRow("Extracted", extraction.extractedAt.toString())
                        InfoRow("PII Risk", extraction.piiRiskLevel)
                        
                        extraction.cameraModel?.let {
                            InfoRow("Camera", "$it (${extraction.cameraMake ?: "Unknown"})")
                        }
                    }
                },
                expertContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Raw EXIF Data", style = MaterialTheme.typography.titleSmall)
                        extraction.exifData.entries.take(10).forEach { (key, value) ->
                            Text(
                                "$key: $value",
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Hashes", style = MaterialTheme.typography.titleSmall)
                        Text("SHA256: ${extraction.sha256Hash}", fontFamily = FontFamily.Monospace)
                        Text("MD5: ${extraction.md5Hash}", fontFamily = FontFamily.Monospace)
                    }
                }
            )
            
            // GPS location card
            if (extraction.hasGpsData && extraction.latitude != null && extraction.longitude != null) {
                Spacer(modifier = Modifier.height(8.dp))
                LocationCard(
                    latitude = extraction.latitude,
                    longitude = extraction.longitude,
                    nearestCity = extraction.nearestCity,
                    onlineResult = extraction.onlineGeoResult,
                    mode = mode,
                    onLocationClick = onLocationClick,
                    onCopyCoordinates = { lat, lng ->
                        clipboardManager.setText(AnnotatedString("$lat, $lng"))
                    }
                )
            }
        }
    }
}

@Composable
fun LocationCard(
    latitude: Double,
    longitude: Double,
    nearestCity: String?,
    onlineResult: String?,
    mode: ViewMode,
    onLocationClick: (Double, Double) -> Unit,
    onCopyCoordinates: (Double, Double) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "📍 GPS Location",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    when (mode) {
                        ViewMode.SIMPLE -> {
                            nearestCity?.let { city ->
                                Text(
                                    "Near $city",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        ViewMode.ADVANCED -> {
                            Text(
                                "${String.format("%.6f", latitude)}, ${String.format("%.6f", longitude)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            nearestCity?.let {
                                Text(
                                    "Near: $it (offline)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        ViewMode.EXPERT -> {
                            Text(
                                "Lat: $latitude",
                                fontFamily = FontFamily.Monospace,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "Lng: $longitude",
                                fontFamily = FontFamily.Monospace,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            nearestCity?.let {
                                Text("Offline: $it", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            onlineResult?.let {
                                Text("Online: $it", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        }
                    }
                }
                
                Column {
                    IconButton(
                        onClick = { onLocationClick(latitude, longitude) }
                    ) {
                        Icon(
                            Icons.Default.Map,
                            contentDescription = "View on Maps",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    
                    IconButton(
                        onClick = { onCopyCoordinates(latitude, longitude) }
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy Coordinates",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodySmall)
    }
}

fun formatFileSize(bytes: Long): String {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    
    return when {
        gb >= 1.0 -> String.format("%.2f GB", gb)
        mb >= 1.0 -> String.format("%.2f MB", mb)
        kb >= 1.0 -> String.format("%.2f KB", kb)
        else -> "$bytes B"
    }
}