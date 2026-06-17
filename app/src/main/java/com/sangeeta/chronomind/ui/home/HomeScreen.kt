////package com.sangeeta.chronomind.ui.home
////
////
////import androidx.compose.foundation.background
////import androidx.compose.foundation.border
////import androidx.compose.foundation.layout.Arrangement
////import androidx.compose.foundation.layout.Box
////import androidx.compose.foundation.layout.Column
////import androidx.compose.foundation.layout.Row
////import androidx.compose.foundation.layout.Spacer
////import androidx.compose.foundation.layout.aspectRatio
////import androidx.compose.foundation.layout.fillMaxSize
////import androidx.compose.foundation.layout.fillMaxWidth
////import androidx.compose.foundation.layout.height
////import androidx.compose.foundation.layout.navigationBarsPadding
////import androidx.compose.foundation.layout.padding
////import androidx.compose.foundation.layout.size
////import androidx.compose.foundation.layout.width
////import androidx.compose.foundation.lazy.grid.GridCells
////import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
////import androidx.compose.foundation.lazy.grid.items
////import androidx.compose.foundation.shape.CircleShape
////import androidx.compose.foundation.shape.RoundedCornerShape
////import androidx.compose.material.icons.Icons
////import androidx.compose.material.icons.outlined.Add
////import androidx.compose.material.icons.outlined.Favorite
////import androidx.compose.material.icons.outlined.Home
////import androidx.compose.material.icons.outlined.Notifications
////import androidx.compose.material.icons.outlined.PlayArrow
////import androidx.compose.material.icons.outlined.Settings
////import androidx.compose.material3.Card
////import androidx.compose.material3.CardDefaults
////import androidx.compose.material3.HorizontalDivider
////import androidx.compose.material3.Icon
////import androidx.compose.material3.IconButton
////import androidx.compose.material3.Scaffold
////import androidx.compose.material3.Text
////import androidx.compose.runtime.Composable
////import androidx.compose.runtime.getValue
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.draw.clip
////import androidx.compose.ui.graphics.Brush
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.text.font.FontWeight
////import androidx.compose.ui.unit.dp
////import androidx.hilt.navigation.compose.hiltViewModel
////import androidx.lifecycle.compose.collectAsStateWithLifecycle
////
////private val ScreenBg = Color(0xFF05070B)
////private val CardBg = Color(0xFF11151D)
////private val CardBgTop = Color(0xFF1A202B)
////private val Accent = Color(0xFFF6BF26)
////private val AccentSoft = Color(0x66F6BF26)
////private val TextPrimary = Color(0xFFF5F7FA)
////private val TextSecondary = Color(0xFF9CA6B5)
////private val RunningGreen = Color(0xFFB6FF6C)
////private val ReadyBlue = Color(0xFF8AB4FF)
////private val PausedGray = Color(0xFFB0B7C3)
////
////@Composable
////fun HomeScreen(
////    onCreateActivityClick: () -> Unit,
////    onActivityClick: (Int) -> Unit,
////    onCurrentFocusClick: (Int) -> Unit,
////    onHistoryClick: () -> Unit,
////    onInsightsClick: () -> Unit,
////    onSettingsClick: () -> Unit,
////    viewModel: HomeViewModel = hiltViewModel()
////) {
////    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
////
////    HomeScreenContent(
////        uiState = uiState,
////        onCreateActivityClick = onCreateActivityClick,
////        onActivityClick = onActivityClick,
////        onCurrentFocusClick = onCurrentFocusClick,
////        onHistoryClick = onHistoryClick,
////        onInsightsClick = onInsightsClick,
////        onSettingsClick = onSettingsClick
////    )
////}
////
////@Composable
////fun HomeScreenContent(
////    uiState: HomeUiState,
////    onCreateActivityClick: () -> Unit,
////    onActivityClick: (Int) -> Unit,
////    onCurrentFocusClick: (Int) -> Unit,
////    onHistoryClick: () -> Unit,
////    onInsightsClick: () -> Unit,
////    onSettingsClick: () -> Unit
////) {
////    Scaffold(
////        containerColor = ScreenBg,
////        bottomBar = {
////            HomeBottomBar(
////                onHistoryClick = onHistoryClick,
////                onCreateActivityClick = onCreateActivityClick,
////                onInsightsClick = onInsightsClick,
////                onSettingsClick = onSettingsClick
////            )
////        }
////    ) { innerPadding ->
////        Column(
////            modifier = Modifier
////                .fillMaxSize()
////                .background(ScreenBg)
////                .padding(innerPadding)
////                .padding(horizontal = 16.dp, vertical = 12.dp)
////        ) {
////            HomeHeader(
////                greeting = uiState.greeting,
////                userName = uiState.userName
////            )
////
////            Spacer(modifier = Modifier.height(16.dp))
////
////            uiState.currentFocus?.let { focus ->
////                CurrentFocusCard(
////                    focus = focus,
////                    onClick = { onCurrentFocusClick(focus.id) }
////                )
////                Spacer(modifier = Modifier.height(12.dp))
////            }
////
////            TodayProgressCard(progressText = uiState.todayProgressText)
////
////            Spacer(modifier = Modifier.height(14.dp))
////
////            Text(
////                text = "Your Activities",
////                color = TextPrimary,
////                fontWeight = FontWeight.SemiBold
////            )
////
////            Spacer(modifier = Modifier.height(10.dp))
////
////            LazyVerticalGrid(
////                columns = GridCells.Fixed(2),
////                horizontalArrangement = Arrangement.spacedBy(12.dp),
////                verticalArrangement = Arrangement.spacedBy(12.dp),
////                modifier = Modifier.fillMaxWidth()
////            ) {
////                items(uiState.activities, key = { it.id }) { item ->
////                    ActivityMiniCard(
////                        item = item,
////                        onClick = { onActivityClick(item.id) }
////                    )
////                }
////            }
////        }
////    }
////}
////
////@Composable
////private fun HomeHeader(
////    greeting: String,
////    userName: String
////) {
////    Row(
////        modifier = Modifier.fillMaxWidth(),
////        verticalAlignment = Alignment.Top,
////        horizontalArrangement = Arrangement.SpaceBetween
////    ) {
////        Column {
////            Text(
////                text = "$greeting,",
////                color = TextPrimary,
////                fontWeight = FontWeight.Medium
////            )
////            Text(
////                text = userName,
////                color = Accent,
////                fontWeight = FontWeight.Bold
////            )
////            Spacer(modifier = Modifier.height(2.dp))
////            Text(
////                text = "Take a breath. You've got this.",
////                color = TextSecondary
////            )
////        }
////
////        Row(verticalAlignment = Alignment.CenterVertically) {
////            IconButton(onClick = { }) {
////                Icon(
////                    imageVector = Icons.Outlined.Notifications,
////                    contentDescription = "Notifications",
////                    tint = TextPrimary
////                )
////            }
////            Box(
////                modifier = Modifier
////                    .size(36.dp)
////                    .clip(CircleShape)
////                    .background(AccentSoft),
////                contentAlignment = Alignment.Center
////            ) {
////                Text(
////                    text = userName.take(1).uppercase(),
////                    color = Accent,
////                    fontWeight = FontWeight.Bold
////                )
////            }
////        }
////    }
////}
////
////@Composable
////private fun CurrentFocusCard(
////    focus: HomeFocusCardUi,
////    onClick: () -> Unit
////) {
////    Card(
////        onClick = onClick,
////        shape = RoundedCornerShape(22.dp),
////        colors = CardDefaults.cardColors(containerColor = CardBg),
////        border = androidx.compose.foundation.BorderStroke(1.dp, AccentSoft),
////        modifier = Modifier.fillMaxWidth()
////    ) {
////        Box(
////            modifier = Modifier
////                .background(
////                    brush = Brush.verticalGradient(
////                        listOf(CardBgTop, CardBg)
////                    )
////                )
////                .padding(16.dp)
////        ) {
////            Column {
////                Text(
////                    text = "CURRENT FOCUS",
////                    color = TextSecondary,
////                    fontWeight = FontWeight.Medium
////                )
////
////                Spacer(modifier = Modifier.height(12.dp))
////
////                Row(
////                    verticalAlignment = Alignment.CenterVertically
////                ) {
////                    ProgressRing(
////                        progress = focus.progress,
////                        centerText = ""
////                    )
////
////                    Spacer(modifier = Modifier.width(14.dp))
////
////                    Column(modifier = Modifier.weight(1f)) {
////                        Text(
////                            text = focus.title,
////                            color = TextPrimary,
////                            fontWeight = FontWeight.SemiBold
////                        )
////                        Text(
////                            text = focus.formattedElapsed,
////                            color = TextPrimary,
////                            fontWeight = FontWeight.Bold
////                        )
////                        Text(
////                            text = focus.status,
////                            color = if (focus.status == "Running") RunningGreen else ReadyBlue,
////                            fontWeight = FontWeight.Medium
////                        )
////                    }
////
////                    Box(
////                        modifier = Modifier
////                            .size(42.dp)
////                            .clip(CircleShape)
////                            .background(Color(0xFF0E1218))
////                            .border(1.dp, AccentSoft, CircleShape),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        Icon(
////                            imageVector = Icons.Outlined.PlayArrow,
////                            contentDescription = "Open timer",
////                            tint = Accent
////                        )
////                    }
////                }
////            }
////        }
////    }
////}
////
////@Composable
////private fun TodayProgressCard(progressText: String) {
////    Card(
////        shape = RoundedCornerShape(18.dp),
////        colors = CardDefaults.cardColors(containerColor = CardBg),
////        modifier = Modifier.fillMaxWidth()
////    ) {
////        Row(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 14.dp, vertical = 12.dp),
////            verticalAlignment = Alignment.CenterVertically,
////            horizontalArrangement = Arrangement.SpaceBetween
////        ) {
////            Text(
////                text = "Today's progress",
////                color = TextSecondary
////            )
////            Text(
////                text = progressText,
////                color = Accent,
////                fontWeight = FontWeight.SemiBold
////            )
////        }
////    }
////}
////
////@Composable
////private fun ActivityMiniCard(
////    item: HomeActivityItemUi,
////    onClick: () -> Unit
////) {
////    Card(
////        onClick = onClick,
////        shape = RoundedCornerShape(18.dp),
////        colors = CardDefaults.cardColors(containerColor = CardBg),
////        modifier = Modifier
////            .fillMaxWidth()
////            .aspectRatio(1.18f)
////    ) {
////        Column(
////            modifier = Modifier
////                .fillMaxSize()
////                .background(
////                    Brush.verticalGradient(listOf(CardBgTop, CardBg))
////                )
////                .padding(14.dp)
////        ) {
////            Row(
////                modifier = Modifier.fillMaxWidth(),
////                horizontalArrangement = Arrangement.SpaceBetween
////            ) {
////                Box(
////                    modifier = Modifier
////                        .size(32.dp)
////                        .clip(RoundedCornerShape(10.dp))
////                        .background(AccentSoft),
////                    contentAlignment = Alignment.Center
////                ) {
////                    Text(
////                        text = item.title.take(1),
////                        color = Accent,
////                        fontWeight = FontWeight.Bold
////                    )
////                }
////
////                StatusDot(item.status)
////            }
////
////            Spacer(modifier = Modifier.height(10.dp))
////
////            Text(
////                text = item.title,
////                color = TextPrimary,
////                fontWeight = FontWeight.SemiBold
////            )
////
////            Spacer(modifier = Modifier.height(6.dp))
////
////            Text(
////                text = item.formattedElapsed,
////                color = TextPrimary,
////                fontWeight = FontWeight.Bold
////            )
////
////            Spacer(modifier = Modifier.height(2.dp))
////
////            Text(
////                text = item.status,
////                color = when (item.status) {
////                    "Running" -> RunningGreen
////                    "Paused" -> PausedGray
////                    else -> ReadyBlue
////                },
////                fontWeight = FontWeight.Medium
////            )
////
////            Spacer(modifier = Modifier.weight(1f))
////
////            LinearProgressTiny(progress = item.progress)
////        }
////    }
////}
////
////@Composable
////private fun ProgressRing(
////    progress: Float,
////    centerText: String
////) {
////    Box(
////        modifier = Modifier
////            .size(64.dp)
////            .clip(CircleShape)
////            .background(Color(0xFF0D1015))
////            .border(3.dp, AccentSoft, CircleShape),
////        contentAlignment = Alignment.Center
////    ) {
////        Text(
////            text = centerText,
////            color = TextPrimary
////        )
////    }
////}
////
////@Composable
////private fun LinearProgressTiny(progress: Float) {
////    Box(
////        modifier = Modifier
////            .fillMaxWidth()
////            .height(6.dp)
////            .clip(RoundedCornerShape(50))
////            .background(Color(0xFF0B0E13))
////    ) {
////        Box(
////            modifier = Modifier
////                .fillMaxWidth(progress.coerceIn(0f, 1f))
////                .height(6.dp)
////                .clip(RoundedCornerShape(50))
////                .background(Accent)
////        )
////    }
////}
////
////@Composable
////private fun StatusDot(status: String) {
////    val color = when (status) {
////        "Running" -> RunningGreen
////        "Paused" -> PausedGray
////        else -> ReadyBlue
////    }
////
////    Box(
////        modifier = Modifier
////            .size(10.dp)
////            .clip(CircleShape)
////            .background(color)
////    )
////}
////
////@Composable
////private fun HomeBottomBar(
////    onHistoryClick: () -> Unit,
////    onCreateActivityClick: () -> Unit,
////    onInsightsClick: () -> Unit,
////    onSettingsClick: () -> Unit
////) {
////    Column(
////        modifier = Modifier
////            .fillMaxWidth()
////            .background(ScreenBg)
////            .navigationBarsPadding()
////    ) {
////        HorizontalDivider(color = Color(0xFF141A22))
////
////        Row(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(horizontal = 14.dp, vertical = 10.dp),
////            horizontalArrangement = Arrangement.SpaceBetween,
////            verticalAlignment = Alignment.CenterVertically
////        ) {
////            BottomBarItem(
////                icon = Icons.Outlined.Home,
////                label = "Home",
////                selected = true,
////                onClick = {}
////            )
////            BottomBarItem(
////                icon = Icons.Outlined.Settings,
////                label = "History",
////                selected = false,
////                onClick = onHistoryClick
////            )
////            AddButton(onClick = onCreateActivityClick)
////            BottomBarItem(
////                icon = Icons.Outlined.Favorite,
////                label = "Insights",
////                selected = false,
////                onClick = onInsightsClick
////            )
////            BottomBarItem(
////                icon = Icons.Outlined.Settings,
////                label = "Settings",
////                selected = false,
////                onClick = onSettingsClick
////            )
////        }
////    }
////}
////
////@Composable
////private fun BottomBarItem(
////    icon: androidx.compose.ui.graphics.vector.ImageVector,
////    label: String,
////    selected: Boolean,
////    onClick: () -> Unit
////) {
////    Column(
////        horizontalAlignment = Alignment.CenterHorizontally
////    ) {
////        IconButton(onClick = onClick) {
////            Icon(
////                imageVector = icon,
////                contentDescription = label,
////                tint = if (selected) Accent else TextSecondary
////            )
////        }
////        Text(
////            text = label,
////            color = if (selected) Accent else TextSecondary
////        )
////    }
////}
////
////@Composable
////private fun AddButton(
////    onClick: () -> Unit
////) {
////    Box(
////        modifier = Modifier
////            .size(56.dp)
////            .clip(CircleShape)
////            .background(Accent),
////        contentAlignment = Alignment.Center
////    ) {
////        IconButton(onClick = onClick) {
////            Icon(
////                imageVector = Icons.Outlined.Add,
////                contentDescription = "Add activity",
////                tint = ScreenBg
////            )
////        }
////    }
////}
//
//
//
//
//
//
//
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.*
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.sangeeta.chronomind.ui.theme.AuraColors
//import com.sangeeta.chronomind.ui.theme.AuraTypography
//import java.time.LocalTime
//import com.sangeeta.chronomind.ui.components.ChronoTimerRing
//
//// ─── Mock data (replace with ViewModel later) ────────────────────────────────
//private data class ActivityItem(
//    val name: String,
//    val icon: String,
//    val timeToday: String,
//    val status: String   // "Running" | "Paused" | "Ready"
//)
//
//private val sampleActivities = listOf(
//    ActivityItem("Deep Work",        "🧠", "4h 15m", "Running"),
//    ActivityItem("Project UI Design","🎨", "2h 30m", "Paused"),
//    ActivityItem("Client Meeting",   "👥", "0m",     "Ready"),
//    ActivityItem("Exercise",         "🏋️", "35m",    "Ready")
//)
//
//// ─── Screen ──────────────────────────────────────────────────────────────────
//@Composable
//fun HomeScreen(
//    onNewActivity: () -> Unit,
//    onHistory:     () -> Unit,
//    onInsights:    () -> Unit
//) {
//    val greeting = when (LocalTime.now().hour) {
//        in 5..11  -> "Good morning"
//        in 12..17 -> "Good afternoon"
//        else      -> "Good evening"
//    }
//
//    // Ambient glow pulse
//    val infiniteAnim = rememberInfiniteTransition(label = "homeGlow")
//    val glowAlpha by infiniteAnim.animateFloat(
//        initialValue  = 0.25f,
//        targetValue   = 0.45f,
//        animationSpec = infiniteRepeatable(
//            animation  = tween(2800, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "glowAlpha"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AuraColors.BackgroundDark)
//    ) {
//        // Ambient yellow glow behind timer
//        Box(
//            modifier = Modifier
//                .size(280.dp)
//                .align(Alignment.TopCenter)
//                .offset(y = 80.dp)
//                .background(
//                    Brush.radialGradient(
//                        colors = listOf(
//                            AuraColors.YellowPrimary.copy(alpha = glowAlpha * 0.3f),
//                            Color.Transparent
//                        )
//                    ),
//                    shape = CircleShape
//                )
//        )
//
//        LazyColumn(
//            modifier            = Modifier
//                .fillMaxSize()
//                .statusBarsPadding()
//                .navigationBarsPadding(),
//            contentPadding      = PaddingValues(bottom = 32.dp),
//            verticalArrangement = Arrangement.spacedBy(0.dp)
//        ) {
//            // ── Header ──────────────────────────────────────────────────────
//            item {
//                HomeHeader(greeting = greeting)
//            }
//
//            // ── 3D Timer Hero (ChronoMind style from Image 3) ──────────────
//            item {
//                TimerHeroSection(glowAlpha = glowAlpha)
//            }
//
//            // ── Quick Actions ────────────────────────────────────────────────
//            item {
//                QuickActionsRow(
//                    onNewActivity = onNewActivity,
//                    onHistory     = onHistory,
//                    onInsights    = onInsights
//                )
//            }
//
//            // ── Activity list label ──────────────────────────────────────────
//            item {
//                Text(
//                    text     = "YOUR ACTIVITIES",
//                    style    = AuraTypography.LabelMedium.copy(
//                        letterSpacing = 2.sp,
//                        fontWeight    = FontWeight.Bold
//                    ),
//                    color    = AuraColors.TextMuted,
//                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
//                )
//            }
//
//            // ── Activity list cards (3D-depth style) ─────────────────────────
//            items(sampleActivities.withIndex().toList()) { (index, activity) ->
//                ActivityCard3D(
//                    number   = index + 1,
//                    activity = activity,
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
//                )
//            }
//        }
//    }
//}
//
//// ─── Header ──────────────────────────────────────────────────────────────────
//@Composable
//private fun HomeHeader(greeting: String) {
//    Row(
//        modifier              = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 20.dp, vertical = 16.dp),
//        verticalAlignment     = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column {
//            Text(
//                text  = greeting,
//                style = AuraTypography.BodyMedium,
//                color = AuraColors.TextMuted
//            )
//            Text(
//                text  = "ChronoMind",
//                style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.Bold),
//                color = AuraColors.TextPrimary
//            )
//        }
//
//        // Date badge
//        Box(
//            modifier = Modifier
//                .clip(RoundedCornerShape(12.dp))
//                .background(AuraColors.SurfaceCardLight)
//                .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(12.dp))
//                .padding(horizontal = 12.dp, vertical = 8.dp)
//        ) {
//            Text(
//                text  = java.time.LocalDate.now()
//                    .format(java.time.format.DateTimeFormatter.ofPattern("EEE, dd MMM")),
//                style = AuraTypography.BodySmall,
//                color = AuraColors.TextSecondary
//            )
//        }
//    }
//}
//
//// ─── 3D Timer Hero (Image 3 style) ───────────────────────────────────────────
//@Composable
//private fun TimerHeroSection(glowAlpha: Float) {
//    Box(
//        modifier          = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 24.dp, vertical = 8.dp),
//        contentAlignment  = Alignment.Center
//    ) {
//        // Outer glow card
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .shadow(
//                    elevation    = 32.dp,
//                    shape        = RoundedCornerShape(28.dp),
//                    ambientColor = AuraColors.YellowGlow,
//                    spotColor    = AuraColors.YellowGlow
//                )
//                .clip(RoundedCornerShape(28.dp))
//                .background(
//                    Brush.verticalGradient(
//                        colors = listOf(
//                            Color(0xFF1A1A0A),
//                            Color(0xFF0D0D0D)
//                        )
//                    )
//                )
//                .border(
//                    width  = 1.dp,
//                    brush  = Brush.linearGradient(
//                        colors = listOf(
//                            AuraColors.YellowPrimary.copy(alpha = 0.6f),
//                            AuraColors.YellowPrimary.copy(alpha = 0.1f),
//                            Color.Transparent
//                        )
//                    ),
//                    shape  = RoundedCornerShape(28.dp)
//                )
//                .padding(24.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // 3D ring timer
//                ChronoTimerRing(
//                    progress     = 0.72f,
//                    timerText    = "1h 15m",
//                    subLabel     = "REMAINING",
//                    activityName = "STUDYING",
//                    sizeDp       = 200.dp
//                )
//
//                // Control buttons row
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(12.dp),
//                    verticalAlignment     = Alignment.CenterVertically
//                ) {
//                    TimerControlButton(
//                        text      = "⏸  Pause",
//                        isPrimary = true
//                    )
//                    TimerControlButton(text = "✓  Finish", isPrimary = false)
//                    TimerControlButton(text = "⇄  Switch", isPrimary = false)
//                }
//            }
//        }
//    }
//}
//
//// ─── Timer control button ─────────────────────────────────────────────────────
//@Composable
//private fun TimerControlButton(text: String, isPrimary: Boolean) {
//    Box(
//        modifier = Modifier
//            .height(40.dp)
//            .clip(RoundedCornerShape(50.dp))
//            .background(
//                if (isPrimary) AuraColors.YellowPrimary
//                else Color(0xFF1E1E1E)
//            )
//            .border(
//                1.dp,
//                if (isPrimary) Color.Transparent
//                else AuraColors.CardBorderDefault,
//                RoundedCornerShape(50.dp)
//            )
//            .padding(horizontal = 18.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text  = text,
//            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
//            color = if (isPrimary) AuraColors.TextOnYellow else AuraColors.TextPrimary
//        )
//    }
//}
//
//// ─── Quick Actions ────────────────────────────────────────────────────────────
//@Composable
//private fun QuickActionsRow(
//    onNewActivity: () -> Unit,
//    onHistory:     () -> Unit,
//    onInsights:    () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 20.dp)
//    ) {
//        Text(
//            text     = "Quick Actions",
//            style    = AuraTypography.TitleMedium,
//            color    = AuraColors.TextPrimary,
//            modifier = Modifier.padding(bottom = 12.dp)
//        )
//        Row(
//            modifier              = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            QuickActionChip(icon = "+",     label = "New Activity", onClick = onNewActivity, modifier = Modifier.weight(1f))
//            QuickActionChip(icon = "📅",    label = "History",      onClick = onHistory,     modifier = Modifier.weight(1f))
//            QuickActionChip(icon = "📈",    label = "Insights",     onClick = onInsights,    modifier = Modifier.weight(1f))
//            QuickActionChip(icon = "⚙️",    label = "Settings",     onClick = {},            modifier = Modifier.weight(1f))
//        }
//    }
//}
//
//@Composable
//private fun QuickActionChip(
//    icon:     String,
//    label:    String,
//    onClick:  () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier            = modifier
//            .clip(RoundedCornerShape(16.dp))
//            .background(AuraColors.SurfaceCard)
//            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
//            .clickable(onClick = onClick)
//            .padding(vertical = 12.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(6.dp)
//    ) {
//        Text(text = icon, fontSize = 22.sp)
//        Text(
//            text  = label,
//            style = AuraTypography.BodySmall.copy(fontSize = 10.sp),
//            color = AuraColors.TextMuted
//        )
//    }
//}
//
//// ─── 3D Activity Card ─────────────────────────────────────────────────────────
//@Composable
//private fun ActivityCard3D(
//    number:   Int,
//    activity: ActivityItem,
//    modifier: Modifier = Modifier
//) {
//    val isRunning = activity.status == "Running"
//    val shadowColor = if (isRunning) AuraColors.YellowGlow else Color.Transparent
//
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .shadow(
//                elevation    = if (isRunning) 20.dp else 4.dp,
//                shape        = RoundedCornerShape(18.dp),
//                ambientColor = shadowColor,
//                spotColor    = shadowColor
//            )
//            .clip(RoundedCornerShape(18.dp))
//            .background(
//                Brush.linearGradient(
//                    colors = if (isRunning)
//                        listOf(Color(0xFF1F1B07), Color(0xFF151200), Color(0xFF0D0D0D))
//                    else
//                        listOf(Color(0xFF1A1A1A), Color(0xFF111111))
//                )
//            )
//            .border(
//                width  = if (isRunning) 1.dp else 0.5.dp,
//                brush  = if (isRunning) Brush.linearGradient(
//                    colors = listOf(
//                        AuraColors.YellowPrimary.copy(alpha = 0.8f),
//                        AuraColors.YellowPrimary.copy(alpha = 0.2f)
//                    )
//                ) else Brush.linearGradient(
//                    colors = listOf(
//                        AuraColors.CardBorderDefault,
//                        AuraColors.CardBorderDefault
//                    )
//                ),
//                shape  = RoundedCornerShape(18.dp)
//            )
//            .padding(horizontal = 16.dp, vertical = 14.dp)
//    ) {
//        Row(
//            verticalAlignment     = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(14.dp)
//        ) {
//            // Number badge
//            Box(
//                modifier         = Modifier
//                    .size(36.dp)
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(
//                        if (isRunning) AuraColors.YellowPrimary.copy(alpha = 0.15f)
//                        else AuraColors.SurfaceCard
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text  = "$number.",
//                    style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
//                    color = if (isRunning) AuraColors.YellowPrimary else AuraColors.TextMuted
//                )
//            }
//
//            // Activity info
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text  = activity.name,
//                    style = AuraTypography.TitleMedium,
//                    color = AuraColors.TextPrimary
//                )
//                Text(
//                    text  = "${activity.timeToday} today",
//                    style = AuraTypography.BodySmall,
//                    color = AuraColors.TextMuted
//                )
//            }
//
//            // Icon + Status badge
//            Column(horizontalAlignment = Alignment.End) {
//                Text(text = activity.icon, fontSize = 20.sp)
//                Spacer(modifier = Modifier.height(4.dp))
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(50.dp))
//                        .background(
//                            when (activity.status) {
//                                "Running" -> AuraColors.YellowPrimary.copy(alpha = 0.15f)
//                                "Paused"  -> Color(0xFF2A2A2A)
//                                else      -> Color(0xFF1A1A1A)
//                            }
//                        )
//                        .padding(horizontal = 8.dp, vertical = 3.dp)
//                ) {
//                    Text(
//                        text  = "[${activity.status.first()}]",
//                        style = AuraTypography.BodySmall.copy(fontSize = 10.sp),
//                        color = when (activity.status) {
//                            "Running" -> AuraColors.YellowPrimary
//                            else      -> AuraColors.TextMuted
//                        }
//                    )
//                }
//            }
//        }
//    }
//}

package com.sangeeta.chronomind.ui.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.components.ChronoTimerRing
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography
import java.time.LocalTime


@Composable
fun HomeScreen(
    onNewActivity: () -> Unit,
    onHistory:     () -> Unit,
    onInsights:    () -> Unit,
    viewModel:     HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        userName        = state.userName,
        activities      = state.activities,
        runningActivity = state.runningActivity,
        onNewActivity   = onNewActivity,
        onHistory       = onHistory,
        onInsights      = onInsights,
        onStart         = viewModel::startActivity,
        onPause         = viewModel::pauseActivity,
        onFinish        = viewModel::finishActivity
    )
}

// ─── Pure UI (Stateless — easy to @Preview) ──────────────────────────────────
@Composable
fun HomeContent(
    userName:        String,
    activities:      List<ActivityUiModel>,
    runningActivity: ActivityUiModel?,
    onNewActivity:   () -> Unit,
    onHistory:       () -> Unit,
    onInsights:      () -> Unit,
    onStart:         (Int) -> Unit = {},
    onPause:         (Int) -> Unit = {},
    onFinish:        (Int) -> Unit = {}
) {
    val greeting = when (LocalTime.now().hour) {
        in 5..11  -> "Good morning"
        in 12..17 -> "Good afternoon"
        else      -> "Good evening"
    }

    val infiniteAnim = rememberInfiniteTransition(label = "homeGlow")
    val glowAlpha by infiniteAnim.animateFloat(
        initialValue  = 0.25f,
        targetValue   = 0.45f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
    ) {
        // Ambient glow
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopCenter)
                .offset(y = 80.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            AuraColors.YellowPrimary.copy(alpha = glowAlpha * 0.3f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentPadding      = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                // Header
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text  = greeting,
                            style = AuraTypography.BodyMedium,
                            color = AuraColors.TextMuted
                        )
                        Text(
                            text  = if (userName.isNotBlank()) userName else "ChronoMind",
                            style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.Bold),
                            color = AuraColors.TextPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AuraColors.SurfaceCardLight)
                            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text  = java.time.LocalDate.now()
                                .format(java.time.format.DateTimeFormatter.ofPattern("EEE, dd MMM")),
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextSecondary
                        )
                    }
                }
            }

            // Timer hero
            item {
                val running = runningActivity
                Box(
                    modifier         = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation    = 32.dp,
                                shape        = RoundedCornerShape(28.dp),
                                ambientColor = AuraColors.YellowGlow,
                                spotColor    = AuraColors.YellowGlow
                            )
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF1A1A0A), Color(0xFF0D0D0D))
                                )
                            )
                            .border(
                                1.dp,
                                Brush.linearGradient(
                                    listOf(
                                        AuraColors.YellowPrimary.copy(alpha = 0.6f),
                                        AuraColors.YellowPrimary.copy(alpha = 0.1f),
                                        Color.Transparent
                                    )
                                ),
                                RoundedCornerShape(28.dp)
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ChronoTimerRing(
                                progress     = running?.progress ?: 0f,
                                timerText    = running?.elapsedFormatted ?: "0m",
                                subLabel     = if (running != null) "ELAPSED" else "READY",
                                activityName = running?.name?.uppercase() ?: "NO ACTIVITY",
                                sizeDp       = 200.dp
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                if (running != null) {
                                    TimerControlButton("⏸  Pause", true) { onPause(running.id) }
                                    TimerControlButton("✓  Finish", false) { onFinish(running.id) }
                                } else {
                                    TimerControlButton("▶  Start", true) {
                                        activities.firstOrNull()?.let { onStart(it.id) }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Quick actions
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        "Quick Actions",
                        style    = AuraTypography.TitleMedium,
                        color    = AuraColors.TextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionChip("+",  "New Activity", onNewActivity, Modifier.weight(1f))
                        QuickActionChip("📅", "History",      onHistory,     Modifier.weight(1f))
                        QuickActionChip("📈", "Insights",     onInsights,    Modifier.weight(1f))
                        QuickActionChip("⚙️", "Settings",     {},            Modifier.weight(1f))
                    }
                }
            }

            // Activity list header
            item {
                Text(
                    "YOUR ACTIVITIES",
                    style    = AuraTypography.LabelMedium.copy(
                        letterSpacing = 2.sp,
                        fontWeight    = FontWeight.Bold
                    ),
                    color    = AuraColors.TextMuted,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }

            // Activity cards from DB
            if (activities.isEmpty()) {
                item {
                    EmptyActivitiesState(onNewActivity)
                }
            } else {
                items(activities, key = { it.id }) { activity ->
                    ActivityCard3D(
                        activity = activity,
                        onStart  = { onStart(activity.id) },
                        onPause  = { onPause(activity.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerControlButton(text: String, isPrimary: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(if (isPrimary) AuraColors.YellowPrimary else Color(0xFF1E1E1E))
            .border(
                1.dp,
                if (isPrimary) Color.Transparent else AuraColors.CardBorderDefault,
                RoundedCornerShape(50.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = text,
            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = if (isPrimary) AuraColors.TextOnYellow else AuraColors.TextPrimary
        )
    }
}

@Composable
private fun QuickActionChip(
    icon:     String,
    label:    String,
    onClick:  () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(AuraColors.SurfaceCard)
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(icon, fontSize = 22.sp)
        Text(
            label,
            style = AuraTypography.BodySmall.copy(fontSize = 10.sp),
            color = AuraColors.TextMuted
        )
    }
}

@Composable
private fun ActivityCard3D(
    activity: ActivityUiModel,
    onStart:  () -> Unit,
    onPause:  () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRunning   = activity.isRunning
    val shadowColor = if (isRunning) AuraColors.YellowGlow else Color.Transparent

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = if (isRunning) 20.dp else 4.dp,
                shape        = RoundedCornerShape(18.dp),
                ambientColor = shadowColor,
                spotColor    = shadowColor
            )
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    if (isRunning)
                        listOf(Color(0xFF1F1B07), Color(0xFF151200), Color(0xFF0D0D0D))
                    else
                        listOf(Color(0xFF1A1A1A), Color(0xFF111111))
                )
            )
            .border(
                width  = if (isRunning) 1.dp else 0.5.dp,
                brush  = Brush.linearGradient(
                    if (isRunning)
                        listOf(
                            AuraColors.YellowPrimary.copy(alpha = 0.8f),
                            AuraColors.YellowPrimary.copy(alpha = 0.2f)
                        )
                    else
                        listOf(AuraColors.CardBorderDefault, AuraColors.CardBorderDefault)
                ),
                shape  = RoundedCornerShape(18.dp)
            )
            .clickable { if (isRunning) onPause() else onStart() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isRunning) AuraColors.YellowPrimary.copy(alpha = 0.15f)
                        else AuraColors.SurfaceCard
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(activity.icon, fontSize = 20.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    activity.name,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )
                Text(
                    "${activity.elapsedFormatted} today  •  ${activity.streakDays}🔥",
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextMuted
                )
            }

            // Status chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        when (activity.statusLabel) {
                            "Running" -> AuraColors.YellowPrimary.copy(alpha = 0.15f)
                            else      -> Color(0xFF222222)
                        }
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    activity.statusLabel,
                    style = AuraTypography.BodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                    color = when (activity.statusLabel) {
                        "Running" -> AuraColors.YellowPrimary
                        else      -> AuraColors.TextMuted
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyActivitiesState(onAdd: () -> Unit) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("📭", fontSize = 48.sp)
        Text(
            "No activities yet",
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary
        )
        Text(
            "Tap + to create your first focus activity",
            style = AuraTypography.BodySmall,
            color = AuraColors.TextMuted
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(AuraColors.YellowPrimary)
                .clickable(onClick = onAdd)
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                "+ New Activity",
                style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.Bold),
                color = AuraColors.TextOnYellow
            )
        }
    }
}

// ─── @Preview ────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0D0D0D, widthDp = 390, heightDp = 844, name = "Home - with data")
@Composable
private fun HomePreviewWithData() {
    val sampleActivities = listOf(
        ActivityUiModel(1, "Deep Work",    "🧠", "#FFCC00", 15300L, 18000L, true,  12, "", true),
        ActivityUiModel(2, "Exercise",     "🏋️", "#FF6B35", 2100L,  2700L,  false, 7,  "", true),
        ActivityUiModel(3, "Reading",      "📚", "#4ECDC4", 1200L,  1800L,  false, 3,  "", true),
    )
    HomeContent(
        userName        = "Sangeeta",
        activities      = sampleActivities,
        runningActivity = sampleActivities.first { it.isRunning },
        onNewActivity   = {},
        onHistory       = {},
        onInsights      = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D0D, widthDp = 390, heightDp = 844, name = "Home - empty")
@Composable
private fun HomePreviewEmpty() {
    HomeContent(
        userName        = "Sangeeta",
        activities      = emptyList(),
        runningActivity = null,
        onNewActivity   = {},
        onHistory       = {},
        onInsights      = {}
    )
}