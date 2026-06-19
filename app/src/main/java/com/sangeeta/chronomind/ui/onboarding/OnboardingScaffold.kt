package com.sangeeta.chronomind.ui.onboarding


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.components.AuraCTAButton
import com.sangeeta.chronomind.ui.components.OnboardingProgressBar
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun OnboardingScaffold(
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonEnabled: Boolean = true,
    showProgress: Boolean = true,
    currentStep: Int = 0,
    totalSteps: Int = 8,
    footerText: String? = null,
    footerContent: @Composable (() -> Unit)? = null,
    topContent: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark),
        containerColor = AuraColors.BackgroundDark,
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(AuraColors.BackgroundDark)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                footerContent?.invoke()

                if (!footerText.isNullOrBlank()) {
                    Text(
                        text = footerText,
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary,
                        textAlign = TextAlign.Center,
                    )
                }

                AuraCTAButton(
                    text = buttonText,
                    onClick = onButtonClick,
                    enabled = buttonEnabled
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AuraColors.BackgroundDark)
                .padding(top = 52.dp)
        ) {
            if (showProgress) {
                OnboardingProgressBar(
                    totalSteps = totalSteps,
                    currentStep = currentStep
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    topContent?.invoke()
                    content(innerPadding)
                }
            }
        }
    }
}
