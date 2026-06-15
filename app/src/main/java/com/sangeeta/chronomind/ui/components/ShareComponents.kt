package com.sangeeta.chronomind.ui.components

import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun OnboardingProgressBar(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(totalSteps) { index ->
            val isActive  = index == currentStep
            val isPast    = index < currentStep
            val animWidth by animateDpAsState(
                targetValue = if (isActive) 52.dp else 28.dp,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "progressWidth"
            )
            val color by animateColorAsState(
                targetValue = when {
                    isPast || isActive -> AuraColors.YellowPrimary
                    else -> AuraColors.CardBorderDefault
                },
                label = "progressColor"
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(animWidth)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}



@Composable
fun AuraBotBubble(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AuraColors.YellowSoft,
                            AuraColors.YellowPrimary.copy(alpha = 0.6f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✦",
                color = AuraColors.TextOnYellow,
                style = AuraTypography.LabelMedium
            )
        }

        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 14.dp,
                        bottomStart = 14.dp,
                        bottomEnd = 14.dp
                    )
                )
                .background(AuraColors.SurfaceCardLight)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message,
                color = AuraColors.TextPrimary,
                style = AuraTypography.BodyMedium
            )
        }
    }
}


@Composable
fun HighlightedHeadline(
    prefix: String,
    highlight: String,
    suffix: String = "",
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.text.BasicText(
        text = buildAnnotatedString {
            append(prefix)
            pushStyle(
                androidx.compose.ui.text.SpanStyle(
                    color = AuraColors.YellowPrimary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )
            append(highlight)
            pop()
            append(suffix)
        },
        style = AuraTypography.DisplayLarge.copy(
            color = AuraColors.TextPrimary,
            textAlign = TextAlign.Center
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}


@Composable
fun AuraOptionCard(
    icon: String,
    title: String,
    subtitle: String = "",
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) AuraColors.CardBorderSelected
        else  AuraColors.CardBorderDefault,
        animationSpec = tween(200),
        label = "cardBorder"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) AuraColors.SurfaceSelected
        else AuraColors.SurfaceCard,
        animationSpec = tween(200),
        label = "cardBg"
    )
    val shadowColor = if (isSelected) AuraColors.YellowGlow else Color.Transparent

    Box(
        modifier = modifier
            .height(80.dp)
            .shadow(
                elevation = if (isSelected) 12.dp else 0.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        bgColor.copy(alpha = 0.95f),
                        bgColor
                    )
                )
            )
            .border(
                width = if (isSelected) 1.3.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
          contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(text = icon, style = AuraTypography.HeadlineMedium)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )

                Spacer(modifier =Modifier.height(4.dp))

                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = AuraTypography.BodySmall,
                        color = AuraColors.TextSecondary
                    )
                }
            }
        }
    }
}


@Composable
fun FocusAreaCard(
    icon: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) AuraColors.CardBorderSelected
        else AuraColors.CardBorderDefault,
        animationSpec = tween(200),
        label = "focusBorder"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) AuraColors.SurfaceSelected
        else AuraColors.SurfaceCard,
        animationSpec = tween(200),
        label = "focusBg"
    )
    val shadowColor = if (isSelected) AuraColors.YellowGlow else Color.Transparent

    Box(
        modifier = modifier
            .height(90.dp)
            .shadow(
                elevation = if (isSelected) 10.dp else 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        bgColor.copy(alpha = 0.9f),
                        bgColor
                    )
                )
            )
            .border(
                width = if (isSelected) 1.3.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = icon, style = AuraTypography.DisplayMedium)
            Text(
                text = label,
                style = AuraTypography.LabelMedium,
                color = AuraColors.TextPrimary
            )
        }
    }
}


@Composable
fun TimerCircle(
    progress: Float,
    timerText: String,
    sizeDp: Dp = 140.dp,
    modifier: Modifier = Modifier
) {
    val animProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "timerProgress"
    )

    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke      = 10.dp.toPx()
            val glowStroke  = 18.dp.toPx()
            val inset       = glowStroke / 2f
            val arcSize     = Size(size.width - glowStroke, size.height - glowStroke)
            val topLeft     = Offset(inset, inset)
            val startAngle  = -90f
            val sweepAngle  = 360f * animProgress

            // 1. Track ring
            drawArc(
                color       = AuraColors.TimerTrack,
                startAngle  = 0f,
                sweepAngle  = 360f,
                useCenter   = false,
                topLeft     = topLeft,
                size        = arcSize,
                style       = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            // 2. Glow arc (wider, low opacity)
            if (sweepAngle > 0f) {
                drawArc(
                    brush       = Brush.sweepGradient(
                        colors  = listOf(
                            Color.Transparent,
                            AuraColors.TimerGlow,
                            AuraColors.YellowPrimary.copy(alpha = 0.3f)
                        )
                    ),
                    startAngle  = startAngle,
                    sweepAngle  = sweepAngle,
                    useCenter   = false,
                    topLeft     = topLeft,
                    size        = arcSize,
                    style       = Stroke(width = glowStroke, cap = StrokeCap.Round)
                )
            }

            // 3. Sharp progress arc on top
            if (sweepAngle > 0f) {
                drawArc(
                    brush       = Brush.sweepGradient(
                        colors  = listOf(
                            AuraColors.YellowPrimary.copy(alpha = 0.4f),
                            AuraColors.YellowPrimary
                        )
                    ),
                    startAngle  = startAngle,
                    sweepAngle  = sweepAngle,
                    useCenter   = false,
                    topLeft     = topLeft,
                    size        = arcSize,
                    style       = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
        }

        // Timer text in center
        Text(
            text  = timerText,
            style = AuraTypography.TimerDisplay,
            color = AuraColors.TextPrimary
        )
    }
}


@Composable
fun AuraCTAButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val bgColor = if (enabled) AuraColors.YellowPrimary
    else  AuraColors.YellowPrimary.copy(alpha = 0.4f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 28.dp)
            .shadow(
                elevation   = if (enabled) 16.dp else 0.dp,
                shape       = RoundedCornerShape(52.dp),
                ambientColor = AuraColors.YellowGlow,
                spotColor   = AuraColors.YellowGlow
            )
            .clip(RoundedCornerShape(52.dp))
            .background(bgColor)
            .clickable(enabled = enabled, onClick = onClick),
          //  .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
            Text(
                text  = text,
                modifier = Modifier.align(Alignment.Center),
                style = AuraTypography.TitleMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                color = AuraColors.TextOnYellow
            )
    }
}