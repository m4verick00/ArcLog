package com.arclogbook.common.ui.component

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class ViewMode {
    SIMPLE, ADVANCED, EXPERT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeToggle(
    currentMode: ViewMode,
    onModeChange: (ViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        FilterChip(
            onClick = { expanded = true },
            label = {
                AnimatedContent(
                    targetState = currentMode,
                    transitionSpec = { fadeIn() with fadeOut() },
                    label = "mode_text"
                ) { mode ->
                    Text(
                        when (mode) {
                            ViewMode.SIMPLE -> "Simple"
                            ViewMode.ADVANCED -> "Advanced"
                            ViewMode.EXPERT -> "Expert"
                        }
                    )
                }
            },
            selected = true,
            modifier = Modifier.menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ViewMode.values().forEach { mode ->
                DropdownMenuItem(
                    text = {
                        Text(
                            when (mode) {
                                ViewMode.SIMPLE -> "Simple"
                                ViewMode.ADVANCED -> "Advanced"
                                ViewMode.EXPERT -> "Expert"
                            }
                        )
                    },
                    onClick = {
                        onModeChange(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}