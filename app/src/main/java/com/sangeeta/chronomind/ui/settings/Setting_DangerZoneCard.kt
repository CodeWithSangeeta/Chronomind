package com.sangeeta.chronomind.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun DangerZoneCard(
    onClearDataClick: () -> Unit,
    onResetOnboardingClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF1A0A0A))
            .border(1.dp, Color(0xFFE35D5D).copy(alpha = 0.28f), RoundedCornerShape(22.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "DANGER ZONE",
            style = AuraTypography.LabelMedium,
            color = Color(0xFFE35D5D)
        )

        Text(
            text = "These actions are permanent and cannot be undo",
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary
        )

        DangerButton(
            icon = Icons.Rounded.DeleteForever,
            label = "Clear all activities",
            onClick = onClearDataClick
        )

        DangerButton(
            icon = Icons.Rounded.RestartAlt,
            label = "Reset onboarding",
            onClick = onResetOnboardingClick
        )
    }
}

@Composable
private fun DangerButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF221212))
            .border(1.dp, Color(0xFFE35D5D).copy(alpha = 0.22f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFF311818)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFE35D5D),
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = label,
            style = AuraTypography.TitleMedium,
            color = Color(0xFFFFD2D2),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFE35D5D),
            modifier = Modifier.size(18.dp)
        )
    }
}

