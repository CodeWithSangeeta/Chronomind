//package com.sangeeta.chronomind.ui.components
//
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.LinearEasing
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.size
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.drawscope.withTransform
//import androidx.compose.ui.unit.dp
//
//val CM_Accent_Blue = Color(0xFF4F7CFF)
//val CM_Glow_Amber = Color(0xFFFFB84D)
//val CM_Glow_Shadow = Color(0x80996600)
//
//@Composable
//fun AuraBotIllustration(
//    modifier: Modifier = Modifier,
//    sizeDp: Int = 240
//) {
//    val infiniteTransition = rememberInfiniteTransition(label = "GlowBreathing")
//
//    val glowAlpha by infiniteTransition.animateFloat(
//        initialValue = 0.65f,
//        targetValue = 0.95f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(2200, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "GlowAlpha"
//    )
//
//    val floatScale by infiniteTransition.animateFloat(
//        initialValue = 0.98f,
//        targetValue = 1.02f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(2200, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "FloatScale"
//    )
//
//    Box(
//        modifier = modifier.size(sizeDp.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Canvas(
//            modifier = Modifier
//                .fillMaxSize()
//                .scale(floatScale)
//        ) {
//            val w = size.width
//            val h = size.height
//            val centerX = w / 2f
//            val centerY = h / 2f
//
//            val headRadius = w * 0.28f
//            val headCenter = Offset(centerX, h * 0.36f)
//
//            val visorWidth = headRadius * 1.55f
//            val visorHeight = headRadius * 1.42f
//            val visorTopLeft = Offset(
//                headCenter.x - visorWidth / 2f,
//                headCenter.y - visorHeight / 2f + h * 0.01f
//            )
//
//            val floorCenterY = h * 0.84f
//
//            // 1. Base Layer: Ambient Environment Ground Rings
//            drawFloorGlow(
//                centerX = centerX,
//                centerY = floorCenterY,
//                maxWidth = w * 0.95f,
//                maxHeight = h * 0.17f,
//                glowAlpha = glowAlpha
//            )
//
//            // 2. Underlying Foundation: Body Structure Drawn FIRST to allow proper neck overlap
//            drawBody(
//                center = Offset(centerX, h * 0.67f),
//                width = headRadius * 1.05f,
//                height = headRadius * 0.52f
//            )
//
//            // 3. Middle Connection: Neck Segment linking body and head cavities
//            drawNeck(
//                centerX = centerX,
//                topY = headCenter.y + headRadius * 0.72f,
//                width = headRadius * 0.45f,
//                height = headRadius * 0.20f
//            )
//
//            // 4. Ears: Integrated into back sides before drawing visor layer overrides
//            drawEar(
//                center = Offset(headCenter.x - headRadius * 0.98f, headCenter.y + 4.dp.toPx()),
//                radius = headRadius * 0.22f,
//                isLeft = true
//            )
//
//            drawEar(
//                center = Offset(headCenter.x + headRadius * 0.98f, headCenter.y + 4.dp.toPx()),
//                radius = headRadius * 0.22f,
//                isLeft = false
//            )
//
//            // 5. Shell Layer: Head Base Dome
//            drawHeadShell(
//                center = headCenter,
//                radius = headRadius
//            )
//
//            // 6. Core Interface: Visor System
//            drawVisor(
//                topLeft = visorTopLeft,
//                size = Size(visorWidth, visorHeight)
//            )
//
//            // 7. Detail Overlays: Glowing Elements
//            val eyeY = headCenter.y - headRadius * 0.02f
//            val eyeOffsetX = headRadius * 0.38f
//            val eyeRadius = headRadius * 0.115f
//
//            drawGlowingEye(
//                center = Offset(headCenter.x - eyeOffsetX, eyeY),
//                radius = eyeRadius,
//                glowAlpha = glowAlpha
//            )
//
//            drawGlowingEye(
//                center = Offset(headCenter.x + eyeOffsetX, eyeY),
//                radius = eyeRadius,
//                glowAlpha = glowAlpha
//            )
//
//            drawSmile(
//                centerX = headCenter.x,
//                centerY = headCenter.y + headRadius * 0.42f,
//                width = headRadius * 0.52f,
//                height = headRadius * 0.22f,
//                glowAlpha = glowAlpha
//            )
//        }
//    }
//}
//
//private fun DrawScope.drawFloorGlow(
//    centerX: Float,
//    centerY: Float,
//    maxWidth: Float,
//    maxHeight: Float,
//    glowAlpha: Float
//) {
//    val ringColor = CM_Glow_Amber
//
//    for (i in 0..3) {
//        val scale = 1f - (i * 0.16f)
//        drawOval(
//            color = ringColor.copy(alpha = (0.18f + (0.12f * (3 - i))) * glowAlpha),
//            topLeft = Offset(
//                centerX - (maxWidth * scale) / 2f,
//                centerY - (maxHeight * scale) / 2f
//            ),
//            size = Size(maxWidth * scale, maxHeight * scale),
//            style = Stroke(width = (1.6f + i).dp.toPx())
//        )
//    }
//
//    drawOval(
//        brush = Brush.radialGradient(
//            colors = listOf(
//                CM_Glow_Amber.copy(alpha = 0.45f * glowAlpha),
//                Color.Transparent
//            ),
//            center = Offset(centerX, centerY),
//            radius = maxWidth * 0.28f
//        ),
//        topLeft = Offset(centerX - maxWidth * 0.2f, centerY - maxHeight * 0.18f),
//        size = Size(maxWidth * 0.4f, maxHeight * 0.36f)
//    )
//}
//
//private fun DrawScope.drawHeadShell(
//    center: Offset,
//    radius: Float
//) {
//    drawCircle(
//        brush = Brush.radialGradient(
//            colors = listOf(
//                Color(0xFFFFFFFF),
//                Color(0xFFE4E1DA),
//                Color(0xFFAAA59D),
//                Color(0xFF5F5A53)
//            ),
//            center = Offset(
//                center.x - radius * 0.34f,
//                center.y - radius * 0.42f
//            ),
//            radius = radius * 1.45f
//        ),
//        radius = radius,
//        center = center
//    )
//
//    drawCircle(
//        color = Color.White.copy(alpha = 0.22f),
//        radius = radius * 0.92f,
//        center = center,
//        style = Stroke(width = 2.dp.toPx())
//    )
//}
//
//private fun DrawScope.drawEar(
//    center: Offset,
//    radius: Float,
//    isLeft: Boolean
//) {
//    val finWidth = radius * 0.85f
//    val finHeight = radius * 1.35f
//    // Angles to match organic alignment with helmet curves
//    val rotationAngle = if (isLeft) 15f else -15f
//    val finOffsetX = if (isLeft) -radius * 0.3f else -radius * 0.1f
//
//    // Wrap the ear layout in a canvas coordinate shift to tilt it
//    withTransform({
//        rotate(rotationAngle, pivot = center)
//    }) {
//        // Rear metallic mounting flange
//        drawRoundRect(
//            color = Color(0xFF5F5A53),
//            topLeft = Offset(
//                center.x + finOffsetX - finWidth / 1.5f,
//                center.y - finHeight / 2f
//            ),
//            size = Size(finWidth, finHeight),
//            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
//                x = finWidth * 0.5f,
//                y = finWidth * 0.5f
//            )
//        )
//
//        // Main colored segment housing
//        drawCircle(
//            brush = Brush.radialGradient(
//                colors = listOf(
//                    Color(0xFFFFF4CE),
//                    Color(0xFFEBBF50),
//                    Color(0xFF916A19)
//                ),
//                center = Offset(center.x - radius * 0.1f, center.y - radius * 0.1f),
//                radius = radius * 1.1f
//            ),
//            radius = radius,
//            center = center
//        )
//
//        // Polished center cap ring
//        drawCircle(
//            brush = Brush.verticalGradient(
//                colors = listOf(Color(0xFFFFFFFF), Color(0xFF8C867F))
//            ),
//            radius = radius * 0.65f,
//            center = center,
//            style = Stroke(width = 2.dp.toPx())
//        )
//    }
//}
//
//private fun DrawScope.drawVisor(
//    topLeft: Offset,
//    size: Size
//) {
//    drawOval(
//        brush = Brush.verticalGradient(
//            colors = listOf(
//                Color(0xFF1B1D22),
//                Color(0xFF0B0C10)
//            )
//        ),
//        topLeft = topLeft,
//        size = size
//    )
//
//    drawOval(
//        brush = Brush.linearGradient(
//            colors = listOf(
//                Color.White.copy(alpha = 0.12f),
//                Color.Transparent
//            ),
//            start = Offset(topLeft.x, topLeft.y),
//            end = Offset(topLeft.x + size.width, topLeft.y + size.height)
//        ),
//        topLeft = topLeft + Offset(4.dp.toPx(), 4.dp.toPx()),
//        size = Size(size.width - 8.dp.toPx(), size.height - 8.dp.toPx())
//    )
//}
//
//private fun DrawScope.drawGlowingEye(
//    center: Offset,
//    radius: Float,
//    glowAlpha: Float
//) {
//    drawCircle(
//        brush = Brush.radialGradient(
//            colors = listOf(
//                Color(0xFFFFF3B8),
//                CM_Glow_Amber
//            ),
//            center = center,
//            radius = radius
//        ),
//        radius = radius,
//        center = center
//    )
//
//    drawCircle(
//        brush = Brush.radialGradient(
//            colors = listOf(
//                CM_Glow_Amber.copy(alpha = 0.55f * glowAlpha),
//                Color.Transparent
//            ),
//            center = center,
//            radius = radius * 1.9f
//        ),
//        radius = radius * 1.9f,
//        center = center
//    )
//}
//
//private fun DrawScope.drawSmile(
//    centerX: Float,
//    centerY: Float,
//    width: Float,
//    height: Float,
//    glowAlpha: Float
//) {
//    val smilePath = Path().apply {
//        moveTo(centerX - width / 2f, centerY)
//        quadraticTo(
//            centerX,
//            centerY + height,
//            centerX + width / 2f,
//            centerY
//        )
//    }
//
//    drawPath(
//        path = smilePath,
//        color = CM_Glow_Amber.copy(alpha = 0.35f * glowAlpha),
//        style = Stroke(
//            width = 10.dp.toPx(),
//            cap = StrokeCap.Round
//        )
//    )
//
//    drawPath(
//        path = smilePath,
//        color = CM_Glow_Amber,
//        style = Stroke(
//            width = 5.dp.toPx(),
//            cap = StrokeCap.Round
//        )
//    )
//}
//
//private fun DrawScope.drawBody(
//    center: Offset,
//    width: Float,
//    height: Float
//) {
//    // Main metallic body capsule
//    drawOval(
//        brush = Brush.radialGradient(
//            colors = listOf(
//                Color(0xFFFFFFFF),
//                Color(0xFFDFDCD6),
//                Color(0xFF9E9990),
//                Color(0xFF4B4741)
//            ),
//            center = Offset(center.x - width * 0.15f, center.y - height * 0.3f),
//            radius = width * 1.1f
//        ),
//        topLeft = Offset(center.x - width / 2f, center.y - height / 2f),
//        size = Size(width, height)
//    )
//
//    // Secondary lower lip frame detail matching the image perspective
//    val curveInset = width * 0.12f
//    val innerPath = Path().apply {
//        moveTo(center.x - width / 2f + curveInset, center.y - height * 0.1f)
//        quadraticTo(
//            center.x, center.y + height * 0.38f,
//            center.x + width / 2f - curveInset, center.y - height * 0.1f
//        )
//    }
//    drawPath(
//        path = innerPath,
//        brush = Brush.verticalGradient(
//            colors = listOf(Color(0xFF7A746C), Color(0xFF2E2B28))
//        ),
//        style = Stroke(width = 3.dp.toPx())
//    )
//
//    // Side joint nodes (Arm sockets)
//    val jointRadius = height * 0.28f
//    val socketBrush = Brush.radialGradient(
//        colors = listOf(Color(0xFFDCD7CE), Color(0xFF5F5A53))
//    )
//
//    drawCircle(
//        brush = socketBrush,
//        radius = jointRadius,
//        center = Offset(center.x - width * 0.44f, center.y + height * 0.08f)
//    )
//
//    drawCircle(
//        brush = socketBrush,
//        radius = jointRadius,
//        center = Offset(center.x + width * 0.44f, center.y + height * 0.08f)
//    )
//}
//
//private fun DrawScope.drawNeck(
//    centerX: Float,
//    topY: Float,
//    width: Float,
//    height: Float
//) {
//    // Renders the connector joint directly behind/inside the neck collar cavity
//    drawRoundRect(
//        brush = Brush.verticalGradient(
//            colors = listOf(
//                Color(0xFF55504B),
//                Color(0xFF201E1D)
//            )
//        ),
//        topLeft = Offset(centerX - width / 2f, topY),
//        size = Size(width, height),
//        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
//            x = width * 0.2f,
//            y = height * 0.5f
//        )
//    )
//}





package com.sangeeta.chronomind.ui.components

import android.R.attr.label
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp

// Precise, premium color tokens matching your mindfulness palette
val KaiAmber = Color(0xFFFFB84D)
val KaiDarkAmber = Color(0xFFCC7A00)
val KaiVisorBlack = Color(0xFF0F1013)

/**
 * KaiBot - A premium, highly-engineered minimalist onboarding companion.
 * Created entirely via declarative Canvas rendering for Apple-style fidelity.
 */
@Composable
fun AuraBotIllustration(
    modifier: Modifier = Modifier,
    sizeDp: Int = 220
) {
    val infiniteTransition = rememberInfiniteTransition(label = "KaiAnimationEngine")

    // Extremely subtle breathing aura for the eyes/smile to imply active warmth
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "WarmGlowAlpha"
    )

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "SereneFloat"
    )

    Box(
        modifier = modifier.size(sizeDp.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = floatOffset.dp.toPx() }
        ) {
            val w = size.width
            val h = size.height
            val centerX = w / 2f
            val centerY = h / 2f

            // Proportions locked directly to the primary reference asset
            val headRadius = w * 0.33f
            val headCenter = Offset(centerX, h * 0.40f)

            // ==========================================
            // 1. LOWER BODY BACKDROP (Drawn first for background separation)
            // ==========================================
            drawKaiBody(
                headCenter = headCenter,
                headRadius = headRadius,
                canvasHeight = h
            )

            // ==========================================
            // 2. CONNECTIVE PEDESTAL NECK
            // ==========================================
            drawKaiNeck(
                headCenter = headCenter,
                headRadius = headRadius
            )

            // ==========================================
            // 3. ENGINEERED CYLINDRICAL SIDE MODULES (Ears)
            // ==========================================
            // Left Side Module
            drawEngineeredEar(
                center = Offset(headCenter.x - headRadius * 0.96f, headCenter.y + (headRadius * 0.05f)),
                radius = headRadius * 0.22f,
                isLeft = true
            )
            // Right Side Module
            drawEngineeredEar(
                center = Offset(headCenter.x + headRadius * 0.96f, headCenter.y + (headRadius * 0.05f)),
                radius = headRadius * 0.22f,
                isLeft = false
            )

            // ==========================================
            // 4. MAIN METALLIC HELMET DOME
            // ==========================================
            drawHelmetShell(
                center = headCenter,
                radius = headRadius
            )

            // ==========================================
            // 5. PREMIUM DEEP MATTE VISOR SYSTEM
            // ==========================================
            val visorWidth = headRadius * 1.52f
            val visorHeight = headRadius * 1.34f
            val visorTopLeft = Offset(
                headCenter.x - visorWidth / 2f,
                headCenter.y - visorHeight / 2f + (headRadius * 0.08f)
            )

            drawVisorFaceplate(
                topLeft = visorTopLeft,
                size = Size(visorWidth, visorHeight)
            )

            // ==========================================
            // 6. MINIMALIST EMOTIONAL INTERFACES (Face Features)
            // ==========================================
            val eyeY = headCenter.y + (headRadius * 0.04f)
            val eyeSpacingX = headRadius * 0.36f
            val eyeRadius = headRadius * 0.11f

            // Left Eye
            drawInterfaceEye(
                center = Offset(headCenter.x - eyeSpacingX, eyeY),
                radius = eyeRadius,
                glowAlpha = glowAlpha
            )

            // Right Eye
            drawInterfaceEye(
                center = Offset(headCenter.x + eyeSpacingX, eyeY),
                radius = eyeRadius,
                glowAlpha = glowAlpha
            )

            // Supportive Smile
            drawSupportiveSmile(
                centerX = headCenter.x,
                centerY = headCenter.y + (headRadius * 0.40f),
                width = headRadius * 0.34f,
                height = headRadius * 0.14f,
                glowAlpha = glowAlpha
            )
        }
    }
}

private fun DrawScope.drawHelmetShell(center: Offset, radius: Float) {
    // Soft, luxurious layered metallic shading mimicking studio top-left key light
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFFFFF), // Crisp edge specular highlight
                Color(0xFFEAEAEA), // Primary silver base
                Color(0xFFC5C5C5), // Midtone shadow roll
                Color(0xFF7A7F85)  // Matte core termination shadow
            ),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.4f),
            radius = radius * 1.35f
        ),
        radius = radius,
        center = center
    )

    // Ultra-fine luxury outer rim internal glaze stroke
    drawCircle(
        color = Color.White.copy(alpha = 0.4f),
        radius = radius * 0.97f,
        center = center,
        style = Stroke(width = 1.dp.toPx())
    )
}

private fun DrawScope.drawEngineeredEar(center: Offset, radius: Float, isLeft: Boolean) {
    val moduleWidth = radius * 0.9f
    val moduleHeight = radius * 1.4f
    val sweepAngle = if (isLeft) 12f else -12f

    withTransform({
        rotate(sweepAngle, pivot = center)
    }) {
        // Step 1: Base dark connector bracket sitting flush behind helmet wall
        drawRoundRect(
            color = Color(0xFF5A5D64),
            topLeft = Offset(center.x - moduleWidth / 2f, center.y - moduleHeight / 2f),
            size = Size(moduleWidth, moduleHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(moduleWidth * 0.4f)
        )

        // Step 2: Primary cylindrical metallic casing block
        drawCircle(
            brush = Brush.linearGradient(
                colors = if (isLeft) listOf(Color(0xFFD2D2D2), Color(0xFF707379))
                else listOf(Color(0xFF707379), Color(0xFFD2D2D2)),
                start = Offset(center.x - radius, center.y),
                end = Offset(center.x + radius, center.y)
            ),
            radius = radius,
            center = center
        )

        // Step 3: Recessed golden-amber inner glow accent channel
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFF1C5), KaiAmber, KaiDarkAmber)
            ),
            radius = radius * 0.72f,
            center = center
        )

        // Step 4: Central polished metallic core lens cap
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFFFFF), Color(0xFF9EA1A8)),
                center = center,
                radius = radius * 0.45f
            ),
            radius = radius * 0.40f,
            center = center
        )
    }
}

private fun DrawScope.drawVisorFaceplate(topLeft: Offset, size: Size) {
    // Pure deep black luxury visor backdrop to eliminate cheap plastic gradients
    drawOval(
        color = KaiVisorBlack,
        topLeft = topLeft,
        size = size
    )

    // Sophisticated top-down inner glass reflection ring line
    drawOval(
        brush = Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = 0.08f), Color.Transparent),
            startY = topLeft.y,
            endY = topLeft.y + size.height * 0.4f
        ),
        topLeft = topLeft + Offset(3.dp.toPx(), 2.dp.toPx()),
        size = Size(size.width - 6.dp.toPx(), size.height - 4.dp.toPx()),
        style = Stroke(width = 1.5.dp.toPx())
    )
}

private fun DrawScope.drawInterfaceEye(center: Offset, radius: Float, glowAlpha: Float) {
    // Solid organic core
    drawCircle(
        color = KaiAmber,
        radius = radius,
        center = center
    )

    // Soft, reassuring inner hot-spot highlight
    drawCircle(
        color = Color(0xFFFFFCE6),
        radius = radius * 0.4f,
        center = center - Offset(radius * 0.15f, radius * 0.15f)
    )

    // Premium high-stiffness micro-bloom environmental glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(KaiAmber.copy(alpha = 0.4f * glowAlpha), Color.Transparent),
            center = center,
            radius = radius * 2.2f
        ),
        radius = radius * 2.2f,
        center = center
    )
}

private fun DrawScope.drawSupportiveSmile(
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

    // Outer subtle ambient light dispersion layer
    drawPath(
        path = smilePath,
        color = KaiAmber.copy(alpha = 0.25f * glowAlpha),
        style = Stroke(width = 5.5.dp.toPx(), cap = StrokeCap.Round)
    )

    // Sharp clean vector focal line
    drawPath(
        path = smilePath,
        color = KaiAmber,
        style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawKaiNeck(headCenter: Offset, headRadius: Float) {
    // Smooth pedestal foundation curve transitioning seamlessly down into body plate
    val neckWidth = headRadius * 0.52f
    val neckHeight = headRadius * 0.22f
    val neckTopY = headCenter.y + headRadius * 0.78f

    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFB0B4BC), Color(0xFF63666D))
        ),
        topLeft = Offset(headCenter.x - neckWidth / 2f, neckTopY),
        size = Size(neckWidth, neckHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(neckHeight * 0.4f)
    )
}

private fun DrawScope.drawKaiBody(headCenter: Offset, headRadius: Float, canvasHeight: Float) {
    // Symmetrical, secondary capsule structure maintaining correct scale metrics
    val bodyWidth = headRadius * 0.95f
    val bodyHeight = headRadius * 0.48f
    val bodyTopY = headCenter.y + headRadius * 0.90f

    // Soft rounded geometry capsule
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFE8ECEF), // Soft uniform silver highlights
                Color(0xFF9BA0A6),
                Color(0xFF51545A)  // Base terminator shadow edge
            ),
            center = Offset(headCenter.x, bodyTopY + bodyHeight * 0.2f),
            radius = bodyWidth * 0.8f
        ),
        topLeft = Offset(headCenter.x - bodyWidth / 2f, bodyTopY),
        size = Size(bodyWidth, bodyHeight)
    )

    // Symmetrical shoulder rotation capsule socket points
    val trimRadius = bodyHeight * 0.28f
    val socketBrush = Brush.verticalGradient(colors = listOf(Color(0xFFC2C5CC), Color(0xFF6C6F75)))

    drawCircle(
        brush = socketBrush,
        radius = trimRadius,
        center = Offset(headCenter.x - bodyWidth * 0.43f, bodyTopY + bodyHeight * 0.4f)
    )
    drawCircle(
        brush = socketBrush,
        radius = trimRadius,
        center = Offset(headCenter.x + bodyWidth * 0.43f, bodyTopY + bodyHeight * 0.4f)
    )
}