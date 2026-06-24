@file:Suppress("LongMethod")

package com.chronomind.ui.components

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.SyncAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
private object PremiumTimerRingDefaults {
    val Background = Color(0xFF000000)
    val Surface = Color(0xFF111111)
    val ElevatedSurface = Color(0xFF1A1A1A)
    val PrimaryAccent = Color(0xFFFFD700)
    val SecondaryAccent = Color(0xFFFFCC33)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF8E8E93)
    val DeepShadow = Color(0xFF050505)
    val RingShadow = Color(0xFF0B0B0B)
    val MetallicTop = Color(0x26FFFFFF)
    val MetallicDark = Color(0xFF1E1E1E)
    val DiscGlow = Color(0x33FFD700)
}

@Composable
fun PremiumTimerRing(
    modifier: Modifier = Modifier,
    totalSeconds: Int = 25 * 60,
    remainingSeconds: Int = 25 * 60,
    isRunning: Boolean = true,
    activityLabel: String = "Deep Work",
    subtitle: String = "Focus Time",
    onPauseClick: () -> Unit = {},
    onFinishClick: () -> Unit = {},
    onSwitchClick: () -> Unit = {},
) {
    val progress = (1f - (remainingSeconds.coerceIn(0, totalSeconds)).toFloat() / totalSeconds.toFloat())
        .coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "premium_timer_progress"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "premium_timer_glow")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isRunning) 1800 else 2800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )

    val haloAlpha by animateFloatAsState(
        targetValue = if (isRunning) 0.24f else 0.12f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "halo_alpha"
    )

    val ringGlowAlpha by animateFloatAsState(
        targetValue = if (isRunning) 0.95f else 0.55f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy),
        label = "ring_glow_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PremiumTimerRingDefaults.Background),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .aspectRatio(0.82f)
                .graphicsLayer {
                    shadowElevation = 46f
                    shape = RoundedCornerShape(40.dp)
                    clip = false
                    ambientShadowColor = Color.Black.copy(alpha = 0.70f)
                    spotShadowColor = PremiumTimerRingDefaults.PrimaryAccent.copy(alpha = 0.12f)
                }
                .clip(RoundedCornerShape(40.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            PremiumTimerRingDefaults.ElevatedSurface,
                            PremiumTimerRingDefaults.Surface,
                            Color(0xFF0C0C0C)
                        )
                    )
                )
                .drawBehind {
                    drawPremiumCardReflections()
                }
                .innerShadow(
                    shape = RoundedCornerShape(40.dp),
                    color = Color.Black.copy(alpha = 0.72f),
                    blur = 28.dp,
                    offsetX = 0.dp,
                    offsetY = 10.dp
                )
                .padding(horizontal = 24.dp, vertical = 26.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    PremiumTimerCanvas(
                        progress = animatedProgress,
                        formattedTime = remainingSeconds.toTimeString(),
                        subtitle = subtitle,
                        activityLabel = activityLabel,
                        glowPulse = glowPulse,
                        haloAlpha = haloAlpha,
                        ringGlowAlpha = ringGlowAlpha,
                        isRunning = isRunning
                    )
                }

                PremiumControlsBar(
                    onPauseClick = onPauseClick,
                    onFinishClick = onFinishClick,
                    onSwitchClick = onSwitchClick
                )
            }
        }
    }
}

@Composable
private fun PremiumTimerCanvas(
    progress: Float,
    formattedTime: String,
    subtitle: String,
    activityLabel: String,
    glowPulse: Float,
    haloAlpha: Float,
    ringGlowAlpha: Float,
    isRunning: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.92f)
                .blur(42.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PremiumTimerRingDefaults.PrimaryAccent.copy(alpha = haloAlpha * glowPulse),
                            PremiumTimerRingDefaults.SecondaryAccent.copy(alpha = haloAlpha * 0.45f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val minSide = min(size.width, size.height)
            val center = Offset(size.width / 2f, size.height / 2f)

            val outerShadowStroke = minSide * 0.085f
            val metalStroke = minSide * 0.072f
            val progressStroke = minSide * 0.052f
            val tickRadius = minSide * 0.425f
            val ringRadius = minSide * 0.355f
            val discRadius = minSide * 0.262f

            drawOuterShadowRing(center, ringRadius, outerShadowStroke)
            drawMetalRing(center, ringRadius, metalStroke)
            drawMinuteTicks(center, tickRadius, minSide)
            drawProgressGlow(center, ringRadius, progressStroke, progress, ringGlowAlpha)
            drawProgressRing(center, ringRadius, progressStroke, progress)
            drawInnerDisc(center, discRadius, isRunning, glowPulse)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formattedTime,
                color = PremiumTimerRingDefaults.TextPrimary,
                fontSize = 42.sp,
                lineHeight = 42.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                color = PremiumTimerRingDefaults.TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    PremiumTimerRingDefaults.PrimaryAccent,
                                    PremiumTimerRingDefaults.SecondaryAccent
                                )
                            )
                        )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = activityLabel,
                    color = PremiumTimerRingDefaults.PrimaryAccent,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}

private fun DrawScope.drawOuterShadowRing(
    center: Offset,
    radius: Float,
    stroke: Float
) {
    drawCircle(
        color = PremiumTimerRingDefaults.RingShadow.copy(alpha = 0.92f),
        radius = radius,
        center = center,
        style = Stroke(width = stroke)
    )

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                Color.Black.copy(alpha = 0.14f),
                Color.Black.copy(alpha = 0.34f)
            ),
            center = center,
            radius = radius + stroke
        ),
        radius = radius + stroke * 0.2f,
        center = center,
        style = Stroke(width = stroke * 0.92f)
    )
}

private fun DrawScope.drawMetalRing(
    center: Offset,
    radius: Float,
    stroke: Float
) {
    drawCircle(
        brush = Brush.sweepGradient(
            colors = listOf(
                Color(0xFF2A2A2A),
                Color(0xFF151515),
                Color(0xFF050505),
                Color(0xFF2E2E2E),
                Color(0xFF111111),
                Color(0xFF2A2A2A)
            ),
            center = center
        ),
        radius = radius,
        center = center,
        style = Stroke(width = stroke)
    )

    drawCircle(
        brush = Brush.sweepGradient(
            colors = listOf(
                Color.Transparent,
                Color.White.copy(alpha = 0.10f),
                Color.Transparent,
                Color.Black.copy(alpha = 0.18f),
                Color.Transparent
            ),
            center = center
        ),
        radius = radius - stroke * 0.1f,
        center = center,
        style = Stroke(width = stroke * 0.18f)
    )
}

private fun DrawScope.drawProgressGlow(
    center: Offset,
    radius: Float,
    stroke: Float,
    progress: Float,
    glowAlpha: Float
) {
    if (progress <= 0f) return

    drawArc(
        brush = Brush.sweepGradient(
            colors = listOf(
                PremiumTimerRingDefaults.PrimaryAccent.copy(alpha = 0.10f * glowAlpha),
                PremiumTimerRingDefaults.PrimaryAccent.copy(alpha = 0.65f * glowAlpha),
                PremiumTimerRingDefaults.SecondaryAccent.copy(alpha = 0.95f * glowAlpha),
                PremiumTimerRingDefaults.PrimaryAccent.copy(alpha = 0.20f * glowAlpha),
            ),
            center = center
        ),
        startAngle = -90f,
        sweepAngle = 360f * progress,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2f, radius * 2f),
        style = Stroke(width = stroke * 1.95f, cap = StrokeCap.Round),
        alpha = 0.55f
    )
}

private fun DrawScope.drawProgressRing(
    center: Offset,
    radius: Float,
    stroke: Float,
    progress: Float
) {
    drawArc(
        color = Color(0xFF1B1B1B),
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2f, radius * 2f),
        style = Stroke(width = stroke, cap = StrokeCap.Round)
    )

    if (progress <= 0f) return

    drawArc(
        brush = Brush.sweepGradient(
            colorStops = arrayOf(
                0.00f to Color(0xFFFFF2A8),
                0.22f to PremiumTimerRingDefaults.SecondaryAccent,
                0.58f to PremiumTimerRingDefaults.PrimaryAccent,
                0.84f to Color(0xFFFFE680),
                1.00f to Color(0xFFFFF2A8)
            ),
            center = center
        ),
        startAngle = -90f,
        sweepAngle = 360f * progress,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2f, radius * 2f),
        style = Stroke(width = stroke, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawMinuteTicks(
    center: Offset,
    radius: Float,
    minSide: Float
) {
    val longTick = minSide * 0.022f
    val shortTick = minSide * 0.012f

    repeat(60) { index ->
        val angleDeg = index * 6f - 90f
        val angleRad = angleDeg * (PI / 180f).toFloat()

        val isMajor = index % 5 == 0
        val tickLength = if (isMajor) longTick else shortTick
        val tickWidth = if (isMajor) minSide * 0.0048f else minSide * 0.0028f

        val outer = Offset(
            x = center.x + cos(angleRad) * radius,
            y = center.y + sin(angleRad) * radius
        )
        val inner = Offset(
            x = center.x + cos(angleRad) * (radius - tickLength),
            y = center.y + sin(angleRad) * (radius - tickLength)
        )

        drawLine(
            color = if (isMajor) {
                PremiumTimerRingDefaults.PrimaryAccent.copy(alpha = 0.48f)
            } else {
                Color(0x66A38E47)
            },
            start = inner,
            end = outer,
            strokeWidth = tickWidth,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawInnerDisc(
    center: Offset,
    radius: Float,
    isRunning: Boolean,
    glowPulse: Float
) {
    drawCircle(
        color = Color.Black.copy(alpha = 0.92f),
        radius = radius * 1.06f,
        center = center
    )

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF181818),
                Color(0xFF0B0B0B),
                Color(0xFF000000)
            ),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                PremiumTimerRingDefaults.DiscGlow.copy(
                    alpha = if (isRunning) 0.22f * glowPulse else 0.09f
                ),
                Color.Transparent
            ),
            center = center,
            radius = radius * 0.92f
        ),
        radius = radius * 0.92f,
        center = center
    )

    drawCircle(
        brush = Brush.sweepGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.09f),
                Color.Transparent,
                Color.Transparent,
                Color.Black.copy(alpha = 0.24f),
                Color.Transparent,
                Color.White.copy(alpha = 0.06f)
            ),
            center = center
        ),
        radius = radius * 0.98f,
        center = center,
        style = Stroke(width = radius * 0.055f)
    )
}

@Composable
private fun PremiumControlsBar(
    onPauseClick: () -> Unit,
    onFinishClick: () -> Unit,
    onSwitchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-4).dp)
            .graphicsLayer {
                shadowElevation = 28f
                shape = RoundedCornerShape(36.dp)
                clip = false
                ambientShadowColor = Color.Black.copy(alpha = 0.70f)
                spotShadowColor = PremiumTimerRingDefaults.PrimaryAccent.copy(alpha = 0.10f)
            }
            .clip(RoundedCornerShape(36.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B1B1B),
                        Color(0xFF121212),
                        Color(0xFF0D0D0D)
                    )
                )
            )
            .innerShadow(
                shape = RoundedCornerShape(36.dp),
                color = Color.Black.copy(alpha = 0.65f),
                blur = 22.dp,
                offsetY = 8.dp
            )
            .padding(horizontal = 22.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PremiumActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Pause,
                    contentDescription = "Pause",
                    tint = PremiumTimerRingDefaults.PrimaryAccent,
                    modifier = Modifier.size(22.dp)
                )
            },
            onClick = onPauseClick
        )

        PremiumActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Finish",
                    tint = PremiumTimerRingDefaults.PrimaryAccent,
                    modifier = Modifier.size(22.dp)
                )
            },
            onClick = onFinishClick
        )

        PremiumActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.SyncAlt,
                    contentDescription = "Switch",
                    tint = PremiumTimerRingDefaults.PrimaryAccent,
                    modifier = Modifier.size(22.dp)
                )
            },
            onClick = onSwitchClick
        )
    }
}

@Composable
private fun PremiumActionButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.93f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "premium_button_scale"
    )

    Box(
        modifier = Modifier
            .size(68.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shadowElevation = 18f
                shape = CircleShape
                clip = false
                ambientShadowColor = Color.Black.copy(alpha = 0.75f)
                spotShadowColor = PremiumTimerRingDefaults.PrimaryAccent.copy(alpha = 0.16f)
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF222222),
                        Color(0xFF151515),
                        Color(0xFF0E0E0E)
                    )
                )
            )
            .drawWithCache {
                onDrawWithContent {
                    drawContent()

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.10f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.35f, size.height * 0.28f),
                            radius = size.minDimension * 0.55f
                        )
                    )

                    drawCircle(
                        color = PremiumTimerRingDefaults.PrimaryAccent.copy(alpha = 0.12f),
                        radius = size.minDimension * 0.46f,
                        style = Stroke(width = size.minDimension * 0.025f)
                    )
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                        onClick()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

private fun Modifier.innerShadow(
    shape: androidx.compose.ui.graphics.Shape,
    color: Color = Color.Black,
    blur: Dp = 16.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp
): Modifier = drawWithCache {
    val outline = shape.createOutline(size, layoutDirection, this)
    val paint = Paint()

    onDrawWithContent {
        drawContent()

        drawIntoCanvas { canvas ->
            canvas.saveLayer(size.toRect(), paint)

            drawOutline(
                outline = outline,
                color = color,
                style = Stroke(width = 0f)
            )

            val frameworkPaint = paint.asFrameworkPaint().apply {
                isAntiAlias = true
                this.color = android.graphics.Color.TRANSPARENT
                setShadowLayer(
                    blur.toPx(),
                    offsetX.toPx(),
                    offsetY.toPx(),
                    color.copy(alpha = 0.95f).toArgb()
                )
                maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
            }

            drawIntoCanvas { canvas ->
                val outlinePath = outline.toComposePath()

                canvas.nativeCanvas.apply {
                    save()
                    clipPath(outlinePath.asAndroidPath())
                    drawPath(outlinePath.asAndroidPath(), frameworkPaint)
                    restore()
                }
            }

            canvas.restore()
        }
    }
}

private fun DrawScope.drawPremiumCardReflections() {
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                PremiumTimerRingDefaults.MetallicTop,
                Color.Transparent,
                Color.Transparent
            ),
            startY = 0f,
            endY = size.height * 0.20f
        ),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(40.dp.toPx(), 40.dp.toPx())
    )

    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.035f),
                Color.Transparent,
                Color.Black.copy(alpha = 0.18f)
            ),
            start = Offset(size.width * 0.15f, 0f),
            end = Offset(size.width * 0.85f, size.height)
        ),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(40.dp.toPx(), 40.dp.toPx())
    )
}

private fun Int.toTimeString(): String {
    val safe = coerceAtLeast(0)
    val minutes = safe / 60
    val seconds = safe % 60
    return "%02d:%02d".format(minutes, seconds)
}


private fun Outline.toComposePath(): Path {
    return when (this) {
        is Outline.Rectangle -> Path().apply {
            addRect(rect)
        }
        is Outline.Rounded -> Path().apply {
            addRoundRect(roundRect)
        }
        is Outline.Generic -> path
    }
}