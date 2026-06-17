package com.sangeeta.chronomind.ui.insights


import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.components.ChronoTimerRing
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

private val weekDays   = listOf("M", "T", "W", "T", "F", "S", "S")
private val weekData   = listOf(0.9f, 0.7f, 1.0f, 0.8f, 0.6f, 0.5f, 0.3f)

// Intensity: 0=NoData 1=Grace 2=Good 3=Focused
private val consistencyGrid = listOf(
    listOf(3, 3, 3, 2, 3, 1, 0),
    listOf(3, 2, 3, 3, 2, 1, 0),
)

@Composable
fun InsightsScreen(onBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf("Week") }

    var visible by remember { mutableStateOf(false) }
    val alpha   by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label         = "insightsAlpha"
    )
    LaunchedEffect(Unit) { visible = true }

    // Infinite glow for hero section
    val infiniteAnim = rememberInfiniteTransition(label = "insightsGlow")
    val glowAlpha    by infiniteAnim.animateFloat(
        initialValue  = 0.2f,
        targetValue   = 0.4f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iGlow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .graphicsLayer(alpha = alpha)
    ) {
        // Ambient glow
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopCenter)
                .offset(y = 60.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AuraColors.YellowPrimary.copy(alpha = glowAlpha * 0.2f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(bottom = 32.dp)
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BackButton(onClick = onBack)
                Text(
                    text  = "Your Consistency\nRhythm ✨",
                    style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.Bold),
                    color = AuraColors.TextPrimary
                )
            }

            // ── Tab row ───────────────────────────────────────────────────────
            Row(
                modifier              = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Week", "Month", "Year").forEach { tab ->
                    InsightTab(
                        label      = tab,
                        isSelected = selectedTab == tab,
                        onClick    = { selectedTab = tab }
                    )
                }
            }

            // ── Consistency heatmap grid ──────────────────────────────────────
            InsightCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Day labels
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        weekDays.forEach { day ->
                            Text(
                                text  = day,
                                style = AuraTypography.BodySmall.copy(fontSize = 10.sp),
                                color = AuraColors.TextMuted,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    // Grid rows
                    consistencyGrid.forEach { row ->
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            row.forEach { intensity ->
                                HeatCell(
                                    intensity = intensity,
                                    modifier  = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    // Legend
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        LegendItem(color = Color(0xFF2D5A27), label = "Focused")
                        LegendItem(color = AuraColors.YellowPrimary.copy(alpha = 0.6f), label = "Good")
                        LegendItem(color = Color(0xFF333320), label = "Grace Day")
                        LegendItem(color = AuraColors.SurfaceCard, label = "No Data")
                    }
                }
            }

            // ── This week stats ───────────────────────────────────────────────
            InsightCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text  = "This week",
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextMuted
                        )
                        Text(
                            text  = "Total time",
                            style = AuraTypography.LabelMedium,
                            color = AuraColors.TextSecondary
                        )
                        Text(
                            text  = "12h 30m",
                            style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.Bold),
                            color = AuraColors.TextPrimary
                        )
                    }
                    // Growth badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1A2A1A))
                            .border(1.dp, Color(0xFF2D5A27), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text  = "+28% ↑",
                            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text  = "vs last week",
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextMuted
                )
            }

            // ── Bar chart: daily breakdown ─────────────────────────────────────
            InsightCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text  = "Daily Focus",
                        style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AuraColors.TextPrimary
                    )
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        verticalAlignment     = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        weekDays.forEachIndexed { i, day ->
                            Column(
                                modifier            = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height((weekData[i] * 80).dp)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    AuraColors.YellowPrimary,
                                                    AuraColors.YellowPrimary.copy(alpha = 0.4f)
                                                )
                                            )
                                        )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text  = day,
                                    style = AuraTypography.BodySmall.copy(fontSize = 10.sp),
                                    color = AuraColors.TextMuted
                                )
                            }
                        }
                    }
                }
            }

            // ── AI Insights card ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .shadow(
                        elevation    = 16.dp,
                        shape        = RoundedCornerShape(20.dp),
                        ambientColor = AuraColors.YellowGlow,
                        spotColor    = AuraColors.YellowGlow
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1A1800),
                                Color(0xFF0D0D00)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Brush.linearGradient(
                            colors = listOf(
                                AuraColors.YellowPrimary.copy(alpha = 0.5f),
                                AuraColors.YellowPrimary.copy(alpha = 0.1f)
                            )
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment     = Alignment.Top
                ) {
                    Text("💡", fontSize = 24.sp)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text  = "You tend to focus most on weekday evenings.",
                            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Medium),
                            color = AuraColors.YellowPrimary
                        )
                        Text(
                            text  = "Reading has been your most consistent activity.",
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

// ─── Sub-composables ──────────────────────────────────────────────────────────
@Composable
private fun InsightCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1A1A), Color(0xFF111111))
                )
            )
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(20.dp))
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun InsightTab(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(
                if (isSelected) AuraColors.YellowPrimary
                else AuraColors.SurfaceCard
            )
            .border(
                1.dp,
                if (isSelected) Color.Transparent else AuraColors.CardBorderDefault,
                RoundedCornerShape(50.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text  = label,
            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = if (isSelected) AuraColors.TextOnYellow else AuraColors.TextSecondary
        )
    }
}

@Composable
private fun HeatCell(intensity: Int, modifier: Modifier = Modifier) {
    val color = when (intensity) {
        3    -> Color(0xFF2D5A27)
        2    -> AuraColors.YellowPrimary.copy(alpha = 0.5f)
        1    -> Color(0xFF2A2A15)
        else -> Color(0xFF1A1A1A)
    }
    Box(
        modifier = modifier
            .height(24.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
    )
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(
            text  = label,
            style = AuraTypography.BodySmall.copy(fontSize = 9.sp),
            color = AuraColors.TextMuted
        )
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(AuraColors.SurfaceCard)
            .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text("←", color = AuraColors.TextPrimary, style = AuraTypography.TitleMedium)
    }
}