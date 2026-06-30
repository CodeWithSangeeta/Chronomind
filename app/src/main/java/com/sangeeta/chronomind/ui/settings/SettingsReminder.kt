package com.sangeeta.chronomind.ui.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.ui.create_activity.FormSectionCard
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun SettingsReminderSection(
    isEnabled: Boolean,
    selectedHour: Int,
    selectedMinute: Int,
    selectedAmPm: String,
    onReminderToggle: (Boolean) -> Unit,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onAmPmChange: (String) -> Unit
) {
    FormSectionCard(title = "Reminder") {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = null,
                        tint = AuraColors.YellowPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Daily reminder",
                            style = AuraTypography.TitleMedium,
                            color = AuraColors.TextPrimary
                        )
                        Text(
                            text = "Scroll to set the reminder time",
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextSecondary
                        )
                    }
                }

                Switch(
                    checked = isEnabled,
                    onCheckedChange = onReminderToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AuraColors.BackgroundDark,
                        checkedTrackColor = AuraColors.YellowPrimary,
                        uncheckedThumbColor = AuraColors.TextMuted,
                        uncheckedTrackColor = AuraColors.SurfaceCard
                    )
                )
            }

            if (isEnabled) {
                CompactScrollTimePicker(
                    selectedHour = selectedHour,
                    selectedMinute = selectedMinute,
                    selectedAmPm = selectedAmPm,
                    onHourChange = onHourChange,
                    onMinuteChange = onMinuteChange,
                    onAmPmChange = onAmPmChange
                )
            }
        }
    }
}


@Composable
fun CompactScrollTimePicker(
    selectedHour: Int,
    selectedMinute: Int,
    selectedAmPm: String,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onAmPmChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AuraColors.BackgroundDark)
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleValueScrollPicker(
            items = (1..12).map { "%02d".format(it) },
            startIndex = (selectedHour - 1).coerceAtLeast(0),
            onItemSelected = { onHourChange(it.toInt()) },
            modifier = Modifier.width(64.dp)
        )

        Text(
            text = ":",
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp)
        )

        SingleValueScrollPicker(
            items = (0..59).map { "%02d".format(it) },
            startIndex = selectedMinute.coerceIn(0, 59),
            onItemSelected = { onMinuteChange(it.toInt()) },
            modifier = Modifier.width(64.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        SingleValueScrollPicker(
            items = listOf("AM", "PM"),
            startIndex = if (selectedAmPm == "PM") 1 else 0,
            onItemSelected = onAmPmChange,
            modifier = Modifier.width(72.dp)
        )
    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleValueScrollPicker(
    items: List<String>,
    startIndex: Int,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemHeight = 40.dp
    val repeatedItems = remember(items) {
        List(2000) { index -> items[index % items.size] }
    }

    val initialIndex = remember(startIndex, items) {
        val middle = repeatedItems.size / 2
        middle - (middle % items.size) + startIndex.coerceIn(0, items.lastIndex)
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState.isScrollInProgress, listState.firstVisibleItemIndex) {
        if (!listState.isScrollInProgress) {
            val selectedItem = repeatedItems[listState.firstVisibleItemIndex]
            onItemSelected(selectedItem)
        }
    }

    Box(
        modifier = modifier
            .height(itemHeight)
            .clip(RoundedCornerShape(10.dp))
            .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
            .border(
                1.dp,
                AuraColors.YellowPrimary.copy(alpha = 0.25f),
                RoundedCornerShape(10.dp)
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
                        .height(itemHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = repeatedItems[index],
                        style = AuraTypography.TitleMedium,
                        color = AuraColors.YellowPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}