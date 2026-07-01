package com.secondpulse.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondpulse.app.domain.Option
import com.secondpulse.app.ui.theme.SpColors

@Composable fun SpChoiceCard(option: Option, enabled: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().heightIn(min = 72.dp).clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(14.dp), color = SpColors.Surface.copy(alpha=.75f), border = BorderStroke(1.dp, SpColors.BorderStrong)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                option.neutralContextLabel?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = SpColors.TextMuted) }
                Text(option.visibleLabel, style = MaterialTheme.typography.titleLarge, color = SpColors.TextPrimary)
                option.intentSubtitle?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = SpColors.TextSecondary) }
            }
            Icon(Icons.Outlined.ChevronRight, null, tint = SpColors.TextPrimary)
        }
    }
}
