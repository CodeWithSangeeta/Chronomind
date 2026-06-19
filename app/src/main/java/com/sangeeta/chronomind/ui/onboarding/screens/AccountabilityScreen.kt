package com.sangeeta.chronomind.ui.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.components.*
import com.sangeeta.chronomind.ui.onboarding.AccountabilityType
import com.sangeeta.chronomind.ui.onboarding.BubbleArrowDirection
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold


@Composable
fun AccountabilityScreen(
    selected: AccountabilityType?,
    onSelect: (AccountabilityType) -> Unit,
    onContinue: () -> Unit,
    currentStep: Int = 1,
    totalSteps: Int = 7,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label = "accountAlpha"
    )
    val slideY by animateDpAsState(
        targetValue = if (visible) 0.dp else 24.dp,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "accountSlide"
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
                message = "How do you want to stay on track?",
                botImageSize = 160.dp,
                arrowDirection = BubbleArrowDirection.LEFT
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
                AccountabilityType.entries.forEach { type ->
                    AuraOptionCard(
                        icon = type.icon,
                        title = type.title,
                        isSelected = selected == type,
                        onClick = { onSelect(type) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}