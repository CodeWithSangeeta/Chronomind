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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.components.*
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun WidgetPreviewScreen(
    onContinue: () -> Unit,
    currentStep: Int = 5,
    totalSteps: Int  = 8,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label         = "widgetAlpha"
    )
    val slideY by animateDpAsState(
        targetValue   = if (visible) 0.dp else 24.dp,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label         = "widgetSlide"
    )
    LaunchedEffect(Unit) { visible = true }

    // Live-ish timer animation for widget preview
    val infiniteAnim = rememberInfiniteTransition(label = "widgetTimer")
    val liveProgress by infiniteAnim.animateFloat(
        initialValue  = 0.60f,
        targetValue   = 0.78f,
        animationSpec = infiniteRepeatable(
            animation  = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "widgetProg"
    )

    OnboardingScaffold(
        buttonText = "Continue",
        onButtonClick = onContinue,
        currentStep = currentStep,
        totalSteps = totalSteps,
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
            AuraBotBubble(
                message = "Stay connected to your\nintentions without even\nopening the app."
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Widget previews column
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Small + Medium widgets side by side
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Small widget
                    WidgetCard(
                        label    = "Small Widget",
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            TimerCircle(
                                progress  = liveProgress,
                                timerText = "2:15",
                                sizeDp    = 72.dp
                            )
                            Row(
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = "🔥", fontSize = 12.sp)
                                Text(
                                    text  = "12 day streak",
                                    style = AuraTypography.BodySmall,
                                    color = AuraColors.TextSecondary
                                )
                            }
                        }
                    }

                    // Medium widget
                    WidgetCard(
                        label    = "Medium Widget",
                        modifier = Modifier.weight(1.5f)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text  = "Today",
                                style = AuraTypography.LabelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = AuraColors.TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            MediumWidgetRow("M", "Math",     "2h 15m", true)
                            MediumWidgetRow("M", "Exercise", "35m",    false)
                            MediumWidgetRow("AA","Reading",  "20m",    false)
                        }
                    }
                }

                // Large widget — full width
                WidgetCard(
                    label    = "Large Widget",
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Timer
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            TimerCircle(
                                progress  = liveProgress,
                                timerText = "2:15",
                                sizeDp    = 90.dp
                            )
                            Row(
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = "Mh 15s", style = AuraTypography.BodySmall,
                                    color = AuraColors.TextSecondary)
                                Spacer(modifier = Modifier.width(4.dp))
                                StatusDot(color = AuraColors.YellowPrimary)
                                Text(text = "Running", style = AuraTypography.BodySmall,
                                    color = AuraColors.YellowPrimary)
                            }
                        }

                        // Action buttons
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LargeWidgetAction("⏸", "Pause")
                            LargeWidgetAction("⇄", "Switch")
                            LargeWidgetAction("✓", "Finish")
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Widget card wrapper
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun WidgetCard(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier            = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text      = label,
            style     = AuraTypography.BodySmall,
            color     = AuraColors.TextMuted,
            textAlign = TextAlign.Center,
            modifier  = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation    = 8.dp,
                    shape        = RoundedCornerShape(18.dp),
                    ambientColor = AuraColors.YellowGlow,
                    spotColor    = AuraColors.YellowGlow
                )
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AuraColors.SurfaceCardLight,
                            AuraColors.SurfaceCard
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = AuraColors.CardBorderDefault,
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(content = content)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Medium widget activity row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MediumWidgetRow(
    abbr: String,
    name: String,
    time: String,
    isActive: Boolean
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Letter badge
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    if (isActive) AuraColors.YellowPrimary.copy(alpha = 0.2f)
                    else AuraColors.SurfaceCard
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text  = abbr.first().toString(),
                style = AuraTypography.BodySmall.copy(fontSize = 9.sp),
                color = if (isActive) AuraColors.YellowPrimary else AuraColors.TextMuted
            )
        }
        Text(
            text     = name,
            style    = AuraTypography.BodySmall,
            color    = AuraColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text  = time,
            style = AuraTypography.BodySmall,
            color = if (isActive) AuraColors.YellowPrimary else AuraColors.TextSecondary
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Large widget action button
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LargeWidgetAction(icon: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(AuraColors.SurfaceCardLight)
                .border(
                    width = 1.dp,
                    color = AuraColors.CardBorderDefault,
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text  = icon,
                style = AuraTypography.LabelMedium,
                color = AuraColors.TextPrimary
            )
        }
        Text(
            text  = label,
            style = AuraTypography.BodySmall.copy(fontSize = 10.sp),
            color = AuraColors.TextMuted
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Small status dot
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StatusDot(color: Color) {
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(color)
    )
}