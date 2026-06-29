package com.sangeeta.chronomind.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun QuickActionsSection(
    actions: List<HomeQuickAction>,
    onActionClick: (HomeQuickAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Quick Actions", style = AuraTypography.LabelMedium, color = AuraColors.TextMuted)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            actions.forEach { action ->
                QuickActionCard(action = action, onClick = { onActionClick(action) })
            }
        }
    }
}

@Composable
fun QuickActionCard(action: HomeQuickAction, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(108.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.verticalGradient(listOf(AuraColors.SurfaceCardLight, AuraColors.SurfaceCard)))
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
                .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.22f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(action.icon, contentDescription = action.title, tint = AuraColors.YellowPrimary)
        }
        Text(action.title, style = AuraTypography.BodyMedium, color = AuraColors.TextPrimary)
    }
}


@Composable
 fun EmptyRecentActivities() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "No recent activities yet",
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary
        )
        Text(
            text = "Activities used in the last 7 days or with progress will appear here.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary
        )
    }
}


@Composable
fun PremiumPrimaryButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.horizontalGradient(listOf(AuraColors.SurfaceCard, Color(0xFF1C1707))))
            .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.35f), RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = AuraColors.YellowPrimary)
        Spacer(Modifier.width(10.dp))
        Text(text, style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold), color = AuraColors.YellowPrimary)
    }
}

@Composable
fun PremiumPillButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (isPrimary) Brush.horizontalGradient(listOf(AuraColors.YellowPrimary.copy(alpha = 0.18f), AuraColors.SurfaceCard))
                else Brush.horizontalGradient(listOf(AuraColors.SurfaceCardLight, AuraColors.SurfaceCard))
            )
            .border(1.dp, if (isPrimary) AuraColors.YellowPrimary.copy(alpha = 0.35f) else AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = if (isPrimary) AuraColors.YellowPrimary else AuraColors.TextPrimary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold), color = if (isPrimary) AuraColors.YellowPrimary else AuraColors.TextPrimary)
    }
}