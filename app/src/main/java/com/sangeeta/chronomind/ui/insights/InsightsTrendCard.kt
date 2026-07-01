package com.sangeeta.chronomind.ui.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarViewWeek
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun TrendCard(
    selectedRange: InsightsRange,
    trend: TrendChartUiModel,
    pattern: PatternInsightUiModel
) {
    var showPatternDialog by remember { mutableStateOf(false) }

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = trend.title,
                    style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AuraColors.TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = trend.totalLabel,
                    style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AuraColors.YellowPrimary,
                    maxLines = 1
                )
                Text(
                    text = trend.subtitle,
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF131313))
                    .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = when (selectedRange) {
                        InsightsRange.WEEK -> "Weekly"
                        InsightsRange.MONTH -> "Monthly"
                        InsightsRange.YEAR ->  "Yearly"
                    },
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextPrimary,
                    maxLines = 1
                )
            }
        }

        TrendBarChart(
            points = trend.points,
            highlightedIndex = trend.highlightedIndex
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PatternPillButton(
                selectedRange = selectedRange,
                pattern = pattern,
                onClick = { showPatternDialog = true }
            )
        }
    }

    if (showPatternDialog) {
        ProductivePatternDialog(
            selectedRange = selectedRange,
            pattern = pattern,
            onDismiss = { showPatternDialog = false }
        )


    }
}

@Composable
private fun TrendBarChart(
    points: List<TrendPointUiModel>,
    highlightedIndex: Int
) {
    val maxValue = maxOf(points.maxOfOrNull { it.valueMinutes } ?: 0, 1).toFloat()
    val scrollState = rememberScrollState()

    val barWidth = 24.dp
    val itemWidth = 46.dp
    val minVisibleBarsWithoutScroll = 7
    val shouldScroll = points.size > minVisibleBarsWithoutScroll
    val chartContentWidth = itemWidth * points.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .then(
                    if (shouldScroll) {
                        Modifier.horizontalScroll(scrollState)
                    } else {
                        Modifier
                    }
                )
        ) {
            Row(
                modifier = if (shouldScroll) {
                    Modifier
                        .width(chartContentWidth)
                        .fillMaxHeight()
                        .drawBehind {
                            val stroke = 1.dp.toPx()
                            val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            repeat(3) { index ->
                                val y = size.height * (index + 1) / 4f
                                drawLine(
                                    color = Color.White.copy(alpha = 0.10f),
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = stroke,
                                    pathEffect = dash
                                )
                            }
                        }
                } else {
                    Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val stroke = 1.dp.toPx()
                            val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            repeat(3) { index ->
                                val y = size.height * (index + 1) / 4f
                                drawLine(
                                    color = Color.White.copy(alpha = 0.10f),
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = stroke,
                                    pathEffect = dash
                                )
                            }
                        }
                },
                horizontalArrangement = if (shouldScroll) Arrangement.Start else Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                points.forEachIndexed { index, point ->
                    val fraction = point.valueMinutes / maxValue
                    val active = index == highlightedIndex

                    Column(
                        modifier = Modifier.width(itemWidth),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        if (active) {
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AuraColors.YellowPrimary)
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = formatMinutes(point.valueMinutes),
                                    style = AuraTypography.BodySmall,
                                    color = Color(0xFF181818),
                                    maxLines = 1
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        Box(
                            modifier = Modifier
                                .width(barWidth)
                                .height((42 + (92 * fraction)).dp)
                                .shadow(
                                    elevation = if (active) 12.dp else 5.dp,
                                    shape = RoundedCornerShape(9.dp),
                                    ambientColor = AuraColors.YellowGlow,
                                    spotColor = AuraColors.YellowGlow
                                )
                                .clip(RoundedCornerShape(9.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = if (active) {
                                            listOf(Color(0xFFF9D647), Color(0xFFC58D08))
                                        } else {
                                            listOf(Color(0xFFF1C93D), Color(0xFFA97808))
                                        }
                                    )
                                )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = point.label,
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextSecondary,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}




@Composable
private fun PatternPillButton(
    selectedRange: InsightsRange,
    pattern: PatternInsightUiModel,
    onClick: () -> Unit
) {
    val title = when (selectedRange) {
        InsightsRange.WEEK -> "Most productive day"
        InsightsRange.MONTH -> "Most productive day"
        InsightsRange.YEAR -> "Most productive month"
    }

    val icon = when (selectedRange) {
        InsightsRange.WEEK -> Icons.Rounded.Today
        InsightsRange.MONTH -> Icons.Rounded.Today
        InsightsRange.YEAR -> Icons.Rounded.DateRange
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        AuraColors.YellowPrimary.copy(alpha = 0.14f),
                        AuraColors.SurfaceCard
                    )
                )
            )
            .border(
                1.dp,
                AuraColors.YellowPrimary.copy(alpha = 0.26f),
                RoundedCornerShape(22.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AuraColors.YellowPrimary,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = title,
            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.YellowPrimary,
            maxLines = 1
        )
    }
}

@Composable
private fun ProductivePatternDialog(
    selectedRange: InsightsRange,
    pattern: PatternInsightUiModel,
    onDismiss: () -> Unit
) {
    val heading = when (selectedRange) {
        InsightsRange.WEEK -> "Most Productive Day"
        InsightsRange.MONTH -> "Most Productive Day"
        InsightsRange.YEAR -> "Most Productive Month"
    }

    val icon = when (selectedRange) {
        InsightsRange.WEEK -> Icons.Rounded.Today
        InsightsRange.MONTH -> Icons.Rounded.Today
        InsightsRange.YEAR -> Icons.Rounded.DateRange
    }

    val subtitle = when (selectedRange) {
        InsightsRange.WEEK -> "Based on your finished sessions from this week."
        InsightsRange.MONTH -> "Based on your finished sessions from this month."
        InsightsRange.YEAR -> "Based on your finished sessions from this year."
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AuraColors.SurfaceCard,
        icon = {
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
                    tint = AuraColors.YellowPrimary
                )
            }
        },
        title = {
            Text(
                text = heading,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = pattern.highlight.ifBlank { "No clear pattern yet" },
                        style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = AuraColors.YellowPrimary,
                        maxLines = 1
                    )

                    if (pattern.value.isNotBlank() && pattern.highlight.isNotBlank()) {
                        Text(
                            text = "· ${pattern.value}",
                            style = AuraTypography.BodyMedium,
                            color = AuraColors.TextPrimary,
                            maxLines = 1
                        )
                    }
                }

                Text(
                    text = if (pattern.highlight.isBlank()) {
                        "Not enough finished session data yet."
                    } else {
                        subtitle
                    },
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary
                )
            }
        },
        confirmButton = {
            Text(
                text = "Got it",
                color = AuraColors.YellowPrimary,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    )
}

private fun formatMinutes(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}
