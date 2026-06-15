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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.components.*
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun SpaceSummaryScreen(
    // All driven by ViewModel later — passed as plain strings for now
    focusAreas: List<String>,           // e.g. ["Study", "Exercise", "Reading"]
    accountabilityLabel: String,        // e.g. "Daily streaks"
    checkInLabel: String,               // e.g. "I decide when I've completed today"
    streakMissLabel: String,            // e.g. "Continue my streak"
    onCreateSpace: () -> Unit,
    currentStep: Int = 6,
    totalSteps: Int  = 8,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600),
        label = "summaryAlpha"
    )
    val slideY by animateDpAsState(
        targetValue = if (visible) 0.dp else 24.dp,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "summarySlide"
    )
    LaunchedEffect(Unit) { visible = true }

    OnboardingScaffold(
        buttonText = "Create My Space",
        onButtonClick = onCreateSpace,
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
                message = "You're completely ready\nto begin, Sangeeta."
            )

            Spacer(modifier = Modifier.height(24.dp))


            HighlightedHeadline(
                prefix = "We'll help you stay\n",
                highlight = "mindful,",
                suffix = " not perfect."
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Summary card
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = AuraColors.YellowGlow,
                        spotColor = AuraColors.YellowGlow
                    )
                    .clip(RoundedCornerShape(20.dp))
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
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Text(
                        text = "Your Space Summary",
                        style = AuraTypography.TitleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AuraColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SummaryRow(
                        icon = "📖",
                        label = "Focus areas",
                        value = focusAreas.joinToString(", ")
                    )
                    SummaryDivider()
                    SummaryRow(
                        icon = "🔥",
                        label = "Accountability",
                        value = accountabilityLabel
                    )
                    SummaryDivider()
                    SummaryRow(
                        icon = "🤍",
                        label = "Check-in style",
                        value = checkInLabel
                    )
                    SummaryDivider()
                    SummaryRow(
                        icon = "🔄",
                        label = "Streak on missed day",
                        value = streakMissLabel
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment     = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text  = icon,
            style = AuraTypography.HeadlineMedium
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text  = label,
                style = AuraTypography.LabelMedium,
                color = AuraColors.TextMuted
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text  = value,
                style = AuraTypography.BodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = AuraColors.TextPrimary
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Thin divider between rows
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SummaryDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(AuraColors.CardBorderDefault.copy(alpha = 0.5f))
    )
}