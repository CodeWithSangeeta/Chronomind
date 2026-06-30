package com.sangeeta.chronomind.ui.create_activity


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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography


@Composable
fun AdvancedSettingsSection(
    isExpanded: Boolean,
    selectedStreakBehavior: StreakBehavior,
    selectedCompletionStyle: CompletionStyle,
    onToggle: () -> Unit,
    onStreakBehaviorSelected: (StreakBehavior) -> Unit,
    onCompletionStyleSelected: (CompletionStyle) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AuraColors.SurfaceCard)
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null,
                    tint = AuraColors.TextMuted,
                    modifier = Modifier.size(18.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "ADVANCED SETTINGS",
                        style = AuraTypography.LabelMedium,
                        color = AuraColors.TextMuted
                    )
                    Text(
                        text = "Customize how this activity behaves",
                        style = AuraTypography.BodySmall,
                        color = AuraColors.TextSecondary
                    )
                }
            }

            Icon(
                imageVector = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                contentDescription = null,
                tint = AuraColors.TextMuted
            )
        }

        if (isExpanded) {
            HorizontalDivider(
                color = AuraColors.CardBorderDefault,
                thickness = 1.dp
            )

            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                DropdownSettingRow(
                    label = "Streak on missed day",
                    supportingText = "Choose what happens to your streak if you miss a day.",
                    selectedLabel = selectedStreakBehavior.label,
                    options = listOf(
                        "Continue streak" to {
                            onStreakBehaviorSelected(StreakBehavior.CONTINUE_STREAK)
                        },
                        "Reset to zero" to {
                            onStreakBehaviorSelected(StreakBehavior.RESET_TO_ZERO)
                        }
                    )
                )

                DropdownSettingRow(
                    label = "Completion check",
                    supportingText = "Choose how this activity gets marked as complete.",
                    selectedLabel = selectedCompletionStyle.label,
                    options = listOf(
                        "Manual check" to {
                            onCompletionStyleSelected(CompletionStyle.MANUAL_CHECK)
                        },
                        "Auto check" to {
                            onCompletionStyleSelected(CompletionStyle.AUTO_CHECK)
                        }
                    )
                )
            }
        }
    }
}


@Composable
private fun DropdownSettingRow(
    label: String,
    supportingText: String,
    selectedLabel: String,
    options: List<Pair<String, () -> Unit>>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary
            )
            Text(
                text = supportingText,
                style = AuraTypography.BodySmall,
                color = AuraColors.TextSecondary
            )
        }

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AuraColors.BackgroundDark)
                    .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(12.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedLabel,
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.YellowPrimary
                )

                Icon(
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = null,
                    tint = AuraColors.TextMuted
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = AuraColors.SurfaceCard
            ) {
                options.forEach { (optLabel, action) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = optLabel,
                                color = if (optLabel == selectedLabel) {
                                    AuraColors.YellowPrimary
                                } else {
                                    AuraColors.TextPrimary
                                }
                            )
                        },
                        onClick = {
                            action()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

