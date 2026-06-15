package com.sangeeta.chronomind.ui.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.components.*
import com.sangeeta.chronomind.ui.onboarding.FocusArea
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun FocusAreaScreen(
    selectedAreas: Set<FocusArea>,
    onToggleArea: (FocusArea) -> Unit,
    onContinue: () -> Unit,
    currentStep: Int = 0,
    totalSteps: Int = 8,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label         = "focusAlpha"
    )
    val slideY by animateDpAsState(
        targetValue   = if (visible) 0.dp else 24.dp,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label         = "focusSlide"
    )
    LaunchedEffect(Unit) { visible = true }

        OnboardingScaffold(
            buttonText = "Continue",
            onButtonClick = onContinue,
            buttonEnabled = selectedAreas.isNotEmpty(),
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

   AuraBotBubble(message = "What would you like to be more consistent with?")

            Spacer(modifier = Modifier.height(8.dp))

            // Sub label
            Text(
                text      = "Choose all that apply.",
                style     = AuraTypography.BodySmall,
                color     = AuraColors.TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Grid — 2 columns
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Chunk the 8 items into rows of 2
                FocusArea.entries.chunked(2).forEach { rowItems ->
                    Row(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { area ->
                            FocusAreaCard(
                                icon       = area.icon,
                                label      = area.label,
                                isSelected = area in selectedAreas,
                                onClick    = { onToggleArea(area) },
                                modifier   = Modifier.weight(1f)
                            )
                        }
                        // Fill empty slot if odd number in last row
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
