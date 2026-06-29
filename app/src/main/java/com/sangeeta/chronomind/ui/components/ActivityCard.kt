package com.sangeeta.chronomind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
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
import com.sangeeta.chronomind.ui.model.ActivitySessionState
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
    val borderColor = when (activity.sessionState) {
        ActivitySessionState.RUNNING -> AuraColors.YellowPrimary
        ActivitySessionState.PENDING -> AuraColors.YellowPrimary.copy(alpha = 0.55f)
        ActivitySessionState.COMPLETED_TODAY -> Color(0xFF4CAF50).copy(alpha = 0.6f)
        ActivitySessionState.IDLE -> if (isSelected) {
            AuraColors.YellowPrimary.copy(alpha = 0.45f)
        } else {
            AuraColors.CardBorderDefault
        }
    }

    val statusText = when (activity.sessionState) {
        ActivitySessionState.RUNNING -> "In session • ${activity.displayTime}"
        ActivitySessionState.PENDING -> "Resume • ${activity.displayTime}"
        ActivitySessionState.COMPLETED_TODAY -> "Completed today • ${activity.displayTime}"
        ActivitySessionState.IDLE -> "${activity.targetSeconds / 60} min • ${activity.lastActiveDate}"
    }

    val actionIcon = when (activity.sessionState) {
        ActivitySessionState.RUNNING -> Icons.Rounded.Pause
        ActivitySessionState.PENDING -> Icons.Rounded.PlayArrow
        ActivitySessionState.COMPLETED_TODAY -> Icons.Rounded.CheckCircle
        ActivitySessionState.IDLE -> Icons.Rounded.PlayArrow
    }

    val actionTint = when (activity.sessionState) {
        ActivitySessionState.COMPLETED_TODAY -> Color(0xFF66BB6A)
        ActivitySessionState.PENDING -> Color(0xFFFFB74D)
        else -> AuraColors.YellowPrimary
    }

    val actionDescription = when (activity.sessionState) {
        ActivitySessionState.RUNNING -> "Pause ${activity.name}"
        ActivitySessionState.PENDING -> "Resume ${activity.name}"
        ActivitySessionState.COMPLETED_TODAY -> "${activity.name} completed today"
        ActivitySessionState.IDLE -> "Start ${activity.name}"
    }

    val actionEnabled = activity.sessionState != ActivitySessionState.COMPLETED_TODAY

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF141414), AuraColors.SurfaceCard)
                )
            )
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable(onClick = onCardClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Transparent)
                .widthIn(min = 0.dp)
                .then(Modifier)
                .paddingSafe(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(AuraColors.SurfaceCardLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = activity.icon,
                    contentDescription = null,
                    tint = actionTint,
                    modifier = Modifier.size(24.dp)
                )
            }

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
                    text = statusText,
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "${(activity.progress * 100).toInt()}%",
                        style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = actionTint
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
                                .background(actionTint)
                        )
                    }
                }

                if (activity.streakDays > 0) {
                    Text(
                        text = "Streak ${activity.streakDays} days",
                        style = AuraTypography.BodySmall,
                        color = AuraColors.TextMuted
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (actionEnabled) AuraColors.SurfaceCardLight
                        else AuraColors.SurfaceCardLight.copy(alpha = 0.6f)
                    )
                    .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                    .clickable(enabled = actionEnabled, onClick = onActionClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionDescription,
                    tint = if (actionEnabled) actionTint else actionTint.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private fun Modifier.paddingSafe(): Modifier =
    this.then(
        Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    )