package com.sangeeta.chronomind.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Sort
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun ControlRow(
    selectedFilter: HistoryFilter,
    selectedSort: HistorySort,
    onFilterSelected: (HistoryFilter) -> Unit,
    onSortSelected: (HistorySort) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterDropdown(
            modifier = Modifier.weight(1f),
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected,
        )

        SortDropdown(
            modifier = Modifier.weight(1f),
            selectedSort = selectedSort,
            onSortSelected = onSortSelected,
        )
    }
}

@Composable
private fun FilterDropdown(
    modifier: Modifier = Modifier,
    selectedFilter: HistoryFilter,
    onFilterSelected: (HistoryFilter) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AuraColors.SurfaceCard)
                .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.FilterList,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = selectedFilter.label,
                style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                color = AuraColors.TextPrimary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Icon(
                imageVector = Icons.Rounded.ExpandMore,
                contentDescription = null,
                tint = AuraColors.TextSecondary,
                modifier = Modifier.size(18.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = AuraColors.SurfaceCard,
        ) {
            HistoryFilter.entries.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = filter.label,
                            style = AuraTypography.BodyMedium,
                            color = if (filter == selectedFilter) {
                                AuraColors.YellowPrimary
                            } else {
                                AuraColors.TextPrimary
                            }
                        )
                    },
                    onClick = {
                        expanded = false
                        onFilterSelected(filter)
                    }
                )
            }
        }
    }
}

@Composable
private fun SortDropdown(
    modifier: Modifier = Modifier,
    selectedSort: HistorySort,
    onSortSelected: (HistorySort) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AuraColors.SurfaceCard)
                .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Sort,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = selectedSort.label,
                style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                color = AuraColors.TextPrimary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Icon(
                imageVector = Icons.Rounded.ExpandMore,
                contentDescription = null,
                tint = AuraColors.TextSecondary,
                modifier = Modifier.size(18.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = AuraColors.SurfaceCard,
        ) {
            HistorySort.entries.forEach { sort ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = sort.label,
                            style = AuraTypography.BodyMedium,
                            color = if (sort == selectedSort) {
                                AuraColors.YellowPrimary
                            } else {
                                AuraColors.TextPrimary
                            }
                        )
                    },
                    onClick = {
                        expanded = false
                        onSortSelected(sort)
                    }
                )
            }
        }
    }
}