package com.sangeeta.chronomind.ui.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material3.Icon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

private data class PreviewActivity(
    val name: String,
    val time: String,
    val progress: Float,
    val status: PreviewStatus
)

private val previewActivities = listOf(
    PreviewActivity("Exercise",     "2:15", 0.72f, PreviewStatus.RUNNING),
    PreviewActivity("Creative work", "0:35", 0.35f, PreviewStatus.PAUSED),
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
    val accent = when (name) {
        "Exercise" -> Color(0xFF87A788)
        "Creative work" -> Color(0xFFA67FA7)
        else -> Color(0xFFD2B54E)
    }

    val isTimer = (name == "Reading")

    val actionIcon = when (status) {
        PreviewStatus.RUNNING -> Icons.Rounded.Pause
        PreviewStatus.PAUSED -> Icons.Rounded.PlayArrow
        PreviewStatus.READY -> Icons.Rounded.PlayArrow
    }

    val supportText = when (status) {
        PreviewStatus.RUNNING -> null
        PreviewStatus.PAUSED -> "Last attempt: Today"
        PreviewStatus.READY -> "Last attempt: Yesterday"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AuraColors.SurfaceCardLight,
                        AuraColors.SurfaceCard
                    )
                )
            )
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(accent)
                        .border(width = 1.dp, color = accent.copy(alpha = 0.5f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isTimer) Icons.Rounded.HourglassEmpty else Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }


                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = name,
                        style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = AuraColors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = time,
                        style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Medium),
                        color = AuraColors.TextPrimary,
                        maxLines = 1
                    )


                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActivityMetaBadge(
                            label = if (isTimer) "Timer" else "Stopwatch",
                            textColor = accent,
                            backgroundColor = accent.copy(alpha = 0.10f),
                            borderColor = accent.copy(alpha = 0.18f),
                            leadingIcon = if (isTimer) Icons.Rounded.HourglassEmpty else Icons.Rounded.Bolt
                        )
                    }

                    if (supportText != null) {
                        Text(
                            text = supportText,
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(AuraColors.SurfaceCardLight)
                        .border(1.dp, AuraColors.CardBorderDefault, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null,
                        tint = AuraColors.YellowPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }


            if (isTimer) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = AuraColors.TextSecondary,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Progress",
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextMuted,
                            fontSize = 10.sp
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(AuraColors.TimerTrack)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(accent)
                            )
                        }
                    }
                }
            }
        }
    }
}