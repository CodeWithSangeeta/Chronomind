package com.sangeeta.chronomind.ui.home


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.ui.components.ActivityCard
import com.sangeeta.chronomind.ui.components.ChronoTimerRing
import com.sangeeta.chronomind.ui.model.ActivityDisplayState
import com.sangeeta.chronomind.ui.model.ActivitySessionState
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
    val heroDisplayState by viewModel.heroDisplayState.collectAsStateWithLifecycle()
    val showFinishDialog by viewModel.showFinishDialog.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    var timerPulseTrigger by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val activity = context as? Activity

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        val activityId = viewModel.lastPermissionRequestedActivityId
            ?: return@rememberLauncherForActivityResult

        if (granted) {
            viewModel.continueStartFocusAfterPermission(activityId)
        } else {
            viewModel.onNotificationPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeViewModel.HomeEvent.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.continueStartFocusAfterPermission(event.activityId)
                    }
                }
            }
        }
    }

//    LaunchedEffect(Unit) {
//        viewModel.events.collect { event ->
//            when (event) {
//                is HomeViewModel.HomeEvent.RequestNotificationPermission -> {
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//                        viewModel.continueStartFocusAfterPermission(event.activityId)
//                    } else {
//                        val granted = ContextCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.POST_NOTIFICATIONS
//                        ) == PackageManager.PERMISSION_GRANTED
//
//                        if (granted) {
//                            viewModel.continueStartFocusAfterPermission(event.activityId)
//                        } else {
//                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                        }
//                    }
//                }
//            }
//        }
//    }

    LaunchedEffect(Unit) {
        viewModel.scrollToTimerSignal.collect {
            listState.animateScrollToItem(0)
            timerPulseTrigger++
        }
    }

    LaunchedEffect(heroDisplayState?.activityId, heroDisplayState?.isRunning) {
        if (heroDisplayState?.isRunning == true) {
            listState.animateScrollToItem(0)
            timerPulseTrigger++
        }
    }

    HomeScreenContent(
        uiState = uiState,
        heroDisplayState = heroDisplayState,
        showFinishDialog = showFinishDialog,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToAllActivities = onNavigateToAllActivities,
        onQuickActionClick = { action ->
            when (action.id) {
                "newactivity" -> onNavigateToCreateActivity()
                "history" -> onNavigateToHistory()
                "insights" -> onNavigateToInsights()
                "widgetsetup" -> onNavigateToWidgetSetup()
                "widgetpreview" -> onNavigateToWidgetPreview()
            }
        },
        onStartFocus = viewModel::startFocus,
        onPause = viewModel::pauseSession,
        onFinish = viewModel::requestFinish,
        onConfirmFinish = viewModel::confirmFinish,
        onCancelFinish = viewModel::cancelFinish,
        onRecentActivityClick = viewModel::onRecentActivitySelected,
        onStartActivityDirectly = viewModel::startActivityDirectly,
        listState = listState,
        timerPulseTrigger = timerPulseTrigger
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    heroDisplayState: ActivityDisplayState?,
    showFinishDialog: Boolean,
    onNavigateToSettings: () -> Unit,
    onNavigateToAllActivities: () -> Unit,
    onQuickActionClick: (HomeQuickAction) -> Unit,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onConfirmFinish: () -> Unit,
    onCancelFinish: () -> Unit,
    onRecentActivityClick: (Int) -> Unit,
    onStartActivityDirectly: (Int) -> Unit,
    listState: LazyListState,
    timerPulseTrigger: Int
){
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
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item(key = "focus_timer_card") {
                AnimatedHomeTimerCard(pulseTrigger = timerPulseTrigger) { animatedModifier ->
                    Box(modifier = animatedModifier) {
                        FocusTimerCard(
                            heroState = heroDisplayState,
                            onStartFocus = onStartFocus,
                            onPause = onPause,
                            onFinish = onFinish,
                            onSwitch = onNavigateToAllActivities
                        )
                    }
                }
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
                    ActivityCard(
                        activity = activity,
                        isSelected = activity.id == uiState.selectedActivity?.id,
                        onCardClick = { onRecentActivityClick(activity.id) },
                        onActionClick = {
                            when (activity.sessionState) {
                                ActivitySessionState.RUNNING -> onPause()
                                ActivitySessionState.PENDING,
                                ActivitySessionState.IDLE -> onStartActivityDirectly(activity.id)
                                ActivitySessionState.COMPLETED_TODAY -> Unit
                            }
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(6.dp)) }
        }
    }


    if (showFinishDialog) {
        AlertDialog(
            onDismissRequest = onCancelFinish,
            title = { Text("Mark as complete?") },
            text = {
                Text("This will finish today's session. You won't be able to run this activity again today.")
            },
            confirmButton = {
                TextButton(onClick = onConfirmFinish) {
                    Text("Mark Complete")
                }
            },
            dismissButton = {
                TextButton(onClick = onCancelFinish) {
                    Text("Cancel")
                }
            }
        )
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
                    .shadow(
                        12.dp,
                        CircleShape,
                        ambientColor = AuraColors.YellowGlow,
                        spotColor = AuraColors.YellowGlow
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                AuraColors.YellowPrimary.copy(alpha = 0.18f),
                                AuraColors.SurfaceCard
                            )
                        )
                    )
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
                .shadow(
                    10.dp,
                    CircleShape,
                    ambientColor = AuraColors.YellowGlow,
                    spotColor = AuraColors.YellowGlow
                )
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


@Composable
private fun TimerHeroCard(heroState: ActivityDisplayState?) {
    val activityName = heroState?.name?.uppercase() ?: "NO ACTIVITY SELECTED"
    val timerText = heroState?.displayTime ?: "00:00"
    val progress = heroState?.progress ?: 0f

    val subLabel = when (heroState?.sessionState) {
        ActivitySessionState.RUNNING -> "IN SESSION"
        ActivitySessionState.PENDING -> "PENDING"
        ActivitySessionState.COMPLETED_TODAY -> "COMPLETED TODAY"
        else -> "START WHEN READY"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF101010), AuraColors.BackgroundDark)
                )
            )
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(32.dp))
            .padding(horizontal = 20.dp, vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ChronoTimerRing(
                progress = progress,
                timerText = timerText,
                subLabel = subLabel,
                activityName = activityName,
                sizeDp = 250.dp
            )

            if (heroState != null && heroState.sessionState != ActivitySessionState.RUNNING) {
                Text(
                    text = heroState.name,
                    style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Medium),
                    color = AuraColors.YellowPrimary
                )
            }
        }
    }
}


@Composable
private fun SessionControls(
    heroState: ActivityDisplayState?,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit
) {
    when {
        heroState == null -> {
            PremiumPrimaryButton(
                text = "Start Focus",
                icon = Icons.Rounded.PlayArrow,
                onClick = onStartFocus
            )
        }

        heroState.sessionState == ActivitySessionState.COMPLETED_TODAY -> {
            PremiumPrimaryButton(
                text = "Completed Today",
                icon = Icons.Rounded.CheckCircle,
                onClick = {}
            )
        }

        heroState.isRunning -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PremiumPillButton(
                    text = "Pause",
                    icon = Icons.Rounded.Pause,
                    isPrimary = true,
                    modifier = Modifier.weight(1f),
                    onClick = onPause
                )
                PremiumPillButton(
                    text = "Finish",
                    icon = Icons.Rounded.Stop,
                    modifier = Modifier.weight(1f),
                    onClick = onFinish
                )
            }
        }

        else -> {
            PremiumPrimaryButton(
                text = if (heroState.sessionState == ActivitySessionState.PENDING) "Resume Focus" else "Start Focus",
                icon = Icons.Rounded.PlayArrow,
                onClick = onStartFocus
            )
        }
    }
}





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


@Composable
private fun AnimatedHomeTimerCard(
    pulseTrigger: Int,
    content: @Composable (Modifier) -> Unit
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(pulseTrigger) {
        if (pulseTrigger > 0) {
            scale.snapTo(0.96f)
            scale.animateTo(
                targetValue = 1.03f,
                animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    content(
        Modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
    )
}