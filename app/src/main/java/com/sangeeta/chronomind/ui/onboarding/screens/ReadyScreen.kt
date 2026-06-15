package com.sangeeta.chronomind.ui.onboarding.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.components.*
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun ReadyScreen(
    onCreateMySpace: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Entrance
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(700),
        label         = "readyAlpha"
    )
    LaunchedEffect(Unit) { visible = true }

    // Bot float
    val infiniteAnim = rememberInfiniteTransition(label = "readyFloat")
    val botFloat by infiniteAnim.animateFloat(
        initialValue  = 0f,
        targetValue   = -14f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "readyBotY"
    )
    // Outer ring pulse
    val ringScale by infiniteAnim.animateFloat(
        initialValue  = 0.90f,
        targetValue   = 1.10f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "readyRing"
    )
    // Sparkle twinkle
    val sparkleAlpha by infiniteAnim.animateFloat(
        initialValue  = 0.3f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkleTwinkle"
    )

//    OnboardingScaffold(
//        buttonText = "Create My Space",
//        onButtonClick = onCreateSpace,
//        currentStep = currentStep,
//        totalSteps = totalSteps,
//        modifier = modifier
//    ) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
    ) {
        // Background radial glow
        Box(
            modifier = Modifier
                .size(340.dp)
                .align(Alignment.Center)
                .offset(y = (-40).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AuraColors.YellowGlow.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = alpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Bot bubble
            AuraBotBubble(
                message = "You're completely ready\nto begin, Sangeeta."
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // Bot with ring + sparkles
            Box(
                modifier         = Modifier
                    .size(220.dp)
                    .offset(y = botFloat.dp),
                contentAlignment = Alignment.Center
            ) {
                // Pulsing ring
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(ringScale)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    AuraColors.YellowGlow.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )

                // Sparkle top-right
                Text(
                    text     = "✦",
                    style    = AuraTypography.HeadlineMedium,
                    color    = AuraColors.YellowPrimary.copy(alpha = sparkleAlpha),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-10).dp, y = 20.dp)
                )

                // Sparkle top-left smaller
                Text(
                    text     = "✦",
                    style    = AuraTypography.BodyMedium,
                    color    = AuraColors.YellowSoft.copy(alpha = sparkleAlpha * 0.7f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 14.dp, y = 28.dp)
                )

                // Bot illustration
                AuraBotIllustration()
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Headline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier            = Modifier.padding(horizontal = 28.dp)
            ) {
                HighlightedHeadline(
                    prefix    = "Let's start building\nyour ",
                    highlight = "best self,",
                    suffix    = " ✦"
                )
                Text(
                    text      = "one day at a time.",
                    style     = AuraTypography.DisplayLarge,
                    color     = AuraColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // CTA
            AuraCTAButton(
                text    = "Create My Space",
                onClick = onCreateMySpace
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}