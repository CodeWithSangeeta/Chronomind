package com.sangeeta.chronomind.ui.history

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

private data class HistoryEntry(
    val activityName: String,
    val icon:         String,
    val duration:     String,
    val mood:         String,
    val date:         String,
    val streak:       Boolean
)

private val historyData = listOf(
    HistoryEntry("Study",      "📖", "1h 15m", "Deep Focus",           "Today",       true),
    HistoryEntry("Exercise",   "🏋️", "35m",    "Restorative & Calm",   "Today",       true),
    HistoryEntry("Reading",    "📚", "20m",    "Slightly Distracted",  "Today",       true),
    HistoryEntry("Meditation", "🪷", "10m",    "Deep Focus",           "Yesterday",   true),
    HistoryEntry("Study",      "📖", "2h",     "Deep Focus",           "Jun 14, 2025",true),
    HistoryEntry("Exercise",   "🏋️", "45m",    "Deep Focus",           "Jun 14, 2025",true),
    HistoryEntry("Reading",    "📚", "30m",    "Restorative & Calm",   "Jun 13, 2025",true),
    HistoryEntry("Study",      "📖", "1h 30m", "Slightly Distracted",  "Jun 13, 2025",false)
)

private val filterTabs = listOf("All", "Study", "Exercise", "Reading")

@Composable
fun HistoryScreen(onBack: () -> Unit) {
    var selectedFilter by remember { mutableStateOf("All") }

    val filtered = remember(selectedFilter) {
        if (selectedFilter == "All") historyData
        else historyData.filter { it.activityName == selectedFilter }
    }

    val grouped = filtered.groupBy { it.date }

    var visible by remember { mutableStateOf(false) }
    val alpha   by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label         = "histAlpha"
    )
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .graphicsLayer(alpha = alpha)
    ) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // ── Header ─────────────────────────────────────────────────────────
            item {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BackButton(onClick = onBack)
                        Text(
                            text  = "History",
                            style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.Bold),
                            color = AuraColors.TextPrimary
                        )
                    }
                    // Menu icon
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AuraColors.SurfaceCard)
                            .border(1.dp, AuraColors.CardBorderDefault, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⋯", color = AuraColors.TextPrimary, style = AuraTypography.TitleMedium)
                    }
                }
            }

            // ── Filter tabs ────────────────────────────────────────────────────
            item {
                Row(
                    modifier              = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterTabs.forEach { tab ->
                        FilterChip(
                            label      = tab,
                            isSelected = selectedFilter == tab,
                            onClick    = { selectedFilter = tab }
                        )
                    }
                }
            }

            // ── Grouped entries ────────────────────────────────────────────────
            grouped.forEach { (date, entries) ->
                item {
                    Text(
                        text     = date,
                        style    = AuraTypography.LabelMedium.copy(
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color    = AuraColors.TextMuted,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
                items(entries) { entry ->
                    HistoryEntryCard(
                        entry    = entry,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .shadow(
                elevation    = if (isSelected) 8.dp else 0.dp,
                shape        = RoundedCornerShape(50.dp),
                ambientColor = AuraColors.YellowGlow,
                spotColor    = AuraColors.YellowGlow
            )
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
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Text(
            text  = label,
            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = if (isSelected) AuraColors.TextOnYellow else AuraColors.TextSecondary
        )
    }
}

@Composable
private fun HistoryEntryCard(entry: HistoryEntry, modifier: Modifier = Modifier) {
    val moodColor = when (entry.mood) {
        "Deep Focus"          -> AuraColors.YellowPrimary
        "Restorative & Calm"  -> Color(0xFF4ECDC4)
        "Slightly Distracted" -> Color(0xFFFF8C00)
        else                  -> AuraColors.TextMuted
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF1A1A1A), Color(0xFF111111))
                )
            )
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Activity icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AuraColors.SurfaceCard),
                contentAlignment = Alignment.Center
            ) {
                Text(entry.icon, fontSize = 18.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = entry.activityName,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Mood dot
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(moodColor)
                    )
                    Text(
                        text  = entry.mood,
                        style = AuraTypography.BodySmall,
                        color = AuraColors.TextMuted
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text  = entry.duration,
                    style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
                    color = AuraColors.TextPrimary
                )
                if (entry.streak) {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🔥", fontSize = 14.sp)
                    }
                }
            }
        }
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