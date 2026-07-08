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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun <T> SettingsDropdownRow(
    icon: ImageVector,
    label: String,
    subtitle: String,
    selected: T,
    options: List<T>,
    labelFor: (T) -> String,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val iconSize = 40.dp
    val rowGap = 14.dp

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(rowGap)
        ) {
            RowIcon(icon = icon)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextMuted
                )
            }
        }

        Box(
            modifier = Modifier.padding(start = iconSize + rowGap)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AuraColors.BackgroundDark)
                    .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(14.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = labelFor(selected),
                    style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = AuraColors.TextPrimary
                )

                Icon(
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = null,
                    tint = AuraColors.TextSecondary
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = AuraColors.SurfaceCard
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = labelFor(option),
                                style = AuraTypography.BodyMedium,
                                color = if (option == selected) {
                                    AuraColors.YellowPrimary
                                } else {
                                    AuraColors.TextPrimary
                                }
                            )
                        },
                        onClick = {
                            expanded = false
                            onSelected(option)
                        }
                    )
                }
            }
        }
    }
}