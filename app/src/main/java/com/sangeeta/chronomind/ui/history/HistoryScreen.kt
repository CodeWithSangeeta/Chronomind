//package com.sangeeta.chronomind.ui.history
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.navigationBars
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.windowInsetsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.AccessTime
//import androidx.compose.material.icons.rounded.ArrowBack
//import androidx.compose.material.icons.rounded.CheckCircle
//import androidx.compose.material.icons.rounded.ExpandMore
//import androidx.compose.material.icons.rounded.FilterList
//import androidx.compose.material.icons.rounded.Timeline
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.sangeeta.chronomind.ui.theme.AuraColors
//import com.sangeeta.chronomind.ui.theme.AuraTypography
//
//@Composable
//fun HistoryScreen(
//    viewModel: HistoryViewModel = hiltViewModel(),
//    onBackClick: () -> Unit
//) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    HistoryScreenContent(
//        uiState = uiState,
//        onBackClick = onBackClick,
//        onRangeSelected = viewModel::onRangeSelected,
//        onFilterSelected = viewModel::onFilterSelected,
//        onSortSelected = viewModel::onSortSelected
//    )
//}
//
//@Composable
//private fun HistoryScreenContent(
//    uiState: HistoryUiState,
//    onBackClick: () -> Unit,
//    onRangeSelected: (HistoryRange) -> Unit,
//    onFilterSelected: (HistoryFilter) -> Unit,
//    onSortSelected: (HistorySort) -> Unit
//) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AuraColors.BackgroundDark)
//            .statusBarsPadding()
//            .windowInsetsPadding(WindowInsets.navigationBars),
//        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
//        verticalArrangement = Arrangement.spacedBy(18.dp)
//    ) {
//        item {
//            HistoryTopBar(onBackClick = onBackClick)
//        }
//
//        item {
//            SummaryCard(
//                uiState = uiState,
//                onRangeSelected = onRangeSelected
//            )
//        }
//
//        item {
//            FilterSortRow(
//                selectedFilter = uiState.selectedFilter,
//                selectedSort = uiState.selectedSort,
//                onFilterSelected = onFilterSelected,
//                onSortSelected = onSortSelected
//            )
//        }
//
//        if (uiState.isEmpty) {
//            item {
//                HistoryEmptyState()
//            }
//        } else {
//            uiState.groupedSessions.forEach { group ->
//                item {
//                    DateGroupHeader(
//                        title = group.title,
//                        subtitleDate = group.subtitleDate,
//                        sessionCount = group.sessions.size
//                    )
//                }
//
//                items(group.sessions, key = { it.id }) { session ->
//                    HistorySessionCard(session = session)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun HistoryTopBar(
//    onBackClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        CompactIconButton(
//            icon = Icons.Rounded.ArrowBack,
//            contentDescription = "Back",
//            onClick = onBackClick
//        )
//
//        Spacer(modifier = Modifier.width(14.dp))
//
//        Text(
//            text = "History",
//            style = AuraTypography.DisplayMedium,
//            color = AuraColors.TextPrimary
//        )
//    }
//}
//
//@Composable
//private fun SummaryCard(
//    uiState: HistoryUiState,
//    onRangeSelected: (HistoryRange) -> Unit
//) {
//    var rangeExpanded by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(26.dp))
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(Color(0xFF101010), Color(0xFF0C0C0C))
//                )
//            )
//            .border(
//                width = 1.dp,
//                color = AuraColors.CardBorderDefault,
//                shape = RoundedCornerShape(26.dp)
//            )
//            .padding(18.dp),
//        verticalArrangement = Arrangement.spacedBy(18.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box {
//                Row(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(14.dp))
//                        .clickable { rangeExpanded = true }
//                        .padding(horizontal = 4.dp, vertical = 2.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = uiState.selectedRange.label,
//                        style = AuraTypography.TitleMedium,
//                        color = AuraColors.TextPrimary
//                    )
//                    Icon(
//                        imageVector = Icons.Rounded.ExpandMore,
//                        contentDescription = null,
//                        tint = AuraColors.TextSecondary
//                    )
//                }
//
//                DropdownMenu(
//                    expanded = rangeExpanded,
//                    onDismissRequest = { rangeExpanded = false },
//                    containerColor = AuraColors.SurfaceCard
//                ) {
//                    HistoryRange.entries.forEach { range ->
//                        DropdownMenuItem(
//                            text = {
//                                Text(
//                                    text = range.label,
//                                    color = if (range == uiState.selectedRange) {
//                                        AuraColors.YellowPrimary
//                                    } else {
//                                        AuraColors.TextPrimary
//                                    }
//                                )
//                            },
//                            onClick = {
//                                rangeExpanded = false
//                                onRangeSelected(range)
//                            }
//                        )
//                    }
//                }
//            }
//
//            Text(
//                text = uiState.summary.rangeText,
//                style = AuraTypography.BodyMedium,
//                color = AuraColors.TextSecondary
//            )
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            SummaryMetric(
//                icon = Icons.Rounded.Timeline,
//                value = uiState.summary.totalSessions.toString(),
//                label = "Sessions"
//            )
//
//            SummaryDivider()
//
//            SummaryMetric(
//                icon = Icons.Rounded.AccessTime,
//                value = uiState.summary.totalFocusTime,
//                label = "Focus Time"
//            )
//
//            SummaryDivider()
//
//            SummaryMetric(
//                icon = Icons.Rounded.CheckCircle,
//                value = "${uiState.summary.completionRate}%",
//                label = "Completion"
//            )
//        }
//    }
//}
//
//@Composable
//private fun SummaryMetric(
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    value: String,
//    label: String
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(40.dp)
//                .shadow(
//                    elevation = 8.dp,
//                    shape = CircleShape,
//                    ambientColor = AuraColors.YellowGlow,
//                    spotColor = AuraColors.YellowGlow
//                )
//                .clip(CircleShape)
//                .background(Color(0xFF18120A))
//                .border(
//                    width = 1.dp,
//                    color = AuraColors.YellowPrimary.copy(alpha = 0.30f),
//                    shape = CircleShape
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = null,
//                tint = AuraColors.YellowPrimary
//            )
//        }
//
//        Text(
//            text = value,
//            style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.SemiBold),
//            color = AuraColors.TextPrimary
//        )
//
//        Text(
//            text = label,
//            style = AuraTypography.BodyMedium,
//            color = AuraColors.TextSecondary
//        )
//    }
//}
//
//@Composable
//private fun SummaryDivider() {
//    Box(
//        modifier = Modifier
//            .width(1.dp)
//            .size(1.dp, 72.dp)
//            .background(AuraColors.CardBorderDefault)
//    )
//}
//
//@Composable
//private fun FilterSortRow(
//    selectedFilter: HistoryFilter,
//    selectedSort: HistorySort,
//    onFilterSelected: (HistoryFilter) -> Unit,
//    onSortSelected: (HistorySort) -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(14.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Row(
//            modifier = Modifier
//                .weight(1f)
//                .clip(RoundedCornerShape(20.dp))
//                .background(Color(0xFF111111))
//                .padding(4.dp),
//            horizontalArrangement = Arrangement.spacedBy(6.dp)
//        ) {
//            HistoryFilter.entries.forEach { filter ->
//                val selected = filter == selectedFilter
//                Row(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(16.dp))
//                        .background(
//                            if (selected) Color(0xFF1A1408) else Color.Transparent
//                        )
//                        .border(
//                            width = if (selected) 1.dp else 0.dp,
//                            color = if (selected) {
//                                AuraColors.YellowPrimary.copy(alpha = 0.28f)
//                            } else {
//                                Color.Transparent
//                            },
//                            shape = RoundedCornerShape(16.dp)
//                        )
//                        .clickable { onFilterSelected(filter) }
//                        .padding(horizontal = 14.dp, vertical = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Rounded.FilterList,
//                        contentDescription = null,
//                        tint = if (selected) AuraColors.YellowPrimary else AuraColors.TextSecondary,
//                        modifier = Modifier.size(16.dp)
//                    )
//                    Text(
//                        text = filter.label,
//                        style = AuraTypography.TitleMedium,
//                        color = if (selected) AuraColors.YellowPrimary else AuraColors.TextPrimary
//                    )
//                }
//            }
//        }
//
//        SortMenu(
//            selectedSort = selectedSort,
//            onSortSelected = onSortSelected
//        )
//    }
//}
//
//@Composable
//private fun SortMenu(
//    selectedSort: HistorySort,
//    onSortSelected: (HistorySort) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Box {
//        Row(
//            modifier = Modifier
//                .clip(RoundedCornerShape(18.dp))
//                .background(Color(0xFF111111))
//                .border(
//                    width = 1.dp,
//                    color = AuraColors.CardBorderDefault,
//                    shape = RoundedCornerShape(18.dp)
//                )
//                .clickable { expanded = true }
//                .padding(horizontal = 16.dp, vertical = 14.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = selectedSort.label,
//                style = AuraTypography.TitleMedium,
//                color = AuraColors.TextPrimary
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Icon(
//                imageVector = Icons.Rounded.ExpandMore,
//                contentDescription = null,
//                tint = AuraColors.TextSecondary
//            )
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            containerColor = AuraColors.SurfaceCard
//        ) {
//            HistorySort.entries.forEach { sort ->
//                DropdownMenuItem(
//                    text = {
//                        Text(
//                            text = sort.label,
//                            color = if (sort == selectedSort) {
//                                AuraColors.YellowPrimary
//                            } else {
//                                AuraColors.TextPrimary
//                            }
//                        )
//                    },
//                    onClick = {
//                        expanded = false
//                        onSortSelected(sort)
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun DateGroupHeader(
//    title: String,
//    subtitleDate: String,
//    sessionCount: Int
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.Bottom
//    ) {
//        Text(
//            text = "$title • $subtitleDate",
//            style = AuraTypography.DisplayMedium,
//            color = AuraColors.TextPrimary
//        )
//
//        Text(
//            text = "$sessionCount sessions",
//            style = AuraTypography.BodyMedium,
//            color = AuraColors.TextSecondary
//        )
//    }
//}
//
//@Composable
//private fun HistorySessionCard(
//    session: HistorySessionUiModel
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(22.dp))
//            .background(
//                Brush.horizontalGradient(
//                    colors = listOf(Color(0xFF111111), Color(0xFF0D0D0D))
//                )
//            )
//            .border(
//                width = 1.dp,
//                color = AuraColors.CardBorderDefault,
//                shape = RoundedCornerShape(22.dp)
//            )
//            .padding(horizontal = 16.dp, vertical = 16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(14.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(50.dp)
//                .shadow(
//                    elevation = 10.dp,
//                    shape = CircleShape,
//                    ambientColor = session.accentColor.copy(alpha = 0.30f),
//                    spotColor = session.accentColor.copy(alpha = 0.30f)
//                )
//                .clip(CircleShape)
//                .background(
//                    Brush.radialGradient(
//                        colors = listOf(
//                            session.accentColor.copy(alpha = 0.95f),
//                            session.accentColor.copy(alpha = 0.55f)
//                        )
//                    )
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = session.iconEmoji,
//                style = AuraTypography.TitleMedium
//            )
//        }
//
//        Column(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(6.dp)
//        ) {
//            Text(
//                text = session.activityName,
//                style = AuraTypography.TitleMedium,
//                color = AuraColors.TextPrimary
//            )
//
//            Text(
//                text = session.durationText,
//                style = AuraTypography.BodyMedium,
//                color = AuraColors.TextSecondary
//            )
//        }
//
//        Box(
//            modifier = Modifier
//                .clip(RoundedCornerShape(14.dp))
//                .background(
//                    if (session.isCompleted) Color(0xFF132114) else Color(0xFF20170C)
//                )
//                .border(
//                    width = 1.dp,
//                    color = if (session.isCompleted) {
//                        Color(0xFF29472B)
//                    } else {
//                        Color(0xFF5A4120)
//                    },
//                    shape = RoundedCornerShape(14.dp)
//                )
//                .padding(horizontal = 12.dp, vertical = 8.dp)
//        ) {
//            Text(
//                text = session.statusLabel,
//                style = AuraTypography.BodyMedium,
//                color = if (session.isCompleted) Color(0xFF7FDB7B) else Color(0xFFF0B04C)
//            )
//        }
//    }
//}
//
//@Composable
//private fun HistoryEmptyState() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(24.dp))
//            .background(Color(0xFF101010))
//            .border(
//                width = 1.dp,
//                color = AuraColors.CardBorderDefault,
//                shape = RoundedCornerShape(24.dp)
//            )
//            .padding(24.dp),
//        verticalArrangement = Arrangement.spacedBy(10.dp)
//    ) {
//        Text(
//            text = "No sessions found",
//            style = AuraTypography.TitleMedium,
//            color = AuraColors.TextPrimary
//        )
//        Text(
//            text = "Try a different filter or range to view your past focus sessions.",
//            style = AuraTypography.BodyMedium,
//            color = AuraColors.TextSecondary
//        )
//    }
//}
//
//@Composable
//private fun CompactIconButton(
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    contentDescription: String,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .size(44.dp)
//            .clip(CircleShape)
//            .background(Color(0xFF111111))
//            .border(
//                width = 1.dp,
//                color = AuraColors.CardBorderDefault,
//                shape = CircleShape
//            )
//            .clickable(onClick = onClick),
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = contentDescription,
//            tint = AuraColors.TextPrimary
//        )
//    }
//}




package com.sangeeta.chronomind.ui.history

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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRangeSelected = viewModel::onRangeSelected,
        onFilterSelected = viewModel::onFilterSelected,
        onSortSelected = viewModel::onSortSelected,
    )
}

@Composable
private fun HistoryScreenContent(
    uiState: HistoryUiState,
    onBackClick: () -> Unit,
    onRangeSelected: (HistoryRange) -> Unit,
    onFilterSelected: (HistoryFilter) -> Unit,
    onSortSelected: (HistorySort) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { HistoryTopBar(onBackClick = onBackClick) }
        item {
            HistoryRangeTabs(
                selected = uiState.selectedRange,
                onSelected = onRangeSelected,
            )
        }
        item { SummarySection(summary = uiState.summary) }
        item {
            FilterSortRow(
                selectedFilter = uiState.selectedFilter,
                selectedSort = uiState.selectedSort,
                onFilterSelected = onFilterSelected,
                onSortSelected = onSortSelected,
            )
        }

        if (uiState.isEmpty) {
            item { HistoryEmptyState() }
        } else {
            uiState.groupedSessions.forEach { group ->
                item {
                    DateGroupHeader(
                        title = group.title,
                        subtitleDate = group.subtitleDate,
                        sessionCount = group.sessions.size,
                    )
                }
                items(group.sessions, key = { it.id }) { session ->
                    HistorySessionCard(session = session)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(4.dp)) }
    }
}

@Composable
private fun HistoryTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        CompactIconButton(
            icon = Icons.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "History",
                style = AuraTypography.TitleMedium,////
                color = AuraColors.TextPrimary,
                maxLines = 1,
            )
            Text(
                text = "Track your completed and missed sessions",
                style = AuraTypography.BodySmall,
                color = AuraColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun HistoryRangeTabs(
    selected: HistoryRange,
    onSelected: (HistoryRange) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF101010))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        HistoryRange.entries.forEach { range ->
            val active = range == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (active) Color(0xFF1A1408) else Color.Transparent)
                    .border(
                        width = if (active) 1.dp else 0.dp,
                        color = if (active) AuraColors.YellowPrimary.copy(alpha = 0.30f) else Color.Transparent,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .clickable { onSelected(range) }
                    .padding(vertical = 11.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = range.label,
                    style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (active) AuraColors.YellowPrimary else AuraColors.TextPrimary,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun SummarySection(summary: HistorySummary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SummaryMetric(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.History,
            value = summary.totalSessions.toString(),
            label = "Sessions",
        )
        SummaryDivider()
        SummaryMetric(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.Timer,
            value = summary.totalFocusTime,
            label = "Focus Time",
        )
        SummaryDivider()
        SummaryMetric(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.TrackChanges,
            value = "${summary.completionRate}%",
            label = "Completion",
        )
    }
}

@Composable
private fun SummaryMetric(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape,
                    ambientColor = AuraColors.YellowGlow,
                    spotColor = AuraColors.YellowGlow,
                )
                .clip(CircleShape)
                .background(Color(0xFF18120A))
                .border(
                    width = 1.dp,
                    color = AuraColors.YellowPrimary.copy(alpha = 0.28f),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(16.dp),
            )
        }
        Text(
            text = value,
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.TextPrimary,
            maxLines = 1,
        )
        Text(
            text = label,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary,
            maxLines = 1,
        )
    }
}

@Composable
private fun SummaryDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(60.dp)
            .background(AuraColors.CardBorderDefault),
    )
}

@Composable
private fun FilterSortRow(
    selectedFilter: HistoryFilter,
    selectedSort: HistorySort,
    onFilterSelected: (HistoryFilter) -> Unit,
    onSortSelected: (HistorySort) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState())
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF111111))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            HistoryFilter.entries.forEach { filter ->
                val selected = filter == selectedFilter
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (selected) Color(0xFF1A1408) else Color.Transparent)
                        .border(
                            width = if (selected) 1.dp else 0.dp,
                            color = if (selected) AuraColors.YellowPrimary.copy(alpha = 0.28f) else Color.Transparent,
                            shape = RoundedCornerShape(14.dp),
                        )
                        .clickable { onFilterSelected(filter) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FilterList,
                        contentDescription = null,
                        tint = if (selected) AuraColors.YellowPrimary else AuraColors.TextSecondary,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = filter.label,
                        style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                        color = if (selected) AuraColors.YellowPrimary else AuraColors.TextPrimary,
                        maxLines = 1,
                    )
                }
            }
        }

        SortMenu(
            selectedSort = selectedSort,
            onSortSelected = onSortSelected,
        )
    }
}

@Composable
private fun SortMenu(
    selectedSort: HistorySort,
    onSortSelected: (HistorySort) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF111111))
                .border(
                    width = 1.dp,
                    color = AuraColors.CardBorderDefault,
                    shape = RoundedCornerShape(16.dp),
                )
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedSort.label,
                style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                color = AuraColors.TextPrimary,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Rounded.ExpandMore,
                contentDescription = null,
                tint = AuraColors.TextSecondary,
                modifier = Modifier.size(18.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = AuraColors.SurfaceCard,
        ) {
            HistorySort.entries.forEach { sort ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = sort.label,
                            color = if (sort == selectedSort) AuraColors.YellowPrimary else AuraColors.TextPrimary,
                            style = AuraTypography.BodyMedium,
                        )
                    },
                    onClick = {
                        expanded = false
                        onSortSelected(sort)
                    },
                )
            }
        }
    }
}

@Composable
private fun DateGroupHeader(
    title: String,
    subtitleDate: String,
    sessionCount: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = title,
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.TextPrimary,
            maxLines = 1,
        )
        Text(
            text = subtitleDate,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary,
            maxLines = 1,
        )
    }
        Text(
            text = "$sessionCount sessions",
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary,
            maxLines = 1,
        )
    }
}

@Composable
private fun HistorySessionCard(session: HistorySessionUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFF111111), Color(0xFF0D0D0D)),
                ),
            )
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    ambientColor = session.accentColor.copy(alpha = 0.28f),
                    spotColor = session.accentColor.copy(alpha = 0.28f),
                )
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            session.accentColor.copy(alpha = 0.95f),
                            session.accentColor.copy(alpha = 0.55f),
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = session.iconEmoji,
                style = AuraTypography.BodyMedium,////
                maxLines = 1,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = session.activityName,
                style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold), ////
                color = AuraColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = session.durationText,
                style = AuraTypography.BodySmall,
                color = AuraColors.TextSecondary,
                maxLines = 1,
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (session.isCompleted) Color(0xFF132114) else Color(0xFF20170C))
                .border(
                    width = 1.dp,
                    color = if (session.isCompleted) Color(0xFF29472B) else Color(0xFF5A4120),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
        ) {
            Text(
                text = session.statusLabel,
                style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                color = if (session.isCompleted) Color(0xFF7FDB7B) else Color(0xFFF0B04C),
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun HistoryEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF101010))
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(22.dp),
            )
            .padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "No sessions found",
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary,
        )
        Text(
            text = "Try a different filter or range to view your past focus sessions.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary,
        )
    }
}

@Composable
private fun CompactIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color(0xFF111111))
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = AuraColors.TextPrimary,
            modifier = Modifier.size(18.dp),
        )
    }
}