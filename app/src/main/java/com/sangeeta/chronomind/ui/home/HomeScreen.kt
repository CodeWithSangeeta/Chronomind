package com.sangeeta.chronomind.ui.home


import android.Manifest
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
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.ui.components.ActivityCard
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
    onNavigateToInsights: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val heroDisplayState by viewModel.heroDisplayState.collectAsStateWithLifecycle()
    val isTimerFinished =  heroDisplayState?.isStopwatch == false &&
                heroDisplayState?.sessionState == ActivitySessionState.PENDING &&
                (heroDisplayState?.targetSeconds ?: 0L) > 0L &&
                (heroDisplayState?.elapsedSeconds ?: 0L) >=
                (heroDisplayState?.targetSeconds ?: 0L)

    val showFinishDialog by viewModel.showFinishDialog.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    var timerPulseTrigger by remember { mutableIntStateOf(0) }


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
        isTimerFinished = isTimerFinished,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToAllActivities = onNavigateToAllActivities,
        onQuickActionClick = { action ->
            when (action.id) {
                "new_activity" -> onNavigateToCreateActivity()
                "history" -> onNavigateToHistory()
                "insights" -> onNavigateToInsights()
            }
        },
        onStartFocus = viewModel::startFocus,
        onPause = viewModel::pauseSession,
        onFinish = viewModel::requestFinish,
        onConfirmFinish = viewModel::confirmFinish,
        onCancelFinish = viewModel::cancelFinish,

        onStopFinishedTimer = viewModel::stopFinishedTimer,
        onCompleteFinishedTimer = viewModel::completeFinishedTimer,

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
    isTimerFinished: Boolean,
    onNavigateToSettings: () -> Unit,
    onNavigateToAllActivities: () -> Unit,
    onQuickActionClick: (HomeQuickAction) -> Unit,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onConfirmFinish: () -> Unit,
    onCancelFinish: () -> Unit,
    onStopFinishedTimer: () -> Unit,
    onCompleteFinishedTimer: () -> Unit,
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
                            selectedActivity = uiState.selectedActivity,
                            onStartFocus = onStartFocus,
                            onPause = onPause,
                            onFinish = onFinish,
                            onSwitch = onNavigateToAllActivities,
                            onNoActivitySelected = onNavigateToAllActivities
                        )

                        if (showFinishDialog) {
                            FinishSessionDialog(
                                onConfirm = onConfirmFinish,
                                onDismiss = onCancelFinish
                            )
                        }

                        if (isTimerFinished) {
                            TimerFinishedDialog(
                                onStop = onStopFinishedTimer,
                                onComplete = onCompleteFinishedTimer
                            )
                        }
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
                                ActivitySessionState.FINISHED_WAITING_FOR_USER,
                                ActivitySessionState.COMPLETED_TODAY -> Unit
                            }
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(6.dp)) }
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
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
            )

            Text(
                text = appName,
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary
            )
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(AuraColors.SurfaceCard)
                .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                .clickable(onClick = onSettingsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Settings",
                tint = AuraColors.TextPrimary
            )
        }
    }
}


@Composable
internal fun TimerFinishedDialog(
    onStop: () -> Unit,
    onComplete: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            // Intentionally empty.
            // The user must choose Stop or Mark Complete.
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 360.dp)
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Gold.copy(alpha = 0.14f),
                    spotColor = Color.Black
                )
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF161616),
                            Color(0xFF0B0B0B)
                        )
                    )
                )
                .border(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            Gold.copy(alpha = 0.34f),
                            Color.White.copy(alpha = 0.10f)
                        )
                    ),
                    RoundedCornerShape(28.dp)
                )
                .padding(22.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                /*
                 * Timer finished indicator
                 */
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Gold.copy(alpha = 0.24f),
                                    Color(0xFF181818)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Gold.copy(alpha = 0.32f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "0",
                        color = Gold,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Timer finished",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Your timer has reached zero. What would you like to do?",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    /*
                     * STOP
                     *
                     * Saves the session as incomplete and
                     * resets the activity.
                     */
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1B1B1B))
                            .border(
                                1.dp,
                                BorderSoft,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }
                            ) {
                                onStop()
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Stop",
                            color = TextSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    /*
                     * MARK COMPLETE
                     *
                     * Records a completed session and updates
                     * today's completion/streak.
                     */
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        GoldSoft,
                                        GoldDim
                                    )
                                )
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }
                            ) {
                                onComplete()
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Mark Complete",
                            color = Color(0xFF1A1200),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
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