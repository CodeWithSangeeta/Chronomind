package com.sangeeta.chronomind.ui.create_activity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.model.ActivityColorOption
import com.sangeeta.chronomind.ui.model.ActivityIconOption
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun PremiumTextField(value: String, placeholder: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = AuraTypography.BodyMedium.copy(color = AuraColors.TextPrimary),
        placeholder = { Text(placeholder, style = AuraTypography.BodyMedium, color = AuraColors.TextMuted) },
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AuraColors.BackgroundDark,
            unfocusedContainerColor = AuraColors.BackgroundDark,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = AuraColors.YellowPrimary,
        )
    )
}

@Composable
fun IconOptionChip(
    option: ActivityIconOption,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) selectedColor.copy(alpha = 0.2f) else AuraColors.BackgroundDark)
            .border(
                width = if (isSelected) 1.2.dp else 1.dp,
                color = if (isSelected) selectedColor else AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = null,
            tint = if (isSelected) selectedColor else AuraColors.TextMuted,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun ColorOptionDot(
    option: ActivityColorOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 38.dp else 32.dp)
            .shadow(
                elevation = if (isSelected) 8.dp else 0.dp,
                shape = CircleShape,
                ambientColor = option.color.copy(alpha = 0.4f),
                spotColor = option.color.copy(alpha = 0.4f)
            )
            .clip(CircleShape)
            .background(option.color)
            .then(
                if (isSelected) Modifier.border(2.5.dp, Color.White.copy(alpha = 0.9f), CircleShape)
                else Modifier
            )
            .clickable(onClick = onClick)
    )
}






@Composable
fun FormSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AuraColors.SurfaceCard)
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = title.uppercase(), style = AuraTypography.LabelMedium, color = AuraColors.TextMuted)
        content()
    }
}


@Composable
fun SaveButton(label: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 14.dp else 0.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = AuraColors.YellowGlow,
                spotColor = AuraColors.YellowGlow
            )
            .clip(RoundedCornerShape(28.dp))
            .background(
                if (enabled) AuraColors.YellowPrimary
                else AuraColors.YellowPrimary.copy(alpha = 0.35f)
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
            color = AuraColors.BackgroundDark
        )
    }
}

@Composable
fun DeleteSection(
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A0A0A))
            .border(
                1.dp,
                Color(0xFFE35D5D).copy(alpha = 0.3f),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "DANGER ZONE",
            style = AuraTypography.LabelMedium,
            color = Color(0xFFE35D5D)
        )

        Text(
            text = "Remove this activity from your library.",
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary
        )

        Row(
            modifier = Modifier
                .height(38.dp)
                .width(175.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE35D5D).copy(alpha = 0.1f))
                .border(
                    1.dp,
                    Color(0xFFE35D5D).copy(alpha = 0.4f),
                    RoundedCornerShape(12.dp)
                )
                .semantics { role = Role.Button }
                .clickable(onClick = onDeleteClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null,
                tint = Color(0xFFE35D5D),
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = "Delete Activity",
                style = AuraTypography.TitleMedium,
                color = Color(0xFFE35D5D)
            )
        }
    }
}

