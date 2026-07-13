package com.sangeeta.chronomind.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun OpenSourceLicensesScreen(
    onBackClick: () -> Unit
) {
    val chronoLicenseColors = darkColorScheme(
        primary = AuraColors.YellowPrimary,
        onPrimary = AuraColors.BackgroundDark,
        background = AuraColors.BackgroundDark,
        onBackground = AuraColors.TextPrimary,
        surface = AuraColors.BackgroundDark,
        onSurface = AuraColors.TextPrimary,
        surfaceVariant = AuraColors.SurfaceCard,
        onSurfaceVariant = AuraColors.TextSecondary
    )

    val chronoLicenseTypography = Typography(
        titleLarge = TextStyle(
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = AuraColors.TextPrimary
        ),
        titleMedium = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            color = AuraColors.TextPrimary
        ),
        bodyMedium = TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Normal,
            color = AuraColors.TextSecondary
        ),
        labelMedium = TextStyle(
            fontSize = 11.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Medium,
            color = AuraColors.BackgroundDark
        ),
        labelSmall = TextStyle(
            fontSize = 11.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Normal,
            color = AuraColors.TextMuted
        )
    )

    MaterialTheme(
        colorScheme = chronoLicenseColors,
        typography = chronoLicenseTypography
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AuraColors.BackgroundDark)
                .statusBarsPadding()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(AuraColors.SurfaceCard)
                        .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                        .clickable(onClick = onBackClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = AuraColors.TextPrimary
                    )
                }

                Text(
                    text = "Open Source Licenses",
                    style = AuraTypography.DisplayMedium,
                    color = AuraColors.TextPrimary
                )
            }

            Text(
                text = "ChronoMind uses open-source libraries. Their license notices are listed below.",
                style = AuraTypography.BodySmall,
                color = AuraColors.TextMuted,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            LibrariesContainer(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}