package com.sangeeta.chronomind.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material.icons.rounded.TrendingUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun SummaryCard(
    uiState: HistoryUiState,
    onRangeSelected: (HistoryRange) -> Unit,
) {
    var rangeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(AuraColors.SurfaceCard)
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Session overview",
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary,
                )
                Text(
                    text = uiState.summary.rangeText,
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextSecondary,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(AuraColors.SurfaceCardLight)
                        .border(
                            1.dp,
                            AuraColors.CardBorderDefault,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { rangeExpanded = true }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = uiState.selectedRange.label,
                        style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                        color = AuraColors.TextPrimary,
                    )
                    Icon(
                        imageVector = Icons.Rounded.ExpandMore,
                        contentDescription = null,
                        tint = AuraColors.TextSecondary,
                        modifier = Modifier.size(16.dp),
                    )
                }

                DropdownMenu(
                    expanded = rangeExpanded,
                    onDismissRequest = { rangeExpanded = false },
                    containerColor = AuraColors.SurfaceCard,
                ) {
                    HistoryRange.entries.forEach { range ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = range.label,
                                    style = AuraTypography.BodyMedium,
                                    color = if (range == uiState.selectedRange) {
                                        AuraColors.YellowPrimary
                                    } else {
                                        AuraColors.TextPrimary
                                    }
                                )
                            },
                            onClick = {
                                rangeExpanded = false
                                onRangeSelected(range)
                            }
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = AuraColors.CardBorderDefault
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SummaryMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.TrendingUp,
                value = uiState.summary.totalSessions.toString(),
                label = "Sessions",
            )
            SummaryMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.TrackChanges,
                value = uiState.summary.totalFocusTime,
                label = "Focus time",
            )
            SummaryMetric(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.CheckCircle,
                value = "${uiState.summary.completionRate}%",
                label = "Completed",
            )
        }
    }
}

@Composable
private fun SummaryMetric(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(AuraColors.SurfaceCardLight)
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(18.dp))
            .padding(horizontal = 10.dp, vertical = 13.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
                .border(
                    1.dp,
                    AuraColors.YellowPrimary.copy(alpha = 0.20f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
            )
        }

        Text(
            text = value,
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.TextPrimary,
            maxLines = 1,
        )
        Text(
            text = label,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary,
            maxLines = 1,
        )
    }
}