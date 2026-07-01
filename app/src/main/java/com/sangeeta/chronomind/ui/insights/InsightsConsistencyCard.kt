package com.sangeeta.chronomind.ui.insights

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun ConsistencyCard(
    consistency: ConsistencyUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        AuraColors.SurfaceCardLight,
                        AuraColors.SurfaceCard
                    )
                )
            )
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Consistency",
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.TextPrimary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AuraColors.SurfaceCardLight,
                            AuraColors.SurfaceCard
                        )
                    )
                )
                .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
                .padding(horizontal = 14.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ConsistencyMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.LocalFireDepartment,
                label = "Current Streak",
                value = consistency.currentStreak.toString(),
                subLabel = "days"
            )

            VerticalDivider()

            ConsistencyMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.EmojiEvents,
                label = "Longest Streak",
                value = consistency.longestStreak.toString(),
                subLabel = "days"
            )

            VerticalDivider()

            CircularConsistencyMeter(
                modifier = Modifier.weight(1f),
                percent = consistency.consistencyPercent,
                footer = consistency.completedDaysText
            )
        }
    }
}

@Composable
private fun ConsistencyMetric(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    subLabel: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
                .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.22f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(50.dp)
            )
        }


        Text(
            text = label,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Row{

            Text(
                text = value,
                style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraColors.YellowPrimary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = subLabel,
                style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraColors.YellowPrimary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(72.dp)
            .background(Color.White.copy(alpha = 0.08f))
    )
}

@Composable
private fun CircularConsistencyMeter(
    modifier: Modifier = Modifier,
    percent: Int,
    footer: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier.size(62.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 7.dp.toPx()
                drawArc(
                    color = Color.White.copy(alpha = 0.08f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(
                            AuraColors.YellowPrimary.copy(alpha = 0.5f),
                            AuraColors.YellowPrimary
                        )
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * (percent / 100f),
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }

            Text(
                text = "$percent%",
                style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraColors.YellowPrimary
            )
        }

        Text(
            text = "Consistency",
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary,
            maxLines = 1
        )

        Text(
            text = footer,
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.YellowPrimary,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}
