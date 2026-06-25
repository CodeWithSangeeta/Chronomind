package com.sangeeta.chronomind.ui.home
//
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.ui.components.ChronoTimerRing
import com.sangeeta.chronomind.ui.model.ActivityUiModel

//
//@Composable
//fun HomeScreen(
//    viewModel: HomeViewModel = hiltViewModel(),
//    onNavigateToSettings: () -> Unit,
//    onNavigateToAllActivities: () -> Unit,
//    onNavigateToCreateActivity: () -> Unit,
//    onNavigateToHistory: () -> Unit,
//    onNavigateToInsights: () -> Unit,
//    onNavigateToWidgetSetup: () -> Unit,
//    onNavigateToWidgetPreview: () -> Unit
//) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    HomeScreenContent(
//        uiState = uiState,
//        onNavigateToSettings = onNavigateToSettings,
//        onNavigateToAllActivities = onNavigateToAllActivities,
//        onQuickActionClick = { action ->
//            when (action.id) {
//                "new_activity"   -> onNavigateToCreateActivity()
//                "history"        -> onNavigateToHistory()
//                "insights"       -> onNavigateToInsights()
//                "widget_setup"   -> onNavigateToWidgetSetup()
//                "widget_preview" -> onNavigateToWidgetPreview()
//            }
//        },
//        onStartFocus = viewModel::startFocus,
//        onPause = viewModel::pauseSession,
//        onFinish = viewModel::finishSession,
//        onRecentActivityClick = viewModel::onRecentActivitySelected,
//        onStartActivityDirectly = viewModel::startActivityDirectly
//    )
//}
//
//@Composable
//private fun HomeScreenContent(
//    uiState: HomeUiState,
//    onNavigateToSettings: () -> Unit,
//    onNavigateToAllActivities: () -> Unit,
//    onQuickActionClick: (HomeQuickAction) -> Unit,
//    onStartFocus: () -> Unit,
//    onPause: () -> Unit,
//    onFinish: () -> Unit,
//    onRecentActivityClick: (Int) -> Unit,
//    onStartActivityDirectly: (Int) -> Unit,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AuraColors.BackgroundDark)
//            .statusBarsPadding()
//            .windowInsetsPadding(WindowInsets.navigationBars)
//    ) {
//        HomeHeader(
//            appName = uiState.appName,
//            onSettingsClick = onNavigateToSettings,
//            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
//        )
//
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 20.dp),
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            item {
//                TimerHeroCard(
//                    runningActivity = uiState.runningActivity,
//                    selectedActivity = uiState.selectedActivity
//                )
//            }
//
//            item {
//                SessionControls(
//                    isRunning = uiState.runningActivity != null,
//                    onStartFocus = onStartFocus,
//                    onPause = onPause,
//                    onFinish = onFinish
//                )
//            }
//
//            item {
//                QuickActionsSection(
//                    actions = uiState.quickActions,
//                    onActionClick = onQuickActionClick
//                )
//            }
//
//            item {
//                SectionHeader(
//                    title = "Recent Activities",
//                    actionText = "View all",
//                    onActionClick = onNavigateToAllActivities
//                )
//            }
//
//            if (uiState.recentActivities.isEmpty()) {
//                item { EmptyRecentActivities() }
//            } else {
//                items(uiState.recentActivities, key = { it.id }) { activity ->
//                    // Shared ActivityCard — selected = yellow border, running = pause icon
//                    ActivityCard(
//                        activity = activity,
//                        isSelected = activity.id == uiState.selectedActivity?.id,
//                        onCardClick = { onRecentActivityClick(activity.id) },
//                       // onActionClick = { onRecentActivityClick(activity.id) }
//                        onActionClick = {
//                            if (activity.isRunning) {
//                                onPause()                             // if already running → pause
//                            } else {
//                                onStartActivityDirectly(activity.id)  // not running → start immediately
//                            }
//                        }
//                    )
//                }
//            }
//
//            item { Spacer(modifier = Modifier.height(6.dp)) }
//        }
//    }
//}
//
//// ─── Shared card used by BOTH HomeScreen (recent) and AllActivitiesScreen ────
//
//@Composable
//fun ActivityCard(
//    activity: ActivityUiModel,
//    isSelected: Boolean,
//    onCardClick: () -> Unit,
//    onActionClick: () -> Unit
//) {
//    val borderColor = when {
//        activity.isRunning -> AuraColors.YellowPrimary
//        isSelected         -> AuraColors.YellowPrimary.copy(alpha = 0.55f)
//        else               -> AuraColors.CardBorderDefault
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(24.dp))
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(Color(0xFF141414), AuraColors.SurfaceCard)
//                )
//            )
//            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(24.dp))
//            .clickable(onClick = onCardClick)
//            .padding(horizontal = 16.dp, vertical = 16.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(14.dp)
//        ) {
//            // Icon badge
//            Box(
//                modifier = Modifier
//                    .size(52.dp)
//                    .clip(CircleShape)
//                    .background(AuraColors.SurfaceCardLight),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = activity.icon, style = AuraTypography.TitleMedium)
//            }
//
//            // Name + meta + progress bar
//            Column(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(6.dp)
//            ) {
//                Text(
//                    text = activity.name,
//                    style = AuraTypography.TitleMedium,
//                    color = AuraColors.TextPrimary,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Text(
//                    text = "${activity.targetSeconds / 60} min • ${activity.lastActiveDate}",
//                    style = AuraTypography.BodyMedium,
//                    color = AuraColors.TextSecondary
//                )
//                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//                    Text(
//                        text = "${(activity.progress * 100).toInt()}%",
//                        style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
//                        color = AuraColors.YellowPrimary
//                    )
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(5.dp)
//                            .clip(RoundedCornerShape(50.dp))
//                            .background(AuraColors.TimerTrack)
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth(activity.progress.coerceIn(0f, 1f))
//                                .height(5.dp)
//                                .clip(RoundedCornerShape(50.dp))
//                                .background(AuraColors.YellowPrimary)
//                        )
//                    }
//                }
//            }
//
//            // Play / Pause toggle button
//            val actionIcon = if (activity.isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
//            val actionDesc = if (activity.isRunning) "Pause ${activity.name}" else "Select ${activity.name}"
//            Box(
//                modifier = Modifier
//                    .size(42.dp)
//                    .clip(CircleShape)
//                    .background(AuraColors.SurfaceCardLight)
//                    .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
//                    .clickable(onClick = onActionClick),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = actionIcon,
//                    contentDescription = actionDesc,
//                    tint = AuraColors.YellowPrimary
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//private fun HomeHeader(
//    appName: String,
//    onSettingsClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .shadow(12.dp, CircleShape, ambientColor = AuraColors.YellowGlow, spotColor = AuraColors.YellowGlow)
//                    .clip(CircleShape)
//                    .background(Brush.radialGradient(listOf(AuraColors.YellowPrimary.copy(alpha = 0.18f), AuraColors.SurfaceCard)))
//                    .border(1.dp, AuraColors.CardBorderSelected.copy(alpha = 0.7f), CircleShape),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(painterResource(R.drawable.app_logo), null, modifier = Modifier.size(42.dp))
//            }
//                Text(text = appName, style = AuraTypography.DisplayMedium, color = AuraColors.TextPrimary)
//        }
//
//        Box(
//            modifier = Modifier
//                .size(44.dp)
//                .shadow(10.dp, CircleShape, ambientColor = AuraColors.YellowGlow, spotColor = AuraColors.YellowGlow)
//                .clip(CircleShape)
//                .background(AuraColors.SurfaceCard)
//                .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
//                .clickable(onClick = onSettingsClick),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = AuraColors.TextPrimary)
//        }
//    }
//}
//
//// ─── TimerHeroCard ────────────────────────────────────────────────────────────
//
//@Composable
//private fun TimerHeroCard(
//    runningActivity: ActivityUiModel?,
//    selectedActivity: ActivityUiModel?
//) {
//    val heroActivity = runningActivity ?: selectedActivity
//    val isRunning = runningActivity != null
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(32.dp))
//            .background(Brush.verticalGradient(listOf(Color(0xFF101010), AuraColors.BackgroundDark)))
//            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(32.dp))
//            .padding(horizontal = 20.dp, vertical = 28.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(18.dp)
//        ) {
//            ChronoTimerRing(
//                progress = heroActivity?.progress ?: 0f,
//                timerText = heroActivity?.elapsedFormatted ?: "00:00",
//                subLabel = if (isRunning) "IN SESSION" else "START WHEN READY",
//                activityName = heroActivity?.name?.uppercase() ?: "NO ACTIVITY SELECTED",
//                sizeDp = 250.dp
//            )
//            if (!isRunning && heroActivity != null) {
//                Text(
//                    text = heroActivity.name,
//                    style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Medium),
//                    color = AuraColors.YellowPrimary
//                )
//            }
//        }
//    }
//}
//
//// ─── SessionControls ─────────────────────────────────────────────────────────
//
//@Composable
//private fun SessionControls(
//    isRunning: Boolean,
//    onStartFocus: () -> Unit,
//    onPause: () -> Unit,
//    onFinish: () -> Unit
//) {
//    if (!isRunning) {
//        PremiumPrimaryButton(text = "Start Focus", icon = Icons.Rounded.PlayArrow, onClick = onStartFocus)
//    } else {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            PremiumPillButton(
//                text = "Pause", icon = Icons.Rounded.Pause,
//                isPrimary = true, modifier = Modifier.weight(1f), onClick = onPause
//            )
//            PremiumPillButton(
//                text = "Finish", icon = Icons.Rounded.Stop,
//                modifier = Modifier.weight(1f), onClick = onFinish
//            )
//        }
//    }
//}
//
//// ─── QuickActionsSection ──────────────────────────────────────────────────────
//
//@Composable
//private fun QuickActionsSection(
//    actions: List<HomeQuickAction>,
//    onActionClick: (HomeQuickAction) -> Unit
//) {
//    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//        Text("Quick Actions", style = AuraTypography.LabelMedium, color = AuraColors.TextMuted)
//        Row(
//            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            actions.forEach { action ->
//                QuickActionCard(action = action, onClick = { onActionClick(action) })
//            }
//        }
//    }
//}
//
//@Composable
//private fun QuickActionCard(action: HomeQuickAction, onClick: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .width(108.dp)
//            .clip(RoundedCornerShape(22.dp))
//            .background(Brush.verticalGradient(listOf(AuraColors.SurfaceCardLight, AuraColors.SurfaceCard)))
//            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
//            .clickable(onClick = onClick)
//            .padding(horizontal = 12.dp, vertical = 18.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(14.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(42.dp)
//                .clip(CircleShape)
//                .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
//                .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.22f), CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(action.icon, contentDescription = action.title, tint = AuraColors.YellowPrimary)
//        }
//        Text(action.title, style = AuraTypography.BodyMedium, color = AuraColors.TextPrimary)
//    }
//}
//
//// ─── SectionHeader ────────────────────────────────────────────────────────────
//
//@Composable
//private fun SectionHeader(title: String, actionText: String, onActionClick: () -> Unit) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(title, style = AuraTypography.LabelMedium, color = AuraColors.TextMuted)
//        Text(
//            actionText,
//            style = AuraTypography.TitleMedium,
//            color = AuraColors.YellowPrimary,
//            modifier = Modifier.clickable(onClick = onActionClick)
//        )
//    }
//}
//
//// ─── EmptyRecentActivities ────────────────────────────────────────────────────
//
//@Composable
//private fun EmptyRecentActivities() {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(22.dp))
//            .background(AuraColors.SurfaceCard)
//            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
//            .padding(20.dp)
//    ) {
//        Text(
//            "No recent activities. Activities used today or yesterday will appear here.",
//            style = AuraTypography.BodyMedium,
//            color = AuraColors.TextSecondary
//        )
//    }
//}
//
//// ─── Buttons ──────────────────────────────────────────────────────────────────
//
//@Composable
//private fun PremiumPrimaryButton(
//    text: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    onClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(28.dp))
//            .background(Brush.horizontalGradient(listOf(AuraColors.SurfaceCard, Color(0xFF1C1707))))
//            .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.35f), RoundedCornerShape(28.dp))
//            .clickable(onClick = onClick)
//            .padding(horizontal = 22.dp, vertical = 18.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(icon, contentDescription = text, tint = AuraColors.YellowPrimary)
//        Spacer(Modifier.width(10.dp))
//        Text(text, style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold), color = AuraColors.YellowPrimary)
//    }
//}
//
//@Composable
//private fun PremiumPillButton(
//    text: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    modifier: Modifier = Modifier,
//    isPrimary: Boolean = false,
//    onClick: () -> Unit
//) {
//    Row(
//        modifier = modifier
//            .clip(RoundedCornerShape(22.dp))
//            .background(
//                if (isPrimary) Brush.horizontalGradient(listOf(AuraColors.YellowPrimary.copy(alpha = 0.18f), AuraColors.SurfaceCard))
//                else Brush.horizontalGradient(listOf(AuraColors.SurfaceCardLight, AuraColors.SurfaceCard))
//            )
//            .border(1.dp, if (isPrimary) AuraColors.YellowPrimary.copy(alpha = 0.35f) else AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
//            .clickable(onClick = onClick)
//            .padding(horizontal = 14.dp, vertical = 14.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(icon, contentDescription = text, tint = if (isPrimary) AuraColors.YellowPrimary else AuraColors.TextPrimary, modifier = Modifier.size(18.dp))
//        Spacer(Modifier.width(8.dp))
//        Text(text, style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold), color = if (isPrimary) AuraColors.YellowPrimary else AuraColors.TextPrimary)
//    }
//}
//
//
//
//





import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Timeline
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
import com.sangeeta.chronomind.ui.history.HistoryFilter
import com.sangeeta.chronomind.ui.history.HistoryRange
import com.sangeeta.chronomind.ui.history.HistorySessionUiModel
import com.sangeeta.chronomind.ui.history.HistorySort
import com.sangeeta.chronomind.ui.history.HistoryUiState
import com.sangeeta.chronomind.ui.history.HistoryViewModel
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        HistoryTopBar(onBackClick = onBackClick)

        SummaryCard(
            uiState = uiState,
            onRangeSelected = onRangeSelected,
        )

        FilterSortRow(
            selectedFilter = uiState.selectedFilter,
            selectedSort = uiState.selectedSort,
            onFilterSelected = onFilterSelected,
            onSortSelected = onSortSelected,
        )

        if (uiState.isEmpty) {
            HistoryEmptyState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 2.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
        }
    }
}

@Composable
private fun HistoryTopBar(
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CompactIconButton(
            icon = Icons.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick,
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = "History",
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary,
                maxLines = 1,
            )
            Text(
                text = "Review past focus sessions and completion consistency",
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary,
                maxLines = 2,
            )
        }
    }
}

@Composable
private fun SummaryCard(
    uiState: HistoryUiState,
    onRangeSelected: (HistoryRange) -> Unit,
) {
    var rangeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF101010), Color(0xFF0C0C0C))
                )
            )
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Session overview",
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary,
                    maxLines = 1,
                )
                Text(
                    text = uiState.summary.rangeText,
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextSecondary,
                    maxLines = 1,
                )
            }

            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF121212))
                        .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(12.dp))
                        .clickable { rangeExpanded = true }
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = uiState.selectedRange.label,
                        style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                        color = AuraColors.TextPrimary,
                    )
                    Icon(
                        imageVector = Icons.Rounded.ExpandMore,
                        contentDescription = null,
                        tint = AuraColors.TextSecondary,
                        modifier = Modifier.size(16.dp),
                    )
                }

                DropdownMenu(
                    expanded = rangeExpanded,
                    onDismissRequest = { rangeExpanded = false },
                    containerColor = AuraColors.SurfaceCard,
                ) {
                    HistoryRange.entries.forEach { range ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = range.label,
                                    color = if (range == uiState.selectedRange) AuraColors.YellowPrimary else AuraColors.TextPrimary,
                                    style = AuraTypography.BodyMedium,
                                )
                            },
                            onClick = {
                                rangeExpanded = false
                                onRangeSelected(range)
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SummaryMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.Timeline,
                value = uiState.summary.totalSessions.toString(),
                label = "Sessions",
            )
            SummaryMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.AccessTime,
                value = uiState.summary.totalFocusTime,
                label = "Focus time",
            )
            SummaryMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.CheckCircle,
                value = "${uiState.summary.completionRate}%",
                label = "Completion",
            )
        }
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
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF111111))
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .shadow(6.dp, CircleShape, ambientColor = AuraColors.YellowGlow, spotColor = AuraColors.YellowGlow)
                .clip(CircleShape)
                .background(Color(0xFF18120A))
                .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.24f), CircleShape),
            contentAlignment = Alignment.Center
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
                .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
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
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(20.dp))
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
            Icon(
                imageVector = session.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )

        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = session.activityName,
                style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold),
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
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
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
            .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
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
