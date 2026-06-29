package com.sangeeta.chronomind.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.model.ActivityDisplayState
import com.sangeeta.chronomind.ui.model.ActivitySessionState
import kotlin.math.cos
import kotlin.math.sin

private val CardOuter = Color(0xFF070707)
private val CardInner = Color(0xFF101010)
private val SurfaceTop = Color(0xFF171717)
private val SurfaceMid = Color(0xFF131313)
private val SurfaceLow = Color(0xFF0C0C0C)
private val BorderSoft = Color.White.copy(alpha = 0.07f)
private val BorderGlow = Color(0x33FFC328)
private val TextPrimary = Color(0xFFF5F2EA)
private val TextSecondary = Color(0xFFB7B0A1)
private val TextMuted = Color(0xFF8A857B)
private val Gold = Color(0xFFFFC328)
private val GoldSoft = Color(0xFFFFD76A)
private val GoldDim = Color(0xFFD39A08)
private val Success = Color(0xFF6ED38B)

@Composable
fun FocusTimerCard(
    heroState: ActivityDisplayState?,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onSwitch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = heroState?.progress ?: 0f
    val timeText = heroState?.displayTime ?: "00:00"
    val title = heroState?.name ?: "No activity selected"
    val isRunning = heroState?.isRunning == true
    val isCompleted = heroState?.sessionState == ActivitySessionState.COMPLETED_TODAY
    val isStopwatch = heroState?.isStopwatch == true
    val targetMinutes = ((heroState?.targetSeconds ?: 0L) / 60L).toInt().coerceAtLeast(0)
    val streakDays = heroState?.streakDays ?: 0

    val statusText = when (heroState?.sessionState) {
        ActivitySessionState.RUNNING -> "Focus in session"
        ActivitySessionState.PENDING -> "Paused session"
        ActivitySessionState.COMPLETED_TODAY -> "Completed today"
        else -> "Start when ready"
    }

    val pulse = rememberInfiniteTransition(label = "focus-card-pulse")
    val glowAlpha by pulse.animateFloat(
        initialValue = if (isRunning) 0.28f else 0.12f,
        targetValue = if (isRunning) 0.62f else 0.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "focus-glow"
    )

    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 28.dp,
                shape = RoundedCornerShape(34.dp),
                ambientColor = Gold.copy(alpha = 0.12f),
                spotColor = Color.Black
            )
            .clip(RoundedCornerShape(34.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CardOuter, CardInner, SurfaceLow)
                )
            )
            .border(
                width = 1.dp,
                color = BorderSoft,
                shape = RoundedCornerShape(34.dp)
            )
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(34.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Gold.copy(alpha = glowAlpha * 0.18f),
                            Color.Transparent
                        ),
                        center = Offset(220f, 160f),
                        radius = 700f
                    )
                )
        )

        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = statusText,
                        color = if (isCompleted) Success else TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(
                                elevation = 18.dp,
                                shape = CircleShape,
                                ambientColor = Color.Black,
                                spotColor = Gold.copy(alpha = 0.18f)
                            )
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF1A1A1A), Color(0xFF0D0D0D))
                                )
                            )
                            .border(1.dp, BorderSoft, CircleShape)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { menuExpanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreHoriz,
                            contentDescription = "More details",
                            tint = Gold,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier
                            .background(Color.Black)
                            .border(1.dp, BorderSoft, RoundedCornerShape(18.dp))
                    ) {
                        DropdownMenuItem(
                            text = { MenuText("Type: ${if (isStopwatch) "Stopwatch" else "Target $targetMinutes min"}") },
                            onClick = { menuExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { MenuText("Streak: $streakDays days") },
                            onClick = { menuExpanded = false }
                        )
                        DropdownMenuItem(
                            text = {
                                MenuText(
                                    when (heroState?.sessionState) {
                                        ActivitySessionState.RUNNING -> "Session running"
                                        ActivitySessionState.PENDING -> "Session paused"
                                        ActivitySessionState.COMPLETED_TODAY -> "Already completed today"
                                        else -> "Not started yet"
                                    }
                                )
                            },
                            onClick = { menuExpanded = false }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1.18f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    SurfaceTop,
                                    SurfaceMid,
                                    SurfaceLow
                                )
                            )
                        )
                        .border(1.dp, BorderSoft, RoundedCornerShape(32.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DialCanvas(
                        progress = progress,
                        glowAlpha = glowAlpha,
                        modifier = Modifier.matchParentSize()
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF141414))
                                .border(1.dp, BorderSoft, RoundedCornerShape(18.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = if (isStopwatch) "Stopwatch" else "Focus timer",
                                color = GoldSoft,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Text(
                            text = timeText,
                            color = Gold,
                            fontSize = 46.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = statusText,
                            color = TextSecondary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(0.82f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoChip(
                        label = if (isStopwatch) "Mode" else "Target",
                        value = if (isStopwatch) "Stopwatch" else "$targetMinutes min",
                        accent = Gold,
                        icon = { TargetGlyph() }
                    )

                    InfoChip(
                        label = "Streak",
                        value = "$streakDays days",
                        accent = Gold,
                        icon = { StreakGlyph() }
                    )

                    InfoChip(
                        label = "Status",
                        value = when (heroState?.sessionState) {
                            ActivitySessionState.RUNNING -> "Running"
                            ActivitySessionState.PENDING -> "Paused"
                            ActivitySessionState.COMPLETED_TODAY -> "Done today"
                            else -> "Ready"
                        },
                        accent = if (isCompleted) Success else Gold,
                        icon = { StatusGlyph(isCompleted = isCompleted) }
                    )
                }
            }

            ControlBar(
                heroState = heroState,
                onStartFocus = onStartFocus,
                onPause = onPause,
                onFinish = onFinish,
                onSwitch = onSwitch
            )
        }
    }
}

@Composable
private fun MenuText(text: String) {
    Text(
        text = text,
        color = TextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun InfoChip(
    label: String,
    value: String,
    accent: Color,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF141414), Color(0xFF0E0E0E))
                )
            )
            .border(1.dp, BorderSoft, RoundedCornerShape(22.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    ambientColor = accent.copy(alpha = 0.12f),
                    spotColor = Color.Black
                )
                .clip(CircleShape)
                .background(Color(0xFF111111))
                .border(1.dp, BorderSoft, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun ControlBar(
    heroState: ActivityDisplayState?,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onSwitch: () -> Unit
) {
    val isRunning = heroState?.isRunning == true
    val isCompleted = heroState?.sessionState == ActivitySessionState.COMPLETED_TODAY

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF111111), Color(0xFF090909))
                )
            )
            .border(1.dp, BorderSoft, RoundedCornerShape(26.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            heroState == null -> {
                ActionButton(
                    label = "Start",
                    onClick = onStartFocus,
                    icon = { PlayGlyph() },
                    modifier = Modifier.weight(1f)
                )
            }

            isCompleted -> {
                ActionButton(
                    label = "Completed",
                    onClick = onStartFocus,
                    icon = { StatusGlyph(isCompleted = true) },
                    modifier = Modifier.weight(1f),
                    accent = Success
                )
            }

            isRunning -> {
                ActionButton(
                    label = "Pause",
                    onClick = onPause,
                    icon = { PauseGlyph() },
                    modifier = Modifier.weight(1f)
                )
                VerticalActionDivider()
                ActionButton(
                    label = "Finish",
                    onClick = onFinish,
                    icon = { FinishGlyph() },
                    modifier = Modifier.weight(1f)
                )
                VerticalActionDivider()
                ActionButton(
                    label = "Switch",
                    onClick = onSwitch,
                    icon = { SwitchGlyph() },
                    modifier = Modifier.weight(1f)
                )
            }

            else -> {
                ActionButton(
                    label = "Start",
                    onClick = onStartFocus,
                    icon = { PlayGlyph() },
                    modifier = Modifier.weight(1f)
                )
                VerticalActionDivider()
                ActionButton(
                    label = "Finish",
                    onClick = onFinish,
                    icon = { FinishGlyph() },
                    modifier = Modifier.weight(1f)
                )
                VerticalActionDivider()
                ActionButton(
                    label = "Switch",
                    onClick = onSwitch,
                    icon = { SwitchGlyph() },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun VerticalActionDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(34.dp)
            .background(Color.White.copy(alpha = 0.07f))
    )
}

@Composable
private fun ActionButton(
    label: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = Gold
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalAccent(accent, icon)
            }
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CompositionLocalAccent(accent: Color, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.Transparent)
    ) {
        content()
    }
}

@Composable
private fun DialCanvas(
    progress: Float,
    glowAlpha: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val outerR = size.minDimension / 2f
        val ringR = outerR * 0.78f
        val ringRect = Rect(
            left = cx - ringR,
            top = cy - ringR,
            right = cx + ringR,
            bottom = cy + ringR
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF222222), Color(0xFF0B0B0B)),
                center = Offset(cx, cy),
                radius = outerR
            ),
            radius = outerR
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.04f),
            radius = outerR,
            style = Stroke(width = 1.5.dp.toPx())
        )

        val tickCount = 60
        repeat(tickCount) { i ->
            val angle = Math.toRadians((i * 360.0 / tickCount) - 90.0)
            val isMajor = i % 5 == 0
            val tickStart = ringR - if (isMajor) 14.dp.toPx() else 10.dp.toPx()
            val tickEnd = ringR - 4.dp.toPx()
            drawLine(
                color = if (isMajor) Gold.copy(alpha = 0.75f) else Color.White.copy(alpha = 0.12f),
                start = Offset(
                    x = cx + cos(angle).toFloat() * tickStart,
                    y = cy + sin(angle).toFloat() * tickStart
                ),
                end = Offset(
                    x = cx + cos(angle).toFloat() * tickEnd,
                    y = cy + sin(angle).toFloat() * tickEnd
                ),
                strokeWidth = if (isMajor) 1.8.dp.toPx() else 1.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        drawArc(
            color = Color.White.copy(alpha = 0.07f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = ringRect.topLeft,
            size = ringRect.size,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )

        val sweep = (progress.coerceIn(0f, 1f) * 360f)

        if (sweep > 0f) {
            drawArc(
                color = Gold.copy(alpha = glowAlpha * 0.23f),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = ringRect.topLeft,
                size = ringRect.size,
                style = Stroke(width = 22.dp.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(GoldDim, Gold, GoldSoft, Gold),
                    center = Offset(cx, cy)
                ),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = ringRect.topLeft,
                size = ringRect.size,
                style = Stroke(width = 9.dp.toPx(), cap = StrokeCap.Round)
            )

            val endAngle = Math.toRadians((sweep - 90f).toDouble())
            val knobX = cx + cos(endAngle).toFloat() * ringR
            val knobY = cy + sin(endAngle).toFloat() * ringR

            drawCircle(
                color = Gold.copy(alpha = 0.18f),
                radius = 18.dp.toPx(),
                center = Offset(knobX, knobY)
            )
            drawCircle(
                color = GoldSoft,
                radius = 8.dp.toPx(),
                center = Offset(knobX, knobY)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.65f),
                radius = 2.5.dp.toPx(),
                center = Offset(knobX - 2.dp.toPx(), knobY - 2.dp.toPx())
            )
        }

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF171717), Color(0xFF0C0C0C)),
                center = Offset(cx, cy),
                radius = outerR * 0.64f
            ),
            radius = outerR * 0.64f
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.035f),
            radius = outerR * 0.64f,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

@Composable
private fun TargetGlyph() {
    Canvas(modifier = Modifier.size(22.dp)) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val r = size.minDimension / 2f
        drawCircle(Gold, r, c, style = Stroke(2.dp.toPx()))
        drawCircle(Gold, r * 0.58f, c, style = Stroke(1.6.dp.toPx()))
        drawCircle(Gold, r * 0.18f, c)
    }
}

@Composable
private fun StreakGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.48f, 0f)
            cubicTo(size.width * 0.92f, size.height * 0.24f, size.width, size.height * 0.58f, size.width * 0.58f, size.height)
            cubicTo(size.width * 0.12f, size.height * 0.84f, size.width * 0.10f, size.height * 0.42f, size.width * 0.48f, 0f)
        }
        drawPath(path, Gold)
    }
}

@Composable
private fun StatusGlyph(isCompleted: Boolean) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val color = if (isCompleted) Success else Gold
        drawCircle(color, radius = size.minDimension / 2f, style = Stroke(2.dp.toPx()))
        val check = Path().apply {
            moveTo(size.width * 0.24f, size.height * 0.55f)
            lineTo(size.width * 0.43f, size.height * 0.74f)
            lineTo(size.width * 0.77f, size.height * 0.34f)
        }
        drawPath(
            path = check,
            color = color,
            style = Stroke(
                width = 2.2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
private fun PauseGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val barWidth = size.width * 0.18f
        val top = size.height * 0.16f
        val height = size.height * 0.68f
        drawRoundRect(
            color = Gold,
            topLeft = Offset(size.width * 0.20f, top),
            size = Size(barWidth, height),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
        drawRoundRect(
            color = Gold,
            topLeft = Offset(size.width * 0.60f, top),
            size = Size(barWidth, height),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
    }
}

@Composable
private fun FinishGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        drawRoundRect(
            color = Gold,
            topLeft = Offset(size.width * 0.20f, size.height * 0.20f),
            size = Size(size.width * 0.60f, size.height * 0.60f),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
    }
}

@Composable
private fun SwitchGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)
        drawArc(
            color = Gold,
            startAngle = 35f,
            sweepAngle = 250f,
            useCenter = false,
            topLeft = Offset(size.width * 0.12f, size.height * 0.14f),
            size = Size(size.width * 0.70f, size.height * 0.70f),
            style = stroke
        )
        val path = Path().apply {
            moveTo(size.width * 0.74f, size.height * 0.18f)
            lineTo(size.width * 0.92f, size.height * 0.20f)
            lineTo(size.width * 0.82f, size.height * 0.35f)
        }
        drawPath(path, Gold, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
private fun PlayGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.28f, size.height * 0.18f)
            lineTo(size.width * 0.78f, size.height * 0.50f)
            lineTo(size.width * 0.28f, size.height * 0.82f)
            close()
        }
        drawPath(path, Gold)
    }
}