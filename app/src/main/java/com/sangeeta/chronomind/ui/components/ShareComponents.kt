package com.sangeeta.chronomind.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.sangeeta.chronomind.R


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
    modifier: Modifier = Modifier,
    botImageSize: Dp = 200.dp,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        Image(
            painter = painterResource(id = R.drawable.bot_img),
            contentDescription = "Bot Avatar",
            modifier = Modifier
                .height(botImageSize)
                .align(Alignment.Top),
            contentScale = ContentScale.Fit,
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp, top = (botImageSize * 0.10f))
                .widthIn(max = 260.dp)
                .drawBehind {
                    val w = size.width
                    val h = size.height
                    val r = 38f
                    val arrowY = 52f

                    val path = Path().apply {
                        moveTo(r, 0f)
                        lineTo(w - r, 0f)
                        quadraticTo(w, 0f, w, r)
                        lineTo(w, h - r)
                        quadraticTo(w, h, w - r, h)
                        lineTo(r, h)
                        quadraticTo(0f, h, 0f, h - r)
                        lineTo(0f, arrowY + 18f)
                        lineTo(-20f, arrowY)
                        lineTo(0f, arrowY - 18f)
                        lineTo(0f, r)
                        quadraticTo(0f, 0f, r, 0f)
                        close()
                    }
                    drawPath(path = path, color = Color(0xFF1E1E1E))
                }
                .padding(start = 20.dp, end = 16.dp, top = 14.dp, bottom = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.White.copy(alpha = 0.95f),
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}


@Composable
fun AuraOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String = "",
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val iconTint = if (isSelected) {
        AuraColors.YellowPrimary
    } else {
        AuraColors.TextSecondary
    }

    val textColor = if (isSelected) {
        AuraColors.TextPrimary
    } else {
        AuraColors.TextPrimary.copy(alpha = 0.92f)
    }

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
            .height(90.dp)
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
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp,
                    style = AuraTypography.TitleMedium,
                    color = textColor
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
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconTint = if (isSelected) {
        AuraColors.YellowPrimary
    } else {
        AuraColors.TextSecondary
    }

    val textColor = if (isSelected) {
        AuraColors.TextPrimary
    } else {
        AuraColors.TextPrimary.copy(alpha = 0.92f)
    }

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
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = label,
                style = AuraTypography.LabelMedium,
                color = textColor
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

            drawArc(
                color       = AuraColors.TimerTrack,
                startAngle  = 0f,
                sweepAngle  = 360f,
                useCenter   = false,
                topLeft     = topLeft,
                size        = arcSize,
                style       = Stroke(width = stroke, cap = StrokeCap.Round)
            )

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