package com.sangeeta.chronomind.ui.onboarding.screens


import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.ui.components.AuraCTAButton
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun IntroScreen(
    onGetStarted: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "splashAlpha"
    )
    val slideY by animateDpAsState(
        targetValue = if (visible) 0.dp else 30.dp,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "splashSlide"
    )

   LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = modifier
            .fillMaxSize()
           .background(AuraColors.BackgroundDark)
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = slideY)
                .graphicsLayer(alpha = alpha),
            verticalArrangement   = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top= 36.dp, bottom = 0.dp, start = 0.dp, end = 40.dp)
                    .clickable(onClick = onSkip),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text  = "Skip",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextMuted,
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Small actions shape\n")
                        withStyle(
                            SpanStyle(
                                color      = AuraColors.YellowPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("extraordinary lives.")
                        }
                    },
                    style     = AuraTypography.DisplayLarge.copy(
                        fontSize  = 32.sp,
                        lineHeight = 42.sp
                    ),
                    color     = AuraColors.TextPrimary
                )

                Text(
                    text  = "Track what matters. Show up\nconsistently. Build a life you're proud of.",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AuraCTAButton(
                text    = "Get Started",
                onClick = onGetStarted
            )

            Spacer(modifier = Modifier.height(46.dp))
        }
    }
}