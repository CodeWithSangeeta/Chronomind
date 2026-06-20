package com.sangeeta.chronomind.ui.onboarding.screens


import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.components.*
import com.sangeeta.chronomind.ui.onboarding.CheckInStyle
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold

@Composable
fun CheckInStyleScreen(
    selected: CheckInStyle?,
    onSelect: (CheckInStyle) -> Unit,
    onContinue: () -> Unit,
    currentStep: Int = 2,
    totalSteps: Int = 7,
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
        footerText = "You can change this later in settings.",
        currentStep = currentStep,
        totalSteps = totalSteps,
        topContent = {
            AuraBotBubble(
                message = "How would you like to check in each day?",
                botImageSize = 160.dp,
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = alpha)
                .offset(y = slideY),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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