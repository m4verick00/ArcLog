package com.arclogbook.feature.widgets

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.material3.ColorProviders

class StatusWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: android.content.Context, id: androidx.glance.GlanceId) {
        provideContent {
            StatusWidgetContent()
        }
    }

    @Composable
    private fun StatusWidgetContent() {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ArcLogbook",
                style = TextStyle(
                    colorProvider = ColorProviders.primary
                )
            )
            
            Spacer(modifier = GlanceModifier.height(8.dp))
            
            Row(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🟢 Active")
                Spacer(modifier = GlanceModifier.width(8.dp))
                Text("Last scan: 2h ago")
            }
        }
    }
}

class StatusWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StatusWidget()
}