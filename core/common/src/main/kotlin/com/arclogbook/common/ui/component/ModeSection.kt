package com.arclogbook.common.ui.component

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ModeSection(
    title: String,
    mode: ViewMode,
    modifier: Modifier = Modifier,
    simpleContent: @Composable () -> Unit = {},
    advancedContent: @Composable () -> Unit = {},
    expertContent: @Composable () -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { it / 3 }) + fadeIn() with
                slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut()
            },
            label = "mode_content"
        ) { targetMode ->
            when (targetMode) {
                ViewMode.SIMPLE -> simpleContent()
                ViewMode.ADVANCED -> advancedContent()
                ViewMode.EXPERT -> expertContent()
            }
        }
    }
}

@Composable
fun ModeText(
    mode: ViewMode,
    simpleText: String,
    advancedText: String? = null,
    expertText: String? = null,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium
) {
    AnimatedContent(
        targetState = mode,
        transitionSpec = { fadeIn() with fadeOut() },
        label = "mode_text"
    ) { targetMode ->
        Text(
            text = when (targetMode) {
                ViewMode.SIMPLE -> simpleText
                ViewMode.ADVANCED -> advancedText ?: simpleText
                ViewMode.EXPERT -> expertText ?: advancedText ?: simpleText
            },
            style = style,
            modifier = modifier
        )
    }
}