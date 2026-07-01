package com.sangeeta.chronomind.ui.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun RangeTabs(
    selected: InsightsRange,
    onSelected: (InsightsRange) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF101010))
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        InsightsRange.entries.forEach { range ->
            val active = range == selected

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        if (active) {
                            Brush.horizontalGradient(
                                listOf(
                                    AuraColors.YellowPrimary.copy(alpha = 0.18f),
                                    AuraColors.SurfaceCard
                                )
                            )
                        } else {
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            )
                        }
                    )
                    .border(
                        width = if (active) 1.dp else 0.dp,
                        color = if (active) AuraColors.YellowPrimary.copy(alpha = 0.32f) else Color.Transparent,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable { onSelected(range) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = range.label,
                    style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (active) AuraColors.YellowPrimary else AuraColors.TextPrimary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun SummaryRow(summary: InsightsSummary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCardItem(
            icon = Icons.Rounded.Timer,
            title = "Focus",
            value = summary.totalFocusTime,
            delta = summary.totalFocusDelta,
            deltaLabel = "vs previous"
        )
        SummaryCardItem(
            icon = Icons.Rounded.TaskAlt,
            title = "Sessions",
            value = summary.sessionsCompleted.toString(),
            delta = summary.sessionsDelta,
            deltaLabel = "vs previous"
        )
        SummaryCardItem(
            icon = Icons.Rounded.PieChart,
            title = "Completion Rate",
            value = "${summary.completionRate}%",
            delta = summary.completionDelta,
            deltaLabel = "vs previous"
        )
    }
}

@Composable
private fun SummaryCardItem(
    icon: ImageVector,
    title: String,
    value: String,
    delta: String,
    deltaLabel: String
) {
    Column(
        modifier = Modifier
            .width(146.dp)
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
                .border(
                    1.dp,
                    AuraColors.YellowPrimary.copy(alpha = 0.22f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = title,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Text(
            text = value,
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.TextPrimary,
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        Text(
            text = "$delta $deltaLabel",
            style = AuraTypography.BodySmall,
            color = AuraColors.YellowPrimary,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}
