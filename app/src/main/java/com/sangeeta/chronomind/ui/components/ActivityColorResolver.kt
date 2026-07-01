package com.sangeeta.chronomind.ui.components

import androidx.compose.ui.graphics.Color
import com.sangeeta.chronomind.ui.theme.AuraColors

object ActivityColorResolver {

    fun tintColor(hex: String): Color {
        val normalized = if (hex.startsWith("#")) hex else "#$hex"
        return runCatching { Color(android.graphics.Color.parseColor(normalized)) }
            .getOrDefault(AuraColors.YellowPrimary)
    }

    fun backgroundColor(hex: String, alpha: Float = 0.12f): Color {
        return tintColor(hex).copy(alpha = alpha)
    }
}