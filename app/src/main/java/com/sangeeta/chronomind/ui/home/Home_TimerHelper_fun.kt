package com.sangeeta.chronomind.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
 fun DialCanvas(
    progress: Float,
    glowAlpha: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val outerR = size.minDimension / 2f
        val ringR = outerR * 0.80f
        val ringRect = Rect(
            left = cx - ringR,
            top = cy - ringR,
            right = cx + ringR,
            bottom = cy + ringR
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF262626), Color(0xFF101010), Color(0xFF080808)),
                center = Offset(cx, cy),
                radius = outerR
            ),
            radius = outerR
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.05f),
            radius = outerR,
            style = Stroke(width = 1.5.dp.toPx())
        )

        drawCircle(
            color = Color.Black.copy(alpha = 0.28f),
            radius = outerR * 0.93f,
            style = Stroke(width = 9.dp.toPx())
        )

        val tickCount = 60
        repeat(tickCount) { i ->
            val angle = Math.toRadians((i * 360.0 / tickCount) - 90.0)
            val isMajor = i % 5 == 0
            val tickStart = ringR - if (isMajor) 15.dp.toPx() else 10.dp.toPx()
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
            color = Color.White.copy(alpha = 0.08f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = ringRect.topLeft,
            size = ringRect.size,
            style = Stroke(width = 11.dp.toPx(), cap = StrokeCap.Round)
        )

        val sweep = progress.coerceIn(0f, 1f) * 360f
        if (sweep > 0f) {
            drawArc(
                color = Gold.copy(alpha = glowAlpha * 0.24f),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = ringRect.topLeft,
                size = ringRect.size,
                style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(GoldDim, Gold, GoldSoft, Gold, GoldDim),
                    center = Offset(cx, cy)
                ),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = ringRect.topLeft,
                size = ringRect.size,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )

            val endAngle = Math.toRadians((sweep - 90f).toDouble())
            val knobX = cx + cos(endAngle).toFloat() * ringR
            val knobY = cy + sin(endAngle).toFloat() * ringR

            drawCircle(
                color = Gold.copy(alpha = 0.20f),
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
                listOf(Color(0xFF171717), Color(0xFF0B0B0B)),
                center = Offset(cx, cy),
                radius = outerR * 0.63f
            ),
            radius = outerR * 0.63f
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.035f),
            radius = outerR * 0.63f,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

@Composable
 fun TargetGlyphCompact() {
    Canvas(modifier = Modifier.size(18.dp)) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val r = size.minDimension / 2f
        drawCircle(Gold, r, c, style = Stroke(1.8.dp.toPx()))
        drawCircle(Gold, r * 0.58f, c, style = Stroke(1.4.dp.toPx()))
        drawCircle(Gold, r * 0.18f, c)
    }
}

@Composable
 fun StreakGlyphCompact() {
    Canvas(modifier = Modifier.size(17.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.48f, 0f)
            cubicTo(
                size.width * 0.92f, size.height * 0.24f,
                size.width, size.height * 0.58f,
                size.width * 0.58f, size.height
            )
            cubicTo(
                size.width * 0.12f, size.height * 0.84f,
                size.width * 0.10f, size.height * 0.42f,
                size.width * 0.48f, 0f
            )
        }
        drawPath(path, Gold)
    }
}

@Composable
 fun StatusGlyph(isCompleted: Boolean) {
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
 fun PauseGlyph() {
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
 fun FinishGlyph() {
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
fun SwitchGlyph() {
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
        drawPath(
            path = path,
            color = Gold,
            style = Stroke(
                2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
fun PlayGlyph() {
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