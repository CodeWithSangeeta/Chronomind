package com.sangeeta.chronomind.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

// ─── Color Palette ────────────────────────────────────────────────────────────
private val CardBg         = Color(0xFF1A1916)
private val CardBorder     = Color(0xFFB8860B)
private val SurfaceDark    = Color(0xFF111110)
private val SurfaceMid     = Color(0xFF1E1D1B)
private val GoldPrimary    = Color(0xFFFFC107)
private val GoldDim        = Color(0xFFB8860B)
private val GoldGlow       = Color(0x33FFC107)
private val TextPrimary    = Color(0xFFFFFFFF)
private val TextMuted      = Color(0xFF888880)
private val DialTick       = Color(0xFF3A3830)
private val DialTickMajor  = Color(0xFF6A6050)

// ─── FocusTimerCard ───────────────────────────────────────────────────────────
@Composable
fun FocusTimerCard(
    totalSeconds: Int = 45 * 60,
    remainingSeconds: Int = 45 * 60,
    sessionMode: String = "Deep Work",
    targetMinutes: Int = 45,
    streakDays: Int = 12,
    completedToday: Int = 1,
    totalToday: Int = 2,
    onPause: () -> Unit = {},
    onFinish: () -> Unit = {},
    onSwitch: () -> Unit = {},
) {
    val progress = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds else 0f
    val minutes  = remainingSeconds / 60
    val seconds  = remainingSeconds % 60
    val timeText = "%02d:%02d".format(minutes, seconds)

    // Pulsing glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(12.dp)
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = GoldDim.copy(alpha = 0.4f),
                ambientColor = GoldDim.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(CardBg)
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(GoldPrimary.copy(alpha = 0.9f), GoldDim.copy(alpha = 0.3f), GoldDim.copy(alpha = 0.1f))
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // ── Row 1: Dial + Stats ──────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Circular Dial
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    DialCanvas(
                        progress = progress,
                        glowAlpha = glowAlpha,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                    )
                    // Inner dial content
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Book icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "Mode Icon",
                                tint = GoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        // Timer text
                        Text(
                            text = timeText,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            letterSpacing = (-1).sp
                        )
                        Text(
                            text = "Focus Time",
                            fontSize = 13.sp,
                            color = TextMuted,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(Modifier.height(10.dp))
                        // Session Mode Chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .border(1.dp, GoldDim.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .background(SurfaceDark)
                                .padding(horizontal = 14.dp, vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Canvas(modifier = Modifier.size(10.dp)) {
                                    drawCircle(GoldPrimary.copy(alpha = 0.8f), radius = 4.dp.toPx())
                                    // Gear-like dot
                                    drawCircle(GoldDim, radius = 5.dp.toPx(), style = Stroke(1.dp.toPx()))
                                }
                                Text(
                                    text = sessionMode,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }

                // Stats Column
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.width(140.dp)
                ) {
                    // More button (top right)
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(SurfaceMid)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    StatChip(
                        iconContent = {
                            // Target/bullseye icon
                            Canvas(modifier = Modifier.size(22.dp)) {
                                val c = Offset(size.width / 2, size.height / 2)
                                val r1 = size.minDimension / 2
                                drawCircle(GoldPrimary, radius = r1, style = Stroke(2.dp.toPx()))
                                drawCircle(GoldPrimary, radius = r1 * 0.6f, style = Stroke(1.5.dp.toPx()))
                                drawCircle(GoldPrimary, radius = r1 * 0.2f)
                            }
                        },
                        label = "Target",
                        value = "$targetMinutes min"
                    )
                    StatChip(
                        iconContent = {
                            // Flame icon
                            Canvas(modifier = Modifier.size(22.dp)) {
                                val path = Path().apply {
                                    moveTo(size.width * 0.5f, 0f)
                                    cubicTo(size.width * 0.9f, size.height * 0.3f, size.width * 0.85f, size.height * 0.6f, size.width * 0.5f, size.height)
                                    cubicTo(size.width * 0.15f, size.height * 0.6f, size.width * 0.1f, size.height * 0.3f, size.width * 0.5f, 0f)
                                }
                                drawPath(path, GoldPrimary.copy(alpha = 0.9f))
                                drawPath(path, GoldDim.copy(alpha = 0.5f), style = Stroke(1.dp.toPx()))
                            }
                        },
                        label = "Streak",
                        value = "$streakDays days"
                    )
                    StatChip(
                        iconContent = {
                            // Check circle icon
                            Canvas(modifier = Modifier.size(22.dp)) {
                                val c = Offset(size.width / 2, size.height / 2)
                                val r = size.minDimension / 2
                                drawCircle(GoldPrimary, radius = r, style = Stroke(2.dp.toPx()))
                                val checkPath = Path().apply {
                                    moveTo(size.width * 0.25f, size.height * 0.5f)
                                    lineTo(size.width * 0.42f, size.height * 0.67f)
                                    lineTo(size.width * 0.75f, size.height * 0.33f)
                                }
                                drawPath(checkPath, GoldPrimary, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                            }
                        },
                        label = "Today",
                        value = "$completedToday/$totalToday\nCompleted"
                    )
                }
            }

            // ── Row 2: Control Buttons ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceDark)
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ControlButton(
                        onClick = onPause,
                        label = "Pause",
                        iconContent = {
                            // Pause bars
                            Canvas(Modifier.size(24.dp)) {
                                val barW = 5.dp.toPx()
                                val barH = 18.dp.toPx()
                                val top  = (size.height - barH) / 2
                                drawRoundRect(GoldPrimary, topLeft = Offset(size.width * 0.25f, top), size = Size(barW, barH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()))
                                drawRoundRect(GoldPrimary, topLeft = Offset(size.width * 0.60f, top), size = Size(barW, barH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()))
                            }
                        }
                    )
                    // Vertical divider
                    Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color.White.copy(alpha = 0.08f)))
                    ControlButton(
                        onClick = onFinish,
                        label = "Finish",
                        iconContent = {
                            Canvas(Modifier.size(24.dp)) {
                                drawRoundRect(
                                    GoldPrimary,
                                    topLeft = Offset(size.width * 0.2f, size.height * 0.2f),
                                    size    = Size(size.width * 0.6f, size.height * 0.6f),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx())
                                )
                            }
                        }
                    )
                    // Vertical divider
                    Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color.White.copy(alpha = 0.08f)))
                    ControlButton(
                        onClick = onSwitch,
                        label = "Switch",
                        iconContent = {
                            Canvas(Modifier.size(24.dp)) {
                                val stroke = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round)
                                val cx = size.width / 2; val cy = size.height / 2
                                val r  = size.minDimension * 0.38f
                                // Arc ~300deg
                                drawArc(
                                    color = GoldPrimary,
                                    startAngle = 50f,
                                    sweepAngle = 270f,
                                    useCenter = false,
                                    topLeft = Offset(cx - r, cy - r),
                                    size = Size(r * 2, r * 2),
                                    style = stroke
                                )
                                // Arrow tip
                                val tipAngle = Math.toRadians(50.0)
                                val tipX = cx + r * cos(tipAngle).toFloat()
                                val tipY = cy + r * sin(tipAngle).toFloat()
                                val arrowPath = Path().apply {
                                    moveTo(tipX - 5.dp.toPx(), tipY - 3.dp.toPx())
                                    lineTo(tipX, tipY + 4.dp.toPx())
                                    lineTo(tipX + 5.dp.toPx(), tipY - 3.dp.toPx())
                                }
                                drawPath(arrowPath, GoldPrimary, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                            }
                        }
                    )
                }
            }
        }
    }
}

// ─── Dial Canvas ──────────────────────────────────────────────────────────────
@Composable
private fun DialCanvas(
    progress: Float,
    glowAlpha: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2
        val cy = size.height / 2
        val outerR = size.minDimension / 2
        val arcR   = outerR * 0.90f
        val innerR = outerR * 0.72f

        // ── Outer raised bezel (neumorphic gradient) ──────────────────────
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF2A2820), Color(0xFF0E0D0B)),
                center = Offset(cx, cy),
                radius = outerR
            ),
            radius = outerR,
            center = Offset(cx, cy)
        )
        // Highlight on top-left (neumorphic)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF3A3830).copy(alpha = 0.6f), Color.Transparent),
                center = Offset(cx * 0.6f, cy * 0.6f),
                radius = outerR * 0.7f
            ),
            radius = outerR,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color(0xFF0A0908),
            radius = outerR,
            style = Stroke(2.dp.toPx())
        )

        // ── Tick marks ────────────────────────────────────────────────────
        val tickCount = 60
        for (i in 0 until tickCount) {
            val angle   = Math.toRadians((i * 360.0 / tickCount) - 90.0)
            val isMajor = i % 5 == 0
            val tickLen = if (isMajor) 8.dp.toPx() else 4.dp.toPx()
            val tickW   = if (isMajor) 1.5.dp.toPx() else 1.dp.toPx()
            val tickColor = if (isMajor) DialTickMajor else DialTick
            val r1 = arcR - 4.dp.toPx()
            val r2 = r1 - tickLen
            drawLine(
                color = tickColor,
                start = Offset(cx + r1 * cos(angle).toFloat(), cy + r1 * sin(angle).toFloat()),
                end   = Offset(cx + r2 * cos(angle).toFloat(), cy + r2 * sin(angle).toFloat()),
                strokeWidth = tickW,
                cap = StrokeCap.Round
            )
        }

        // ── Track arc (background) ────────────────────────────────────────
        val trackR  = arcR - 16.dp.toPx()
        val arcRect = androidx.compose.ui.geometry.Rect(Offset(cx - trackR, cy - trackR), Size(trackR * 2, trackR * 2))
        drawArc(
            color = Color(0xFF1E1D1B),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft  = arcRect.topLeft,
            size     = arcRect.size,
            style    = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
        )

        // ── Gold glow blur (simulated layered arcs) ───────────────────────
        val sweepAngle = progress * 360f
        if (sweepAngle > 0) {
            for (blur in listOf(20.dp.toPx(), 12.dp.toPx(), 6.dp.toPx())) {
                drawArc(
                    color = GoldPrimary.copy(alpha = glowAlpha * 0.15f),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft  = arcRect.topLeft,
                    size     = arcRect.size,
                    style    = Stroke(width = blur, cap = StrokeCap.Round)
                )
            }
            // Main gold arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(GoldDim, GoldPrimary, GoldPrimary),
                    center = Offset(cx, cy)
                ),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft  = arcRect.topLeft,
                size     = arcRect.size,
                style    = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
            )

            // ── Knob dot at arc end ────────────────────────────────────────
            val endAngle = Math.toRadians((sweepAngle - 90.0))
            val kx = cx + trackR * cos(endAngle).toFloat()
            val ky = cy + trackR * sin(endAngle).toFloat()
            // Glow halo
            drawCircle(GoldPrimary.copy(alpha = 0.3f * glowAlpha), radius = 10.dp.toPx(), center = Offset(kx, ky))
            drawCircle(GoldPrimary.copy(alpha = 0.15f), radius = 14.dp.toPx(), center = Offset(kx, ky))
            // Knob
            drawCircle(GoldPrimary, radius = 6.dp.toPx(), center = Offset(kx, ky))
            drawCircle(Color.White.copy(0.5f), radius = 2.5.dp.toPx(), center = Offset(kx - 1.dp.toPx(), ky - 1.dp.toPx()))
        }

        // ── Inner dial face ───────────────────────────────────────────────
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF1C1B19), Color(0xFF111010)),
                center = Offset(cx, cy),
                radius = innerR
            ),
            radius = innerR,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.04f),
            radius = innerR,
            style = Stroke(1.dp.toPx())
        )
    }
}

// ─── Stat Chip ────────────────────────────────────────────────────────────────
@Composable
private fun StatChip(
    iconContent: @Composable () -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceMid)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SurfaceDark),
            contentAlignment = Alignment.Center
        ) { iconContent() }
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextMuted,
                letterSpacing = 0.4.sp
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 16.sp
            )
        }
    }
}

// ─── Control Button ───────────────────────────────────────────────────────────
@Composable
private fun ControlButton(
    onClick: () -> Unit,
    label: String,
    iconContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Using TextButton as the clickable host
        androidx.compose.material3.TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.wrapContentSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                iconContent()
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 380)
@Composable
fun FocusTimerCardPreview() {
    MaterialTheme {
        FocusTimerCard()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 380)
@Composable
fun FocusTimerCardHalfwayPreview() {
    MaterialTheme {
        FocusTimerCard(
            totalSeconds = 45 * 60,
            remainingSeconds = 22 * 60 + 30,
            streakDays = 12,
            completedToday = 1,
            totalToday = 2
        )
    }
}