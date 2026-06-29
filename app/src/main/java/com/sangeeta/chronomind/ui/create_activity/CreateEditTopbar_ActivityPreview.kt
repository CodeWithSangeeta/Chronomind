package com.sangeeta.chronomind.ui.create_activity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun CreateEditTopBar(
    title: String,
    showSaveAction: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(AuraColors.SurfaceCard)
                .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = AuraColors.TextPrimary
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = AuraTypography.DisplayMedium, color = AuraColors.TextPrimary)
        }
        if (showSaveAction) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AuraColors.YellowPrimary)
                    .clickable(onClick = onSaveClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Save",
                    tint = AuraColors.BackgroundDark
                )
            }
        }
    }
}




@Composable
fun ActivityPreviewCard(uiState: CreateEditUiState) {
    val accentColor = uiState.selectedColor.color
    val durationText = when {
        uiState.targetType == TargetType.STOPWATCH -> "Stopwatch"
        uiState.targetHours > 0 && uiState.targetMinutes > 0 ->
            "${uiState.targetHours}h ${uiState.targetMinutes}m"
        uiState.targetHours > 0 -> "${uiState.targetHours}h"
        uiState.targetMinutes > 0 -> "${uiState.targetMinutes}m"
        else -> "Set target"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.12f),
                        AuraColors.SurfaceCard
                    )
                )
            )
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = uiState.selectedIcon.icon,
                contentDescription = null,
                // tint = if (isSelected) selectedColor else AuraColors.TextMuted,
                modifier = Modifier.size(32.dp)
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = if (uiState.activityName.isBlank()) "Activity name" else uiState.activityName,
                style = AuraTypography.TitleMedium,
                color = if (uiState.activityName.isBlank()) AuraColors.TextMuted else AuraColors.TextPrimary,
                maxLines = 1
            )
            Text(
                text = durationText,
                style = AuraTypography.BodyMedium,
                color = accentColor
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(accentColor.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = uiState.targetType.label.uppercase(),
                style = AuraTypography.LabelMedium,
                color = accentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}