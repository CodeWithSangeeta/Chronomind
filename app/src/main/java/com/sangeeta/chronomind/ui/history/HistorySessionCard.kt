package com.sangeeta.chronomind.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun HistorySessionCard(
    session: HistorySessionUiModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(AuraColors.SurfaceCard)
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Icon with activity-color background
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(session.accentColor.copy(alpha = 0.18f))
                .border(
                    width = 1.dp,
                    color = session.accentColor.copy(alpha = 0.30f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = session.icon,
                contentDescription = null,
                tint = session.accentColor,
                modifier = Modifier.size(24.dp),
            )
        }

        // Name + duration
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = session.activityName,
                style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccessTime,
                    contentDescription = null,
                    tint = AuraColors.TextMuted,
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    text = session.durationText,
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextSecondary,
                    maxLines = 1,
                )
            }
        }

        // Status badge
        SessionStatusBadge(
            isCompleted = session.isCompleted,
            label = session.statusLabel,
        )
    }
}

@Composable
private fun SessionStatusBadge(
    isCompleted: Boolean,
    label: String,
) {
    val bgColor = if (isCompleted) {
        Color(0xFF162417)
    } else {
        Color(0xFF241B11)
    }

    val borderColor = if (isCompleted) {
        Color(0xFF315334)
    } else {
        Color(0xFF5A4120)
    }

    val textColor = if (isCompleted) {
        Color(0xFF86E08A)
    } else {
        AuraColors.YellowSoft
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(
            text = label,
            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
            color = textColor,
            maxLines = 1,
        )
    }
}

