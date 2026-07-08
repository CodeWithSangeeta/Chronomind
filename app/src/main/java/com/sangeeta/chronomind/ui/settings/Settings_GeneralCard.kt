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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
 fun SettingsToggleRow(
    icon: ImageVector,
    label: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        RowIcon(icon = icon)

        Column(
            modifier = Modifier.weight(1f)
        ) {
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

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AuraColors.BackgroundDark,
                checkedTrackColor = AuraColors.YellowPrimary,
                uncheckedThumbColor = AuraColors.TextMuted,
                uncheckedTrackColor = AuraColors.SurfaceCardLight
            )
        )
    }
}



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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {

            RowIcon(icon =Icons.Rounded.Schedule,)
        Column(
            modifier = Modifier.weight(1f)
        ) {
                Text(
                    text = "Daily reminder",
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )
                Text(
                    text = "Scroll to set a time for your daily reminder",
                    fontSize = 12.sp,
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextMuted
                )
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
            .padding(start =36.dp)
            .width(260.dp)
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



