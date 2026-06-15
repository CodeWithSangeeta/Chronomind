package com.sangeeta.chronomind.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp

val CM_Accent_Blue = Color(0xFF4F7CFF)
val CM_Glow_Amber = Color(0xFFFFB84D)
val CM_Glow_Shadow = Color(0x80996600)

@Composable
fun AuraBotIllustration(
    modifier: Modifier = Modifier,
    sizeDp: Int = 240
) {
    val infiniteTransition = rememberInfiniteTransition(label = "GlowBreathing")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAlpha"
    )

    val floatScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatScale"
    )

    Box(
        modifier = modifier.size(sizeDp.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .scale(floatScale)
        ) {
            val w = size.width
            val h = size.height
            val centerX = w / 2f
            val centerY = h / 2f

            val headRadius = w * 0.28f
            val headCenter = Offset(centerX, h * 0.36f)

            val visorWidth = headRadius * 1.55f
            val visorHeight = headRadius * 1.42f
            val visorTopLeft = Offset(
                headCenter.x - visorWidth / 2f,
                headCenter.y - visorHeight / 2f + h * 0.01f
            )

            val floorCenterY = h * 0.84f

            // 1. Base Layer: Ambient Environment Ground Rings
            drawFloorGlow(
                centerX = centerX,
                centerY = floorCenterY,
                maxWidth = w * 0.95f,
                maxHeight = h * 0.17f,
                glowAlpha = glowAlpha
            )

            // 2. Underlying Foundation: Body Structure Drawn FIRST to allow proper neck overlap
            drawBody(
                center = Offset(centerX, h * 0.67f),
                width = headRadius * 1.05f,
                height = headRadius * 0.52f
            )

            // 3. Middle Connection: Neck Segment linking body and head cavities
            drawNeck(
                centerX = centerX,
                topY = headCenter.y + headRadius * 0.72f,
                width = headRadius * 0.45f,
                height = headRadius * 0.20f
            )

            // 4. Ears: Integrated into back sides before drawing visor layer overrides
            drawEar(
                center = Offset(headCenter.x - headRadius * 0.98f, headCenter.y + 4.dp.toPx()),
                radius = headRadius * 0.22f,
                isLeft = true
            )

            drawEar(
                center = Offset(headCenter.x + headRadius * 0.98f, headCenter.y + 4.dp.toPx()),
                radius = headRadius * 0.22f,
                isLeft = false
            )

            // 5. Shell Layer: Head Base Dome
            drawHeadShell(
                center = headCenter,
                radius = headRadius
            )

            // 6. Core Interface: Visor System
            drawVisor(
                topLeft = visorTopLeft,
                size = Size(visorWidth, visorHeight)
            )

            // 7. Detail Overlays: Glowing Elements
            val eyeY = headCenter.y - headRadius * 0.02f
            val eyeOffsetX = headRadius * 0.38f
            val eyeRadius = headRadius * 0.115f

            drawGlowingEye(
                center = Offset(headCenter.x - eyeOffsetX, eyeY),
                radius = eyeRadius,
                glowAlpha = glowAlpha
            )

            drawGlowingEye(
                center = Offset(headCenter.x + eyeOffsetX, eyeY),
                radius = eyeRadius,
                glowAlpha = glowAlpha
            )

            drawSmile(
                centerX = headCenter.x,
                centerY = headCenter.y + headRadius * 0.42f,
                width = headRadius * 0.52f,
                height = headRadius * 0.22f,
                glowAlpha = glowAlpha
            )
        }
    }
}

private fun DrawScope.drawFloorGlow(
    centerX: Float,
    centerY: Float,
    maxWidth: Float,
    maxHeight: Float,
    glowAlpha: Float
) {
    val ringColor = CM_Glow_Amber

    for (i in 0..3) {
        val scale = 1f - (i * 0.16f)
        drawOval(
            color = ringColor.copy(alpha = (0.18f + (0.12f * (3 - i))) * glowAlpha),
            topLeft = Offset(
                centerX - (maxWidth * scale) / 2f,
                centerY - (maxHeight * scale) / 2f
            ),
            size = Size(maxWidth * scale, maxHeight * scale),
            style = Stroke(width = (1.6f + i).dp.toPx())
        )
    }

    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(
                CM_Glow_Amber.copy(alpha = 0.45f * glowAlpha),
                Color.Transparent
            ),
            center = Offset(centerX, centerY),
            radius = maxWidth * 0.28f
        ),
        topLeft = Offset(centerX - maxWidth * 0.2f, centerY - maxHeight * 0.18f),
        size = Size(maxWidth * 0.4f, maxHeight * 0.36f)
    )
}

private fun DrawScope.drawHeadShell(
    center: Offset,
    radius: Float
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFFFFF),
                Color(0xFFE4E1DA),
                Color(0xFFAAA59D),
                Color(0xFF5F5A53)
            ),
            center = Offset(
                center.x - radius * 0.34f,
                center.y - radius * 0.42f
            ),
            radius = radius * 1.45f
        ),
        radius = radius,
        center = center
    )

    drawCircle(
        color = Color.White.copy(alpha = 0.22f),
        radius = radius * 0.92f,
        center = center,
        style = Stroke(width = 2.dp.toPx())
    )
}

private fun DrawScope.drawEar(
    center: Offset,
    radius: Float,
    isLeft: Boolean
) {
    val finWidth = radius * 0.85f
    val finHeight = radius * 1.35f
    // Angles to match organic alignment with helmet curves
    val rotationAngle = if (isLeft) 15f else -15f
    val finOffsetX = if (isLeft) -radius * 0.3f else -radius * 0.1f

    // Wrap the ear layout in a canvas coordinate shift to tilt it
    withTransform({
        rotate(rotationAngle, pivot = center)
    }) {
        // Rear metallic mounting flange
        drawRoundRect(
            color = Color(0xFF5F5A53),
            topLeft = Offset(
                center.x + finOffsetX - finWidth / 1.5f,
                center.y - finHeight / 2f
            ),
            size = Size(finWidth, finHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                x = finWidth * 0.5f,
                y = finWidth * 0.5f
            )
        )

        // Main colored segment housing
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFF4CE),
                    Color(0xFFEBBF50),
                    Color(0xFF916A19)
                ),
                center = Offset(center.x - radius * 0.1f, center.y - radius * 0.1f),
                radius = radius * 1.1f
            ),
            radius = radius,
            center = center
        )

        // Polished center cap ring
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFFFFF), Color(0xFF8C867F))
            ),
            radius = radius * 0.65f,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

private fun DrawScope.drawVisor(
    topLeft: Offset,
    size: Size
) {
    drawOval(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1B1D22),
                Color(0xFF0B0C10)
            )
        ),
        topLeft = topLeft,
        size = size
    )

    drawOval(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.12f),
                Color.Transparent
            ),
            start = Offset(topLeft.x, topLeft.y),
            end = Offset(topLeft.x + size.width, topLeft.y + size.height)
        ),
        topLeft = topLeft + Offset(4.dp.toPx(), 4.dp.toPx()),
        size = Size(size.width - 8.dp.toPx(), size.height - 8.dp.toPx())
    )
}

private fun DrawScope.drawGlowingEye(
    center: Offset,
    radius: Float,
    glowAlpha: Float
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFF3B8),
                CM_Glow_Amber
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
                CM_Glow_Amber.copy(alpha = 0.55f * glowAlpha),
                Color.Transparent
            ),
            center = center,
            radius = radius * 1.9f
        ),
        radius = radius * 1.9f,
        center = center
    )
}

private fun DrawScope.drawSmile(
    centerX: Float,
    centerY: Float,
    width: Float,
    height: Float,
    glowAlpha: Float
) {
    val smilePath = Path().apply {
        moveTo(centerX - width / 2f, centerY)
        quadraticTo(
            centerX,
            centerY + height,
            centerX + width / 2f,
            centerY
        )
    }

    drawPath(
        path = smilePath,
        color = CM_Glow_Amber.copy(alpha = 0.35f * glowAlpha),
        style = Stroke(
            width = 10.dp.toPx(),
            cap = StrokeCap.Round
        )
    )

    drawPath(
        path = smilePath,
        color = CM_Glow_Amber,
        style = Stroke(
            width = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
}

private fun DrawScope.drawBody(
    center: Offset,
    width: Float,
    height: Float
) {
    // Main metallic body capsule
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFFFFF),
                Color(0xFFDFDCD6),
                Color(0xFF9E9990),
                Color(0xFF4B4741)
            ),
            center = Offset(center.x - width * 0.15f, center.y - height * 0.3f),
            radius = width * 1.1f
        ),
        topLeft = Offset(center.x - width / 2f, center.y - height / 2f),
        size = Size(width, height)
    )

    // Secondary lower lip frame detail matching the image perspective
    val curveInset = width * 0.12f
    val innerPath = Path().apply {
        moveTo(center.x - width / 2f + curveInset, center.y - height * 0.1f)
        quadraticTo(
            center.x, center.y + height * 0.38f,
            center.x + width / 2f - curveInset, center.y - height * 0.1f
        )
    }
    drawPath(
        path = innerPath,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF7A746C), Color(0xFF2E2B28))
        ),
        style = Stroke(width = 3.dp.toPx())
    )

    // Side joint nodes (Arm sockets)
    val jointRadius = height * 0.28f
    val socketBrush = Brush.radialGradient(
        colors = listOf(Color(0xFFDCD7CE), Color(0xFF5F5A53))
    )

    drawCircle(
        brush = socketBrush,
        radius = jointRadius,
        center = Offset(center.x - width * 0.44f, center.y + height * 0.08f)
    )

    drawCircle(
        brush = socketBrush,
        radius = jointRadius,
        center = Offset(center.x + width * 0.44f, center.y + height * 0.08f)
    )
}

private fun DrawScope.drawNeck(
    centerX: Float,
    topY: Float,
    width: Float,
    height: Float
) {
    // Renders the connector joint directly behind/inside the neck collar cavity
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF55504B),
                Color(0xFF201E1D)
            )
        ),
        topLeft = Offset(centerX - width / 2f, topY),
        size = Size(width, height),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
            x = width * 0.2f,
            y = height * 0.5f
        )
    )
}