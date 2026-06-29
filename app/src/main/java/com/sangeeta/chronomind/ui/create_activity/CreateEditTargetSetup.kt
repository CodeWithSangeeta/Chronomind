package com.sangeeta.chronomind.ui.create_activity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography


@Composable
fun SegmentedTargetSelector(
    selected: TargetType,
    onSelected: (TargetType) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AuraColors.BackgroundDark)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        TargetType.entries.forEach { type ->
            val isActive = type == selected
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isActive) AuraColors.YellowPrimary
                        else Color.Transparent
                    )
                    .clickable { onSelected(type) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (type == TargetType.TIMER) Icons.Rounded.Timer
                        else Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        tint = if (isActive) AuraColors.BackgroundDark else AuraColors.TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = type.label,
                        style = AuraTypography.TitleMedium,
                        color = if (isActive) AuraColors.BackgroundDark else AuraColors.TextMuted,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun TimeTargetRow(
    hours: Int,
    minutes: Int,
    onHoursChange: (Int) -> Unit,
    onMinutesChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        ScrollValuePickerField(
            label = "Hours",
            items = (0..23).map { "%02d".format(it) },
            selectedValue = "%02d".format(hours),
            onValueSelected = { onHoursChange(it.toInt()) },
            modifier = Modifier.width(80.dp)
        )

        ScrollValuePickerField(
            label = "Minutes",
            items = (0..59).map { "%02d".format(it) },
            selectedValue = "%02d".format(minutes),
            onValueSelected = { onMinutesChange(it.toInt()) },
            modifier = Modifier.width(80.dp)
        )
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollValuePickerField(
    label: String,
    items: List<String>,
    selectedValue: String,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val repeatedItems = remember(items) {
        List(2000) { index -> items[index % items.size] }
    }

    val startIndex = remember(selectedValue, items) {
        val baseIndex = items.indexOf(selectedValue).coerceAtLeast(0)
        val middle = repeatedItems.size / 2
        middle - (middle % items.size) + baseIndex
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val selected = repeatedItems[listState.firstVisibleItemIndex]
            if (selected != selectedValue) {
                onValueSelected(selected)
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextMuted
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(48.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
                .border(
                    1.dp,
                    AuraColors.YellowPrimary.copy(alpha = 0.25f),
                    RoundedCornerShape(12.dp)
                )
        ) {
            LazyColumn(
                state = listState,
                flingBehavior = flingBehavior,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(repeatedItems.size) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = repeatedItems[index],
                            style = AuraTypography.TitleMedium,
                            color = AuraColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AuraColors.YellowPrimary.copy(alpha = 0.08f))
                    .border(
                        1.dp,
                        AuraColors.YellowPrimary.copy(alpha = 0.22f),
                        RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}