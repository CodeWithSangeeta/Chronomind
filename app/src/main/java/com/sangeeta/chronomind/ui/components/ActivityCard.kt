package com.sangeeta.chronomind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocalFireDepartment
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
import androidx.compose.ui.unit.sp
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
    val accent = rememberActivityAccent(activity.colorHex)
    val completedGreen = Color(0xFF6FCF7B)

    val borderColor = if (isSelected) {
        AuraColors.CardBorderDefault.copy(alpha = 0.9f)
    } else {
        AuraColors.CardBorderDefault
    }

    val actionIcon = when (activity.sessionState) {
        ActivitySessionState.RUNNING -> Icons.Rounded.Pause
        ActivitySessionState.PENDING -> Icons.Rounded.PlayArrow
        ActivitySessionState.COMPLETED_TODAY -> Icons.Rounded.CheckCircle
        ActivitySessionState.IDLE -> Icons.Rounded.PlayArrow
    }

    val actionTint = when (activity.sessionState) {
        ActivitySessionState.COMPLETED_TODAY -> completedGreen
        else -> AuraColors.YellowPrimary
    }


    val actionEnabled = activity.sessionState != ActivitySessionState.COMPLETED_TODAY

    val completionBadgeLabel = when (activity.sessionState) {
        ActivitySessionState.COMPLETED_TODAY -> "Done today"
        else -> null
    }

    val supportText = when (activity.sessionState) {
        ActivitySessionState.RUNNING -> null
        ActivitySessionState.PENDING -> "Last attempt: Today"
        ActivitySessionState.COMPLETED_TODAY -> null
        ActivitySessionState.IDLE -> "Last attempt: ${activity.lastActiveDate}"
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
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable(onClick = onCardClick)
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
                            color = accent.copy(alpha = 0.5f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = activity.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = activity.name,
                            modifier = Modifier.weight(1f),
                            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = AuraColors.TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (activity.streakDays > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = AuraColors.YellowPrimary.copy(alpha = 0.78f),
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = activity.streakDays.toString(),
                                    style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = AuraColors.TextSecondary
                                )
                            }
                        }
                    }

                    Text(
                        text = activity.displayTime,
                        style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Medium),
                        color = AuraColors.TextPrimary,
                        maxLines = 1
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActivityMetaBadge(
                            label = if (activity.isStopwatch) "Stopwatch" else "Timer",
                            textColor = accent,
                            backgroundColor = accent.copy(alpha = 0.10f),
                            borderColor = accent.copy(alpha = 0.18f),
                            leadingIcon = Icons.Rounded.Bolt
                        )

                        if (completionBadgeLabel != null) {
                            ActivityMetaBadge(
                                label = completionBadgeLabel,
                                textColor = completedGreen,
                                backgroundColor = completedGreen.copy(alpha = 0.10f),
                                borderColor = completedGreen.copy(alpha = 0.18f)
                            )
                        }
                    }

                    if (supportText != null) {
                        Text(
                            text = supportText,
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (actionEnabled) {
                                AuraColors.SurfaceCardLight
                            } else {
                                AuraColors.SurfaceCardLight.copy(alpha = 0.7f)
                            }
                        )
                        .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                        .clickable(
                            enabled = actionEnabled,
                            onClick = onActionClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null,
                        tint = if (actionEnabled) actionTint else completedGreen.copy(alpha = 0.78f),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            val showProgress = (activity.progress > 0f || activity.sessionState == ActivitySessionState.RUNNING ) && !activity.isStopwatch

            if (showProgress) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${(activity.progress * 100).toInt()}%",
                            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = AuraColors.TextSecondary,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Progress",
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextMuted,
                            fontSize = 10.sp
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(AuraColors.TimerTrack)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(activity.progress.coerceIn(0f, 1f))
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(accent)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
 fun ActivityMetaBadge(
    label: String,
    textColor: Color,
    backgroundColor: Color,
    borderColor: Color,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(12.dp)
            )
        }

        Text(
            text = label,
            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
            color = textColor,
            maxLines = 1
        )
    }
}

@Composable
private fun rememberActivityAccent(colorHex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor("#${colorHex.removePrefix("#")}"))
    } catch (_: IllegalArgumentException) {
        AuraColors.YellowPrimary
    }
}