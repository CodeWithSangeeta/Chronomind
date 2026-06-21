package com.sangeeta.chronomind.ui.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.components.*
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold
import com.sangeeta.chronomind.ui.onboarding.PreviewStatus
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography


private data class PreviewActivity(
    val name: String,
    val time: String,
    val progress: Float,
    val status: PreviewStatus
)

private val previewActivities = listOf(
    PreviewActivity("Math",     "2:15", 0.72f, PreviewStatus.RUNNING),
    PreviewActivity("Exercise", "0:35", 0.35f, PreviewStatus.PAUSED),
    PreviewActivity("Reading",  "0:20", 0.20f, PreviewStatus.READY)
)

@Composable
fun TimerPreviewScreen(
    onContinue: () -> Unit,
    currentStep: Int = 4,
    totalSteps: Int  = 7,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label         = "timerPreviewAlpha"
    )
    val slideY by animateDpAsState(
        targetValue   = if (visible) 0.dp else 24.dp,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label         = "timerPreviewSlide"
    )
    LaunchedEffect(Unit) { visible = true }

    // Animate the "running" timer to simulate live ticking
    val infiniteAnim  = rememberInfiniteTransition(label = "timerTick")
    val runningProgress by infiniteAnim.animateFloat(
        initialValue  = 0.65f,
        targetValue   = 0.80f,
        animationSpec = infiniteRepeatable(
            animation  = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "runningProg"
    )

    OnboardingScaffold(
        buttonText = "Continue",
        onButtonClick = onContinue,
        currentStep = currentStep,
        totalSteps = totalSteps,
        topContent = {
            AuraBotBubble(
                message = "You can pause, resume, or switch activities anytime",
                botImageSize = 160.dp
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = alpha)
                .offset(y = slideY)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                previewActivities.forEachIndexed { index, activity ->
                    val progress = if (index == 0) runningProgress else activity.progress
                    ActivityTimerCard(
                        name     = activity.name,
                        time     = activity.time,
                        progress = progress,
                        status   = activity.status
                    )
                }
            }
        }
    }
}


@Composable
private fun ActivityTimerCard(
    name: String,
    time: String,
    progress: Float,
    status: PreviewStatus,
    modifier: Modifier = Modifier
) {
    val statusLabel = when (status) {
        PreviewStatus.RUNNING -> "Running"
        PreviewStatus.PAUSED  -> "Paused"
        PreviewStatus.READY   -> "Ready"
    }
    val statusColor = when (status) {
        PreviewStatus.RUNNING -> AuraColors.YellowPrimary
        PreviewStatus.PAUSED  -> AuraColors.TextMuted
        PreviewStatus.READY   -> AuraColors.TextMuted
    }
    val actionIcon = when (status) {
        PreviewStatus.RUNNING -> "⏸"
        PreviewStatus.PAUSED  -> "▶"
        PreviewStatus.READY   -> "▶"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = if (status == PreviewStatus.RUNNING) 10.dp else 2.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = if (status == PreviewStatus.RUNNING) AuraColors.YellowGlow
                else Color.Transparent,
                spotColor    = if (status == PreviewStatus.RUNNING) AuraColors.YellowGlow
                else Color.Transparent
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AuraColors.SurfaceCardLight,
                        AuraColors.SurfaceCard
                    )
                )
            )
            .border(
                width = if (status == PreviewStatus.RUNNING) 1.5.dp else 1.dp,
                color =  AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Timer circle
            TimerCircle(
                progress  = progress,
                timerText = time,
                sizeDp    = 64.dp
            )

            // Name + status badge
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = name,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Status badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text  = statusLabel,
                        style = AuraTypography.BodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = statusColor
                    )
                }
            }

            // Action button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(AuraColors.SurfaceCard)
                    .border(
                        width = 1.dp,
                        color = AuraColors.CardBorderDefault,
                        shape = RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = actionIcon,
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextPrimary
                )
            }
        }
    }
}