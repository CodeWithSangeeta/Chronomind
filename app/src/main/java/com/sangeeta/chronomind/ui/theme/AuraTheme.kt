package com.sangeeta.chronomind.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object AuraColors {
    val BackgroundDark     = Color(0xFF0D0D0D)
    val SurfaceCard        = Color(0xFF1A1A1A)
    val SurfaceCardLight   = Color(0xFF222222)
    val SurfaceSelected    = Color(0xFF1F1C0A)

    val YellowPrimary      = Color(0xFFFFCC00)
    val YellowSoft         = Color(0xFFFFD740)
    val YellowGlow         = Color(0x33FFCC00)
    val YellowBorder       = Color(0xFFFFCC00)

    val TextPrimary        = Color(0xFFFFFFFF)
    val TextSecondary      = Color(0xFFAAAAAA)
    val TextMuted          = Color(0xFF666666)
    val TextOnYellow       = Color(0xFF0D0D0D)

    val TimerTrack         = Color(0xFF2A2A2A)
    val TimerGlow          = Color(0x4DFFCC00)

    val Running            = Color(0xFFFFCC00)
    val Paused             = Color(0xFF666666)
    val Ready              = Color(0xFF444444)

    val CardBorderDefault  = Color(0xFF2E2E2E)
    val CardBorderSelected = Color(0xFFFFCC00)
}

val AuraFontFamily = FontFamily.Default

object AuraTypography {
    val DisplayLarge = TextStyle(
        fontFamily = AuraFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.5).sp
    )
    val DisplayMedium = TextStyle(
        fontFamily = AuraFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.3).sp
    )
    val HeadlineMedium = TextStyle(
        fontFamily = AuraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    )
    val TitleMedium = TextStyle(
        fontFamily = AuraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )
    val BodyMedium = TextStyle(
        fontFamily = AuraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    val BodySmall = TextStyle(
        fontFamily = AuraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )
    val LabelMedium = TextStyle(
        fontFamily = AuraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
    val TimerDisplay = TextStyle(
        fontFamily = AuraFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = 1.sp
    )
}