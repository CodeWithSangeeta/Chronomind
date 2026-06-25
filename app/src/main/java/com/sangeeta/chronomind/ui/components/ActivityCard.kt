package com.sangeeta.chronomind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
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
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun ActivityCard(
    activity: ActivityUiModel,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onActionClick: () -> Unit
) {
    val borderColor = when {
        activity.isRunning -> AuraColors.YellowPrimary
        isSelected         -> AuraColors.YellowPrimary.copy(alpha = 0.55f)
        else               -> AuraColors.CardBorderDefault
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF141414), AuraColors.SurfaceCard)
                )
            )
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(24.dp))
            .clickable(onClick = onCardClick)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon badge
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(AuraColors.SurfaceCardLight),
                contentAlignment = Alignment.Center
            ) {
                // Fixed: use Icon with ImageVector instead of Text with Emoji
                Icon(
                    imageVector = activity.icon,
                    contentDescription = null,
                    tint = AuraColors.YellowPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Name + meta + progress bar
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = activity.name,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${activity.targetSeconds / 60} min • ${activity.lastActiveDate}",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "${(activity.progress * 100).toInt()}%",
                        style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = AuraColors.YellowPrimary
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(AuraColors.TimerTrack)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(activity.progress.coerceIn(0f, 1f))
                                .height(5.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(AuraColors.YellowPrimary)
                        )
                    }
                }
            }

            // Play / Pause toggle button
            val actionIcon = if (activity.isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
            val actionDesc = if (activity.isRunning) "Pause ${activity.name}" else "Select ${activity.name}"
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AuraColors.SurfaceCardLight)
                    .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                    .clickable(onClick = onActionClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionDesc,
                    tint = AuraColors.YellowPrimary
                )
            }
        }
    }
}
