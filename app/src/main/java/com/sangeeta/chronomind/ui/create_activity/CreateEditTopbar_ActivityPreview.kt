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
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.components.ActivityMetaBadge
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
    val accent = uiState.selectedColor.color

    val titleText = if (uiState.activityName.isBlank()) { "Activity name" } else { uiState.activityName }

    val timeText = when {
        uiState.targetType == TargetType.STOPWATCH -> "00:00"
        uiState.targetHours > 0 && uiState.targetMinutes > 0 ->
            "${uiState.targetHours}h ${uiState.targetMinutes}m"
        uiState.targetHours > 0 -> "${uiState.targetHours}h 00m"
        uiState.targetMinutes > 0 -> "${uiState.targetMinutes}m"
        else -> "Set target"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AuraColors.SurfaceCardLight,
                        AuraColors.SurfaceCard
                    )
                )
            )
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(accent)
                        .border(
                            width = 1.dp,
                            color = accent.copy(alpha = 0.50f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = uiState.selectedIcon.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = titleText,
                        style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (uiState.activityName.isBlank()) {
                            AuraColors.TextMuted
                        } else {
                            AuraColors.TextPrimary
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = timeText,
                        style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Medium),
                        color = AuraColors.TextPrimary,
                        maxLines = 1
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActivityMetaBadge(
                            label = if (uiState.targetType == TargetType.STOPWATCH) {
                                "Stopwatch"
                            } else {
                                "Timer"
                            },
                            textColor = accent,
                            backgroundColor = accent.copy(alpha = 0.10f),
                            borderColor = accent.copy(alpha = 0.18f),
                            leadingIcon = Icons.Rounded.Bolt
                        )
                    }

                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(AuraColors.SurfaceCardLight)
                        .border(1.dp, AuraColors.CardBorderDefault, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        tint = AuraColors.YellowPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}