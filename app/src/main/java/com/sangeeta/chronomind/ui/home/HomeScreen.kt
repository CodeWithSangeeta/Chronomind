//package com.sangeeta.chronomind.ui.home
//
//
//import HomeViewModel
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.navigationBars
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.foundation.layout.consumeWindowInsets
//import androidx.compose.foundation.layout.safeDrawing
//import androidx.compose.foundation.layout.windowInsetsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.PlayArrow
//import androidx.compose.material.icons.rounded.Settings
//import androidx.compose.material.icons.rounded.Pause
//import androidx.compose.material.icons.rounded.Stop
//import androidx.compose.material.icons.rounded.SyncAlt
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.sangeeta.chronomind.R
//import com.sangeeta.chronomind.ui.components.ChronoTimerRing
//import com.sangeeta.chronomind.ui.model.ActivityUiModel
//import com.sangeeta.chronomind.ui.theme.AuraColors
//import com.sangeeta.chronomind.ui.theme.AuraTypography
//
////@Composable
////fun HomeScreen(
////    viewModel: HomeViewModel = hiltViewModel(),
////    onNavigateToSettings: () -> Unit,
////    onNavigateToAllActivities: () -> Unit,
////    onNavigateToCreateActivity: () -> Unit,
////    onNavigateToHistory: () -> Unit,
////    onNavigateToInsights: () -> Unit,
////    onNavigateToWidgetSetup: () -> Unit,
////    onNavigateToWidgetPreview: () -> Unit
////) {
////    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
////
////    HomeScreenContent(
////        uiState = uiState,
////        onNavigateToSettings = onNavigateToSettings,
////        onNavigateToAllActivities = onNavigateToAllActivities,
////        onQuickActionClick = { action ->
////            when (action.id) {
////                "new_activity" -> onNavigateToCreateActivity()
////                "history" -> onNavigateToHistory()
////                "insights" -> onNavigateToInsights()
////                "widget_setup" -> onNavigateToWidgetSetup()
////                "widget_preview" -> onNavigateToWidgetPreview()
////            }
////        },
////        onStartFocus = viewModel::startFocus,
////        onPause = viewModel::pauseSession,
////        onFinish = viewModel::finishSession,
////    )
////}
//
//
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
//                "new_activity" -> onNavigateToCreateActivity()
//                "history" -> onNavigateToHistory()
//                "insights" -> onNavigateToInsights()
//                "widget_setup" -> onNavigateToWidgetSetup()
//                "widget_preview" -> onNavigateToWidgetPreview()
//            }
//        },
//        onStartFocus = viewModel::startFocus,
//        onPause = viewModel::pauseSession,
//        onFinish = viewModel::finishSession,
//        onRecentActivityClick = viewModel::onRecentActivitySelected
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
//    onRecentActivityClick: (Int) -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AuraColors.BackgroundDark)
//            .statusBarsPadding()
//            .windowInsetsPadding(WindowInsets.navigationBars)
//            .consumeWindowInsets(WindowInsets.safeDrawing)
//    ) {
//        HomeHeader(
//            appName = uiState.appName,
//            subtitle = uiState.subtitle,
//            onSettingsClick = onNavigateToSettings
//        )
//    }
//
//    LazyColumn(
//        modifier = Modifier
//        .fillMaxSize(),
//        contentPadding = PaddingValues(
//            start = 20.dp,
//            end = 20.dp,
//            top = 4.dp,
//            bottom = 20.dp
//        ),
//        verticalArrangement = Arrangement.spacedBy(20.dp)
//    ) {
//
//        item {
//            TimerHeroCard(
//                runningActivity = uiState.runningActivity,
//                selectedActivity = uiState.selectedActivity
//            )
//        }
//
//        item {
//            SessionControls(
//                isRunning = uiState.runningActivity != null,
//                onStartFocus = onStartFocus,
//                onPause = onPause,
//                onFinish = onFinish,
//             //   onSwitch = onSwitch
//            )
//        }
//
//        item {
//            QuickActionsSection(
//                actions = uiState.quickActions,
//                onActionClick = onQuickActionClick
//            )
//        }
//
//        item {
//            SectionHeader(
//                title = "Recent Activities",
//                actionText = "View all",
//                onActionClick = onNavigateToAllActivities
//            )
//        }
//
//        if (uiState.recentActivities.isEmpty()) {
//            item {
//                EmptyRecentActivities()
//            }
//        } else {
//            items(uiState.recentActivities, key = { it.id }) { activity ->
//                RecentActivityCard(
//                    activity = activity,
//                    onClick = { onRecentActivityClick(activity.id) }
//                )
//            }
//        }
//
//        item {
//            Spacer(modifier = Modifier.height(6.dp))
//        }
//    }
//}
//
//@Composable
//private fun HomeHeader(
//    appName: String,
//    subtitle: String,
//    onSettingsClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.Top
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .shadow(
//                        elevation = 12.dp,
//                        shape = CircleShape,
//                        ambientColor = AuraColors.YellowGlow,
//                        spotColor = AuraColors.YellowGlow
//                    )
//                    .clip(CircleShape)
//                    .background(
//                        Brush.radialGradient(
//                            colors = listOf(
//                                AuraColors.YellowPrimary.copy(alpha = 0.18f),
//                                AuraColors.SurfaceCard
//                            )
//                        )
//                    )
//                    .border(
//                        width = 1.dp,
//                        color = AuraColors.CardBorderSelected.copy(alpha = 0.7f),
//                        shape = CircleShape
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painterResource(id = R.drawable.app_logo),
//                    contentDescription = null,
//                    modifier = Modifier.size(32.dp)
//                )
//            }
//                Text(
//                    text = appName,
//                    style = AuraTypography.DisplayMedium,
//                    color = AuraColors.TextPrimary
//                )
//        }
//
//        Box(
//            modifier = Modifier
//                .size(44.dp)
//                .shadow(
//                    elevation = 10.dp,
//                    shape = CircleShape,
//                    ambientColor = AuraColors.YellowGlow,
//                    spotColor = AuraColors.YellowGlow
//                )
//                .clip(CircleShape)
//                .background(AuraColors.SurfaceCard)
//                .border(
//                    width = 1.dp,
//                    color = AuraColors.CardBorderDefault,
//                    shape = CircleShape
//                )
//                .clickable(onClick = onSettingsClick),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Rounded.Settings,
//                contentDescription = "Settings",
//                tint = AuraColors.TextPrimary
//            )
//        }
//    }
//}
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
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF101010),
//                        AuraColors.BackgroundDark
//                    )
//                )
//            )
//            .border(
//                width = 1.dp,
//                color = AuraColors.CardBorderDefault,
//                shape = RoundedCornerShape(32.dp)
//            )
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
//                activityName = heroActivity?.name?.uppercase() ?: "CREATIVE PROJECTS",
//                sizeDp = 250.dp
//            )
//
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
//@Composable
//private fun SessionControls(
//    isRunning: Boolean,
//    onStartFocus: () -> Unit,
//    onPause: () -> Unit,
//    onFinish: () -> Unit,
// //   onSwitch: () -> Unit
//) {
//    if (!isRunning) {
//        PremiumPrimaryButton(
//            text = "Start Focus",
//            icon = Icons.Rounded.PlayArrow,
//            onClick = onStartFocus
//        )
//    } else {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            PremiumPillButton(
//                text = "Pause",
//                icon = Icons.Rounded.Pause,
//                isPrimary = true,
//                modifier = Modifier.weight(1f),
//                onClick = onPause
//            )
//            PremiumPillButton(
//                text = "Finish",
//                icon = Icons.Rounded.Stop,
//                modifier = Modifier.weight(1f),
//                onClick = onFinish
//            )
//
//        }
//    }
//}
//
//@Composable
//private fun QuickActionsSection(
//    actions: List<HomeQuickAction>,
//    onActionClick: (HomeQuickAction) -> Unit
//) {
//    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//        Text(
//            text = "Quick Actions",
//            style = AuraTypography.LabelMedium,
//            color = AuraColors.TextMuted
//        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .horizontalScroll(rememberScrollState()),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            actions.forEach { action ->
//                QuickActionCard(
//                    action = action,
//                    onClick = { onActionClick(action) }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun QuickActionCard(
//    action: HomeQuickAction,
//    onClick: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .width(108.dp)
//            .clip(RoundedCornerShape(22.dp))
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(
//                        AuraColors.SurfaceCardLight,
//                        AuraColors.SurfaceCard
//                    )
//                )
//            )
//            .border(
//                width = 1.dp,
//                color = AuraColors.CardBorderDefault,
//                shape = RoundedCornerShape(22.dp)
//            )
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
//                .border(
//                    width = 1.dp,
//                    color = AuraColors.YellowPrimary.copy(alpha = 0.22f),
//                    shape = CircleShape
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = action.icon,
//                contentDescription = action.title,
//                tint = AuraColors.YellowPrimary
//            )
//        }
//
//        Text(
//            text = action.title,
//            style = AuraTypography.BodyMedium,
//            color = AuraColors.TextPrimary
//        )
//    }
//}
//
//@Composable
//private fun SectionHeader(
//    title: String,
//    actionText: String,
//    onActionClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = title,
//            style = AuraTypography.LabelMedium,
//            color = AuraColors.TextMuted
//        )
//        Text(
//            text = actionText,
//            style = AuraTypography.TitleMedium,
//            color = AuraColors.YellowPrimary,
//            modifier = Modifier.clickable(onClick = onActionClick)
//        )
//    }
//}
//
////@Composable
////private fun RecentActivityCard(
////    activity: ActivityUiModel
////) {
////    Box(
////        modifier = Modifier
////            .fillMaxWidth()
////            .clip(RoundedCornerShape(24.dp))
////            .background(
////                Brush.verticalGradient(
////                    colors = listOf(
////                        Color(0xFF141414),
////                        AuraColors.SurfaceCard
////                    )
////                )
////            )
////            .border(
////                width = 1.dp,
////                color = AuraColors.CardBorderDefault,
////                shape = RoundedCornerShape(24.dp)
////            )
////            .padding(horizontal = 16.dp, vertical = 16.dp)
////    ) {
////        Row(
////            verticalAlignment = Alignment.CenterVertically,
////            horizontalArrangement = Arrangement.spacedBy(14.dp)
////        ) {
////            Box(
////                modifier = Modifier
////                    .size(52.dp)
////                    .clip(CircleShape)
////                    .background(AuraColors.SurfaceCardLight),
////                contentAlignment = Alignment.Center
////            ) {
////                Text(
////                    text = activity.icon,
////                    style = AuraTypography.TitleMedium
////                )
////            }
////
////            Column(
////                modifier = Modifier.weight(1f),
////                verticalArrangement = Arrangement.spacedBy(6.dp)
////            ) {
////                Text(
////                    text = activity.name,
////                    style = AuraTypography.TitleMedium,
////                    color = AuraColors.TextPrimary,
////                    maxLines = 1,
////                    overflow = TextOverflow.Ellipsis
////                )
////
////                Text(
////                    text = "${activity.targetSeconds / 60} min • ${activity.lastActiveDate}",
////                    style = AuraTypography.BodyMedium,
////                    color = AuraColors.TextSecondary
////                )
////
////                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
////                    Text(
////                        text = "${(activity.progress * 100).toInt()}%",
////                        style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
////                        color = AuraColors.YellowPrimary
////                    )
////
////                    Box(
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .height(6.dp)
////                            .clip(RoundedCornerShape(50.dp))
////                            .background(AuraColors.TimerTrack)
////                    ) {
////                        Box(
////                            modifier = Modifier
////                                .fillMaxWidth(activity.progress.coerceIn(0f, 1f))
////                                .height(6.dp)
////                                .clip(RoundedCornerShape(50.dp))
////                                .background(AuraColors.YellowPrimary)
////                        )
////                    }
////                }
////            }
////
////            Box(
////                modifier = Modifier
////                    .size(42.dp)
////                    .clip(CircleShape)
////                    .background(AuraColors.SurfaceCardLight)
////                    .border(
////                        width = 1.dp,
////                        color = AuraColors.CardBorderDefault,
////                        shape = CircleShape
////                    ),
////                contentAlignment = Alignment.Center
////            ) {
////                Icon(
////                    imageVector = Icons.Rounded.PlayArrow,
////                    contentDescription = "Quick start ${activity.name}",
////                    tint = AuraColors.YellowPrimary
////                )
////            }
////        }
////    }
////}
//
//
//@Composable
//private fun RecentActivityCard(
//    activity: ActivityUiModel,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(24.dp))
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF141414),
//                        AuraColors.SurfaceCard
//                    )
//                )
//            )
//            .border(
//                width = 1.dp,
//                color = AuraColors.CardBorderDefault,
//                shape = RoundedCornerShape(24.dp)
//            )
//            .clickable(onClick = onClick)
//            .padding(horizontal = 16.dp, vertical = 16.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(14.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(52.dp)
//                    .clip(CircleShape)
//                    .background(AuraColors.SurfaceCardLight),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = activity.icon,
//                    style = AuraTypography.TitleMedium
//                )
//            }
//
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
//
//                Text(
//                    text = "${activity.targetSeconds / 60} min • ${activity.lastActiveDate}",
//                    style = AuraTypography.BodyMedium,
//                    color = AuraColors.TextSecondary
//                )
//
//                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
//                    Text(
//                        text = "${(activity.progress * 100).toInt()}%",
//                        style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
//                        color = AuraColors.YellowPrimary
//                    )
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(6.dp)
//                            .clip(RoundedCornerShape(50.dp))
//                            .background(AuraColors.TimerTrack)
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth(activity.progress.coerceIn(0f, 1f))
//                                .height(6.dp)
//                                .clip(RoundedCornerShape(50.dp))
//                                .background(AuraColors.YellowPrimary)
//                        )
//                    }
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .size(42.dp)
//                    .clip(CircleShape)
//                    .background(AuraColors.SurfaceCardLight)
//                    .border(
//                        width = 1.dp,
//                        color = AuraColors.CardBorderDefault,
//                        shape = CircleShape
//                    )
//                    .clickable(onClick = onClick),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Rounded.PlayArrow,
//                    contentDescription = "Select ${activity.name}",
//                    tint = AuraColors.YellowPrimary
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun EmptyRecentActivities() {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(22.dp))
//            .background(AuraColors.SurfaceCard)
//            .border(
//                width = 1.dp,
//                color = AuraColors.CardBorderDefault,
//                shape = RoundedCornerShape(22.dp)
//            )
//            .padding(20.dp)
//    ) {
//        Text(
//            text = "No recent activities yet.",
//            style = AuraTypography.BodyMedium,
//            color = AuraColors.TextSecondary
//        )
//    }
//}
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
//            .background(
//                Brush.horizontalGradient(
//                    colors = listOf(
//                        AuraColors.SurfaceCard,
//                        Color(0xFF1C1707)
//                    )
//                )
//            )
//            .border(
//                width = 1.dp,
//                color = AuraColors.YellowPrimary.copy(alpha = 0.35f),
//                shape = RoundedCornerShape(28.dp)
//            )
//            .clickable(onClick = onClick)
//            .padding(horizontal = 22.dp, vertical = 18.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = text,
//            tint = AuraColors.YellowPrimary
//        )
//        Spacer(modifier = Modifier.width(10.dp))
//        Text(
//            text = text,
//            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
//            color = AuraColors.YellowPrimary
//        )
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
//                if (isPrimary) {
//                    Brush.horizontalGradient(
//                        colors = listOf(
//                            AuraColors.YellowPrimary.copy(alpha = 0.18f),
//                            AuraColors.SurfaceCard
//                        )
//                    )
//                } else {
//                    Brush.horizontalGradient(
//                        colors = listOf(
//                            AuraColors.SurfaceCardLight,
//                            AuraColors.SurfaceCard
//                        )
//                    )
//                }
//            )
//            .border(
//                width = 1.dp,
//                color = if (isPrimary) {
//                    AuraColors.YellowPrimary.copy(alpha = 0.35f)
//                } else {
//                    AuraColors.CardBorderDefault
//                },
//                shape = RoundedCornerShape(22.dp)
//            )
//            .clickable(onClick = onClick)
//            .padding(horizontal = 14.dp, vertical = 14.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = text,
//            tint = if (isPrimary) AuraColors.YellowPrimary else AuraColors.TextPrimary,
//            modifier = Modifier.size(18.dp)
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = text,
//            style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold),
//            color = if (isPrimary) AuraColors.YellowPrimary else AuraColors.TextPrimary
//        )
//    }
//}





package com.sangeeta.chronomind.ui.home

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.ui.components.ChronoTimerRing
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToAllActivities: () -> Unit,
    onNavigateToCreateActivity: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToInsights: () -> Unit,
    onNavigateToWidgetSetup: () -> Unit,
    onNavigateToWidgetPreview: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToAllActivities = onNavigateToAllActivities,
        onQuickActionClick = { action ->
            when (action.id) {
                "new_activity"   -> onNavigateToCreateActivity()
                "history"        -> onNavigateToHistory()
                "insights"       -> onNavigateToInsights()
                "widget_setup"   -> onNavigateToWidgetSetup()
                "widget_preview" -> onNavigateToWidgetPreview()
            }
        },
        onStartFocus = viewModel::startFocus,
        onPause = viewModel::pauseSession,
        onFinish = viewModel::finishSession,
        onRecentActivityClick = viewModel::onRecentActivitySelected,
        onStartActivityDirectly = viewModel::startActivityDirectly
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    onNavigateToSettings: () -> Unit,
    onNavigateToAllActivities: () -> Unit,
    onQuickActionClick: (HomeQuickAction) -> Unit,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onRecentActivityClick: (Int) -> Unit,
    onStartActivityDirectly: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        HomeHeader(
            appName = uiState.appName,
            onSettingsClick = onNavigateToSettings,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                TimerHeroCard(
                    runningActivity = uiState.runningActivity,
                    selectedActivity = uiState.selectedActivity
                )
            }

            item {
                SessionControls(
                    isRunning = uiState.runningActivity != null,
                    onStartFocus = onStartFocus,
                    onPause = onPause,
                    onFinish = onFinish
                )
            }

            item {
                QuickActionsSection(
                    actions = uiState.quickActions,
                    onActionClick = onQuickActionClick
                )
            }

            item {
                SectionHeader(
                    title = "Recent Activities",
                    actionText = "View all",
                    onActionClick = onNavigateToAllActivities
                )
            }

            if (uiState.recentActivities.isEmpty()) {
                item { EmptyRecentActivities() }
            } else {
                items(uiState.recentActivities, key = { it.id }) { activity ->
                    // Shared ActivityCard — selected = yellow border, running = pause icon
                    ActivityCard(
                        activity = activity,
                        isSelected = activity.id == uiState.selectedActivity?.id,
                        onCardClick = { onRecentActivityClick(activity.id) },
                       // onActionClick = { onRecentActivityClick(activity.id) }
                        onActionClick = {
                            if (activity.isRunning) {
                                onPause()                             // if already running → pause
                            } else {
                                onStartActivityDirectly(activity.id)  // not running → start immediately
                            }
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(6.dp)) }
        }
    }
}

// ─── Shared card used by BOTH HomeScreen (recent) and AllActivitiesScreen ────

@Composable
fun ActivityCard(
    activity: ActivityUiModel,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onActionClick: () -> Unit
) {
    val borderColor = when {
        activity.isRunning -> AuraColors.YellowPrimary
        isSelected         -> AuraColors.YellowPrimary.copy(alpha = 0.55f)
        else               -> AuraColors.CardBorderDefault
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF141414), AuraColors.SurfaceCard)
                )
            )
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(24.dp))
            .clickable(onClick = onCardClick)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon badge
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(AuraColors.SurfaceCardLight),
                contentAlignment = Alignment.Center
            ) {
                Text(text = activity.icon, style = AuraTypography.TitleMedium)
            }

            // Name + meta + progress bar
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = activity.name,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${activity.targetSeconds / 60} min • ${activity.lastActiveDate}",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "${(activity.progress * 100).toInt()}%",
                        style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = AuraColors.YellowPrimary
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(AuraColors.TimerTrack)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(activity.progress.coerceIn(0f, 1f))
                                .height(5.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(AuraColors.YellowPrimary)
                        )
                    }
                }
            }

            // Play / Pause toggle button
            val actionIcon = if (activity.isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
            val actionDesc = if (activity.isRunning) "Pause ${activity.name}" else "Select ${activity.name}"
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AuraColors.SurfaceCardLight)
                    .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                    .clickable(onClick = onActionClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionDesc,
                    tint = AuraColors.YellowPrimary
                )
            }
        }
    }
}


@Composable
private fun HomeHeader(
    appName: String,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shadow(12.dp, CircleShape, ambientColor = AuraColors.YellowGlow, spotColor = AuraColors.YellowGlow)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(AuraColors.YellowPrimary.copy(alpha = 0.18f), AuraColors.SurfaceCard)))
                    .border(1.dp, AuraColors.CardBorderSelected.copy(alpha = 0.7f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(R.drawable.app_logo), null, modifier = Modifier.size(42.dp))
            }
                Text(text = appName, style = AuraTypography.DisplayMedium, color = AuraColors.TextPrimary)
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(10.dp, CircleShape, ambientColor = AuraColors.YellowGlow, spotColor = AuraColors.YellowGlow)
                .clip(CircleShape)
                .background(AuraColors.SurfaceCard)
                .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                .clickable(onClick = onSettingsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = AuraColors.TextPrimary)
        }
    }
}

// ─── TimerHeroCard ────────────────────────────────────────────────────────────

@Composable
private fun TimerHeroCard(
    runningActivity: ActivityUiModel?,
    selectedActivity: ActivityUiModel?
) {
    val heroActivity = runningActivity ?: selectedActivity
    val isRunning = runningActivity != null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF101010), AuraColors.BackgroundDark)))
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(32.dp))
            .padding(horizontal = 20.dp, vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ChronoTimerRing(
                progress = heroActivity?.progress ?: 0f,
                timerText = heroActivity?.elapsedFormatted ?: "00:00",
                subLabel = if (isRunning) "IN SESSION" else "START WHEN READY",
                activityName = heroActivity?.name?.uppercase() ?: "NO ACTIVITY SELECTED",
                sizeDp = 250.dp
            )
            if (!isRunning && heroActivity != null) {
                Text(
                    text = heroActivity.name,
                    style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Medium),
                    color = AuraColors.YellowPrimary
                )
            }
        }
    }
}

// ─── SessionControls ─────────────────────────────────────────────────────────

@Composable
private fun SessionControls(
    isRunning: Boolean,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit
) {
    if (!isRunning) {
        PremiumPrimaryButton(text = "Start Focus", icon = Icons.Rounded.PlayArrow, onClick = onStartFocus)
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PremiumPillButton(
                text = "Pause", icon = Icons.Rounded.Pause,
                isPrimary = true, modifier = Modifier.weight(1f), onClick = onPause
            )
            PremiumPillButton(
                text = "Finish", icon = Icons.Rounded.Stop,
                modifier = Modifier.weight(1f), onClick = onFinish
            )
        }
    }
}

// ─── QuickActionsSection ──────────────────────────────────────────────────────

@Composable
private fun QuickActionsSection(
    actions: List<HomeQuickAction>,
    onActionClick: (HomeQuickAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Quick Actions", style = AuraTypography.LabelMedium, color = AuraColors.TextMuted)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            actions.forEach { action ->
                QuickActionCard(action = action, onClick = { onActionClick(action) })
            }
        }
    }
}

@Composable
private fun QuickActionCard(action: HomeQuickAction, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(108.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.verticalGradient(listOf(AuraColors.SurfaceCardLight, AuraColors.SurfaceCard)))
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
                .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.22f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(action.icon, contentDescription = action.title, tint = AuraColors.YellowPrimary)
        }
        Text(action.title, style = AuraTypography.BodyMedium, color = AuraColors.TextPrimary)
    }
}

// ─── SectionHeader ────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String, actionText: String, onActionClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = AuraTypography.LabelMedium, color = AuraColors.TextMuted)
        Text(
            actionText,
            style = AuraTypography.TitleMedium,
            color = AuraColors.YellowPrimary,
            modifier = Modifier.clickable(onClick = onActionClick)
        )
    }
}

// ─── EmptyRecentActivities ────────────────────────────────────────────────────

@Composable
private fun EmptyRecentActivities() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(AuraColors.SurfaceCard)
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(20.dp)
    ) {
        Text(
            "No recent activities. Activities used today or yesterday will appear here.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary
        )
    }
}

// ─── Buttons ──────────────────────────────────────────────────────────────────

@Composable
private fun PremiumPrimaryButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.horizontalGradient(listOf(AuraColors.SurfaceCard, Color(0xFF1C1707))))
            .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.35f), RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = AuraColors.YellowPrimary)
        Spacer(Modifier.width(10.dp))
        Text(text, style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold), color = AuraColors.YellowPrimary)
    }
}

@Composable
private fun PremiumPillButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (isPrimary) Brush.horizontalGradient(listOf(AuraColors.YellowPrimary.copy(alpha = 0.18f), AuraColors.SurfaceCard))
                else Brush.horizontalGradient(listOf(AuraColors.SurfaceCardLight, AuraColors.SurfaceCard))
            )
            .border(1.dp, if (isPrimary) AuraColors.YellowPrimary.copy(alpha = 0.35f) else AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = if (isPrimary) AuraColors.YellowPrimary else AuraColors.TextPrimary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold), color = if (isPrimary) AuraColors.YellowPrimary else AuraColors.TextPrimary)
    }
}




