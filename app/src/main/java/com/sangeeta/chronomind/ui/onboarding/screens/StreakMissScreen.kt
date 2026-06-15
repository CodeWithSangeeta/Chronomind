package com.sangeeta.chronomind.ui.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.components.*
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold
import com.sangeeta.chronomind.ui.onboarding.StreakMissChoice
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun StreakMissScreen(
    selected: StreakMissChoice?,
    onSelect: (StreakMissChoice) -> Unit,
    onContinue: () -> Unit,
    currentStep: Int = 3,
    totalSteps: Int = 8,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label         = "streakAlpha"
    )
    val slideY by animateDpAsState(
        targetValue   = if (visible) 0.dp else 24.dp,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label         = "streakSlide"
    )
    LaunchedEffect(Unit) { visible = true }

    OnboardingScaffold(
        buttonText = "Continue",
        onButtonClick = onContinue,
        buttonEnabled = selected != null,
        footerText = "You can set this preference\nfor each activity later.",
        currentStep = currentStep,
        totalSteps = totalSteps,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = alpha)
                .offset(y = slideY),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuraBotBubble(
                message = "Life happens! When you miss a day,\nhow should we handle your streak?"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Headline
            HighlightedHeadline(
                prefix    = "Miss a day?\nHere's how we'll\nhandle your ",
                highlight = "streak.",
                suffix    = ""
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Option cards
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StreakMissChoice.entries.forEach { choice ->
                    AuraOptionCard(
                        icon       = choice.icon,
                        title      = choice.title,
                        subtitle   = choice.subtitle,
                        isSelected = selected == choice,
                        onClick    = { onSelect(choice) },
                        modifier   = Modifier.fillMaxWidth()
                    )
                }
            }


        }
    }
}