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
import com.sangeeta.chronomind.ui.onboarding.CheckInStyle
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun CheckInStyleScreen(
    selected: CheckInStyle?,
    onSelect: (CheckInStyle) -> Unit,
    onContinue: () -> Unit,
    currentStep: Int = 2,
    totalSteps: Int = 8,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label         = "checkInAlpha"
    )
    val slideY by animateDpAsState(
        targetValue   = if (visible) 0.dp else 24.dp,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label         = "checkInSlide"
    )
    LaunchedEffect(Unit) { visible = true }

    OnboardingScaffold(
        buttonText = "Continue",
        onButtonClick = onContinue,
        buttonEnabled = selected != null,
        footerText = "You control the app.\nThe app doesn't control you.",
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
                message = "Remember, showing up matters\nfar more than perfection."
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text      = "How do you want to\ncelebrate your\nconsistency here?",
                style     = AuraTypography.DisplayLarge,
                color     = AuraColors.TextPrimary,
                textAlign = TextAlign.Center,
                modifier  = Modifier.padding(horizontal = 28.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Option cards — single select
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CheckInStyle.entries.forEach { style ->
                    AuraOptionCard(
                        icon = style.icon,
                        title = style.title,
                        subtitle = style.subtitle,
                        isSelected = selected == style,
                        onClick = { onSelect(style) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }

        }
    }
}