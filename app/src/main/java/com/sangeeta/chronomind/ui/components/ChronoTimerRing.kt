package com.sangeeta.chronomind.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

/**
 * ChronoTimerRing — the 3D glowing timer ring from Image 3.
 * Features:
 *  - Deep shadow track ring
 *  - Neon amber outer bloom
 *  - Crisp sharp progress arc
 *  - Rotating star-dot at the arc tip (3D depth illusion)
 *  - Inner ambient radial fill
 */
@Composable
fun ChronoTimerRing(
    progress:     Float,   // 0f..1f
    timerText:    String,  // "1h 15m"
    subLabel:     String,  // "REMAINING"
    activityName: String,  // "STUDYING"
    sizeDp:       Dp = 200.dp,
    modifier:     Modifier = Modifier
) {
    val animProgress by animateFloatAsState(
        targetValue   = progress,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label         = "ringProgress"
    )

    val infiniteAnim = rememberInfiniteTransition(label = "ringGlow")

    val glowAlpha by infiniteAnim.animateFloat(
        initialValue  = 0.6f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringGlowAlpha"
    )

    val dotRotation by infiniteAnim.animateFloat(
        initialValue  = 0f,
        targetValue   = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing)
        ),
        label = "dotRot"
    )

    Box(
        modifier         = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = 360f * animProgress
            drawChronoRing(
                sweepAngle = sweepAngle,
                glowAlpha  = glowAlpha,
                dotRotation = dotRotation
            )
        }

        // Center text stack
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text  = timerText,
                style = AuraTypography.TimerDisplay.copy(
                    fontSize   = 36.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = AuraColors.TextPrimary
            )
            Text(
                text  = subLabel,
                style = AuraTypography.LabelMedium.copy(
                    letterSpacing = 2.sp,
                    fontSize      = 10.sp
                ),
                color = AuraColors.TextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Activity name badge
            Text(
                text  = activityName,
                style = AuraTypography.LabelMedium.copy(
                    letterSpacing = 3.sp,
                    fontWeight    = FontWeight.Bold,
                    fontSize      = 12.sp
                ),
                color = AuraColors.YellowPrimary
            )
        }
    }
}

private fun DrawScope.drawChronoRing(
    sweepAngle:  Float,
    glowAlpha:   Float,
    dotRotation: Float
) {
    val strokeTrack   = 14.dp.toPx()
    val strokeGlow    = 30.dp.toPx()
    val strokeSharp   = 14.dp.toPx()
    val inset         = strokeGlow / 2f
    val arcSize       = Size(size.width - strokeGlow, size.height - strokeGlow)
    val arcTopLeft    = Offset(inset, inset)
    val startAngle    = -90f

    // 1. Deep shadow track (gives 3D inset feel)
    drawArc(
        brush      = Brush.sweepGradient(
            colors = listOf(
                Color(0xFF1A1A1A),
                Color(0xFF252525),
                Color(0xFF1A1A1A)
            )
        ),
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter  = false,
        topLeft    = arcTopLeft,
        size       = arcSize,
        style      = Stroke(width = strokeTrack + 6.dp.toPx(), cap = StrokeCap.Round)
    )

    // 2. Track ring base
    drawArc(
        color      = Color(0xFF222222),
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter  = false,
        topLeft    = arcTopLeft,
        size       = arcSize,
        style      = Stroke(width = strokeTrack, cap = StrokeCap.Round)
    )

    if (sweepAngle > 0f) {
        // 3. Wide neon bloom (outer glow layer)
        drawArc(
            brush      = Brush.sweepGradient(
                colors = listOf(
                    Color.Transparent,
                    AuraColors.YellowPrimary.copy(alpha = 0.15f * glowAlpha),
                    AuraColors.YellowPrimary.copy(alpha = 0.5f * glowAlpha),
                    AuraColors.YellowPrimary.copy(alpha = 0.15f * glowAlpha),
                    Color.Transparent
                )
            ),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter  = false,
            topLeft    = arcTopLeft,
            size       = arcSize,
            style      = Stroke(width = strokeGlow, cap = StrokeCap.Round)
        )

        // 4. Sharp crisp progress arc on top
        drawArc(
            brush      = Brush.sweepGradient(
                colors = listOf(
                    AuraColors.YellowPrimary.copy(alpha = 0.5f),
                    AuraColors.YellowPrimary,
                    AuraColors.YellowPrimary,
                    AuraColors.YellowPrimary.copy(alpha = 0.5f)
                )
            ),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter  = false,
            topLeft    = arcTopLeft,
            size       = arcSize,
            style      = Stroke(width = strokeSharp, cap = StrokeCap.Round)
        )

        // 5. Inner ambient radial fill (3D depth)
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        drawCircle(
            brush  = Brush.radialGradient(
                colors = listOf(
                    AuraColors.YellowPrimary.copy(alpha = 0.04f * glowAlpha),
                    Color.Transparent
                ),
                center = Offset(centerX, centerY),
                radius = size.width * 0.4f
            ),
            radius = size.width * 0.4f,
            center = Offset(centerX, centerY)
        )

        // 6. Glowing dot at arc tip (3D star on the rim)
        val angleRad = Math.toRadians((startAngle + sweepAngle).toDouble())
        val radius   = arcSize.width / 2f
        val dotX     = centerX + (radius * Math.cos(angleRad)).toFloat()
        val dotY     = centerY + (radius * Math.sin(angleRad)).toFloat()

        // Outer bloom on dot
        drawCircle(
            brush  = Brush.radialGradient(
                colors = listOf(
                    AuraColors.YellowPrimary.copy(alpha = 0.6f * glowAlpha),
                    Color.Transparent
                ),
                center = Offset(dotX, dotY),
                radius = 18.dp.toPx()
            ),
            radius = 18.dp.toPx(),
            center = Offset(dotX, dotY)
        )
        // Crisp dot
        drawCircle(
            color  = Color.White,
            radius = 6.dp.toPx(),
            center = Offset(dotX, dotY)
        )
        drawCircle(
            color  = AuraColors.YellowPrimary,
            radius = 4.dp.toPx(),
            center = Offset(dotX, dotY)
        )
    }
}