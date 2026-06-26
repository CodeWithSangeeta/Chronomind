package com.sangeeta.chronomind.ui.insights

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InsightsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRangeSelected = viewModel::onRangeSelected
    )
}

@Composable
private fun InsightsScreenContent(
    uiState: InsightsUiState,
    onBackClick: () -> Unit,
    onRangeSelected: (InsightsRange) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            InsightsTopBar(onBackClick = onBackClick)
        }

        item {
            HeaderCaption()
        }

        item {
            RangeTabs(
                selected = uiState.selectedRange,
                onSelected = onRangeSelected
            )
        }

        item {
            SummaryRow(summary = uiState.summary)
        }

        item {
            TrendCard(
                selectedRange = uiState.selectedRange,
                trend = uiState.trend
            )
        }

        item {
            ConsistencyCard(consistency = uiState.consistency)
        }

        item {
            PatternHighlightCard(
                selectedRange = uiState.selectedRange,
                pattern = uiState.pattern
            )
        }

        item {
            TopActivitiesCard(activities = uiState.topActivities)
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun InsightsTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularHeaderButton(
            icon = Icons.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Insights",
                style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraColors.TextPrimary,
                maxLines = 1
            )
            Text(
                text = "Your focus performance",
                style = AuraTypography.BodySmall,
                color = AuraColors.TextSecondary,
                maxLines = 1
            )
        }

        CircularHeaderButton(
            icon = Icons.Rounded.Info,
            contentDescription = "Insights info",
            onClick = { },
            iconTint = AuraColors.YellowPrimary
        )
    }
}

@Composable
private fun HeaderCaption() {
    Text(
        text = "Focus trends, completion rate, consistency, and your top-performing activities.",
        style = AuraTypography.BodySmall,
        color = AuraColors.TextSecondary
    )
}

@Composable
private fun CircularHeaderButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    iconTint: Color = AuraColors.TextPrimary
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color(0xFF111111))
            .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun RangeTabs(
    selected: InsightsRange,
    onSelected: (InsightsRange) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF101010))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        InsightsRange.entries.forEach { range ->
            val active = range == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (active) Color(0xFF1B1408) else Color.Transparent)
                    .border(
                        width = if (active) 1.dp else 0.dp,
                        color = if (active) AuraColors.YellowPrimary.copy(alpha = 0.30f) else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onSelected(range) }
                    .padding(vertical = 11.dp),
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
private fun SummaryRow(summary: InsightsSummary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SummaryCardItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.AccessTime,
            title = "Focus",
            value = summary.totalFocusTime,
            delta = summary.totalFocusDelta
        )
        SummaryCardItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.CheckCircle,
            title = "Sessions",
            value = summary.sessionsCompleted.toString(),
            delta = summary.sessionsDelta
        )
        SummaryCardItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.TrackChanges,
            title = "Rate",
            value = "${summary.completionRate}%",
            delta = summary.completionDelta
        )
    }
}

@Composable
private fun SummaryCardItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    delta: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF101010))
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = AuraColors.YellowGlow,
                    spotColor = AuraColors.YellowGlow
                )
                .clip(CircleShape)
                .background(Color(0xFF18120A))
                .border(
                    1.dp,
                    AuraColors.YellowPrimary.copy(alpha = 0.28f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = title,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = value,
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.TextPrimary,
            maxLines = 1
        )

        Text(
            text = "$delta vs previous",
            style = AuraTypography.BodySmall,
            color = AuraColors.YellowPrimary,
            maxLines = 2
        )
    }
}

@Composable
private fun TrendCard(
    selectedRange: InsightsRange,
    trend: TrendChartUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF101010))
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
                        InsightsRange.WEEK -> "Daily"
                        InsightsRange.MONTH -> "Weekly"
                        InsightsRange.YEAR -> "Monthly"
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
    }
}

@Composable
private fun TrendBarChart(
    points: List<TrendPointUiModel>,
    highlightedIndex: Int
) {
    val maxValue = (points.maxOfOrNull { it.valueMinutes } ?: 1).toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(208.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                points.forEachIndexed { index, point ->
                    val fraction = point.valueMinutes / maxValue
                    val active = index == highlightedIndex

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        if (active) {
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AuraColors.YellowPrimary)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = formatMinutes(point.valueMinutes),
                                    style = AuraTypography.BodySmall,
                                    color = Color(0xFF181818)
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        Box(
                            modifier = Modifier
                                .width(28.dp)
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
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            points.forEach { point ->
                Text(
                    text = point.label,
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ConsistencyCard(
    consistency: ConsistencyUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF101010))
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ConsistencyMetric(
                icon = Icons.Rounded.LocalFireDepartment,
                value = consistency.currentStreak.toString(),
                label = "Current Streak",
                subLabel = "days"
            )

            VerticalDivider()

            ConsistencyMetric(
                icon = Icons.Rounded.Timeline,
                value = consistency.longestStreak.toString(),
                label = "Longest Streak",
                subLabel = "days"
            )

            VerticalDivider()

            CircularConsistencyMeter(
                percent = consistency.consistencyPercent,
                footer = consistency.completedDaysText
            )
        }
    }
}

@Composable
private fun ConsistencyMetric(
    icon: ImageVector,
    value: String,
    label: String,
    subLabel: String
) {
    Row(
        modifier = Modifier.widthIn(max = 112.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF17120A))
                .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.28f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                text = value,
                style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraColors.TextPrimary,
                maxLines = 1
            )
            Text(
                text = label,
                style = AuraTypography.BodySmall,
                color = AuraColors.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subLabel,
                style = AuraTypography.BodySmall,
                color = AuraColors.YellowPrimary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(54.dp)
            .background(Color.White.copy(alpha = 0.08f))
    )
}

@Composable
private fun CircularConsistencyMeter(
    percent: Int,
    footer: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier.size(74.dp),
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
            color = AuraColors.TextSecondary
        )
        Text(
            text = footer,
            style = AuraTypography.BodySmall,
            color = AuraColors.YellowPrimary,
            maxLines = 1
        )
    }
}

@Composable
private fun PatternHighlightCard(
    selectedRange: InsightsRange,
    pattern: PatternInsightUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF101010))
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF18120A))
                    .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.28f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Timeline,
                    contentDescription = null,
                    tint = AuraColors.YellowPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = "Best Pattern",
                    style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AuraColors.TextPrimary
                )
                Text(
                    text = when (selectedRange) {
                        InsightsRange.WEEK -> "This week"
                        InsightsRange.MONTH -> "This month"
                        InsightsRange.YEAR -> "This year"
                    },
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextSecondary
                )
            }
        }

        Text(
            text = pattern.highlight,
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.YellowPrimary
        )

        Text(
            text = pattern.value,
            style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.Medium),
            color = AuraColors.TextPrimary
        )

        Text(
            text = pattern.description,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary
        )
    }
}

@Composable
private fun TopActivitiesCard(
    activities: List<TopActivityUiModel>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF101010))
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Top Activities",
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.TextPrimary
        )

        activities.forEachIndexed { index, activity ->
            TopActivityRow(activity = activity)
            if (index != activities.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.06f))
                )
            }
        }
    }
}

@Composable
private fun TopActivityRow(
    activity: TopActivityUiModel
) {
    val icon = iconForActivity(activity.name)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF181818))
                    .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.26f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = activity.rank.toString(),
                    style = AuraTypography.BodySmall,
                    color = AuraColors.YellowPrimary
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF151515)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = activity.name,
                    tint = AuraColors.TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.name,
                    style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AuraColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = activity.totalFocusTime,
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextSecondary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "${activity.sharePercent}%",
                style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                color = AuraColors.YellowPrimary,
                maxLines = 1
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFF1B1B1B))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(activity.sharePercent / 100f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFF6CF45), Color(0xFFBE8707))
                        )
                    )
            )
        }
    }
}

private fun iconForActivity(name: String): ImageVector {
    val key = name.lowercase()
    return when {
        "study" in key || "learn" in key -> Icons.Rounded.Book
        "workout" in key || "fitness" in key || "gym" in key -> Icons.Rounded.FitnessCenter
        "code" in key || "coding" in key || "program" in key -> Icons.Rounded.Code
        "write" in key || "journal" in key -> Icons.Rounded.Edit
        "client" in key || "work" in key -> Icons.Rounded.Work
        else -> Icons.Rounded.Timeline
    }
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