package com.sangeeta.chronomind.ui.create_activity

import android.R.attr.iconTint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun CreateEditActivityScreen(
    viewModel: CreateEditViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    onDeleted: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateEditActivityContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSaveClick = { viewModel.submit(onSaved) },
        onDeleteClick = { viewModel.deleteActivity(onDeleted) },
        onActivityNameChange = viewModel::updateActivityName,
        onIconSelected = viewModel::selectIcon,
        onColorSelected = viewModel::selectColor,
        onTargetTypeSelected = viewModel::selectTargetType,
        onHoursChange = viewModel::updateTargetHours,
        onMinutesChange = viewModel::updateTargetMinutes,
        onReminderToggle = viewModel::toggleReminder,
        onReminderTimeChange = viewModel::setReminderTime,
        onAdvancedToggle = viewModel::toggleAdvancedExpanded,
        onStreakBehaviorSelected = viewModel::selectStreakBehavior,
        onCompletionStyleSelected = viewModel::selectCompletionStyle,
        onDeleteConfirmToggle = viewModel::showDeleteConfirm,
    )
}

@Composable
private fun CreateEditActivityContent(
    uiState: CreateEditUiState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onActivityNameChange: (String) -> Unit,
    onIconSelected: (ActivityIconOption) -> Unit,
    onColorSelected: (ActivityColorOption) -> Unit,
    onTargetTypeSelected: (TargetType) -> Unit,
    onHoursChange: (Int) -> Unit,
    onMinutesChange: (Int) -> Unit,
    onReminderToggle: (Boolean) -> Unit,
    onReminderTimeChange: (String) -> Unit,
    onAdvancedToggle: () -> Unit,
    onStreakBehaviorSelected: (StreakBehavior) -> Unit,
    onCompletionStyleSelected: (CompletionStyle) -> Unit,
    onDeleteConfirmToggle: (Boolean) -> Unit,
) {
    if (uiState.showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { onDeleteConfirmToggle(false) },
            title = { Text("Delete Activity", color = AuraColors.TextPrimary) },
            text = { Text("This activity will be removed from your library.", color = AuraColors.TextSecondary) },
            confirmButton = {
                Text(
                    text = "Delete",
                    color = Color(0xFFE35D5D),
                    modifier = Modifier.clickable { onDeleteConfirmToggle(false); onDeleteClick() }
                )
            },
            dismissButton = {
                Text(
                    text = "Cancel",
                    color = AuraColors.TextPrimary,
                    modifier = Modifier.clickable { onDeleteConfirmToggle(false) }
                )
            },
            containerColor = AuraColors.SurfaceCard
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {

        CreateEditTopBar(
            title = uiState.screenTitle,
            showSaveAction = uiState.mode == CreateEditMode.EDIT,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item { ActivityPreviewCard(uiState = uiState) }

            item {
                FormSectionCard(title = "Basics") {
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Activity name",
                                    style = AuraTypography.TitleMedium,
                                    color = AuraColors.TextPrimary
                                )
                                Text(
                                    "${uiState.activityName.length}/40",
                                    style = AuraTypography.BodySmall,
                                    color = AuraColors.TextMuted
                                )
                            }
                            PremiumTextField(
                                value = uiState.activityName,
                                placeholder = "e.g. Creative Projects",
                                onValueChange = onActivityNameChange
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                "Choose icon",
                                style = AuraTypography.TitleMedium,
                                color = AuraColors.TextPrimary
                            )
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ActivityIconOption.entries.forEach { iconOption ->
                                    IconOptionChip(
                                        option = iconOption,
                                        isSelected = iconOption == uiState.selectedIcon,
                                        selectedColor = uiState.selectedColor.color,
                                        onClick = { onIconSelected(iconOption) }
                                    )
                                }
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                "Accent color",
                                style = AuraTypography.TitleMedium,
                                color = AuraColors.TextPrimary
                            )
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                ActivityColorOption.entries.forEach { colorOption ->
                                    ColorOptionDot(
                                        option = colorOption,
                                        isSelected = colorOption == uiState.selectedColor,
                                        onClick = { onColorSelected(colorOption) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                FormSectionCard(title = "Target") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SegmentedTargetSelector(
                            selected = uiState.targetType,
                            onSelected = onTargetTypeSelected
                        )
                        if (uiState.targetType == TargetType.TIMER) {
                            TimeTargetRow(
                                hours = uiState.targetHours,
                                minutes = uiState.targetMinutes,
                                onHoursChange = onHoursChange,
                                onMinutesChange = onMinutesChange
                            )
                        } else {
                            Text(
                                text = "No target — timer counts up until you tap Finish.",
                                style = AuraTypography.BodyMedium,
                                color = AuraColors.TextMuted,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            item {
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
                                Text(
                                    "Daily reminder",
                                    style = AuraTypography.TitleMedium,
                                    color = AuraColors.TextPrimary
                                )
                            }
                            Switch(
                                checked = uiState.reminderEnabled,
                                onCheckedChange = onReminderToggle,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = AuraColors.BackgroundDark,
                                    checkedTrackColor = AuraColors.YellowPrimary,
                                    uncheckedThumbColor = AuraColors.TextMuted,
                                    uncheckedTrackColor = AuraColors.SurfaceCard,
                                )
                            )
                        }
                        if (uiState.reminderEnabled) {
                            ReminderTimePicker(
                                currentTime = uiState.reminderTime,
                                onTimeChange = onReminderTimeChange
                            )
                        }
                    }
                }
            }

            item {
                AdvancedSettingsSection(
                    uiState = uiState,
                    onToggle = onAdvancedToggle,
                    onStreakBehaviorSelected = onStreakBehaviorSelected,
                    onCompletionStyleSelected = onCompletionStyleSelected,
                )
            }

            item {
                SaveButton(
                    label = if (uiState.mode == CreateEditMode.CREATE) "Create Activity" else "Save Changes",
                    enabled = uiState.isValid && !uiState.isSaving,
                    onClick = onSaveClick
                )
            }

            if (uiState.mode == CreateEditMode.EDIT) {
                item {
                    DeleteSection(onDeleteClick = { onDeleteConfirmToggle(true) })
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun CreateEditTopBar(
    title: String,
    showSaveAction: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(AuraColors.SurfaceCard)
                .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = AuraColors.TextPrimary
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = AuraTypography.DisplayMedium, color = AuraColors.TextPrimary)
        }
        if (showSaveAction) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AuraColors.YellowPrimary)
                    .clickable(onClick = onSaveClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Save",
                    tint = AuraColors.BackgroundDark
                )
            }
        }
    }
}

@Composable
private fun ActivityPreviewCard(uiState: CreateEditUiState) {
    val accentColor = uiState.selectedColor.color
    val durationText = when {
        uiState.targetType == TargetType.STOPWATCH -> "Stopwatch"
        uiState.targetHours > 0 && uiState.targetMinutes > 0 ->
            "${uiState.targetHours}h ${uiState.targetMinutes}m"
        uiState.targetHours > 0 -> "${uiState.targetHours}h"
        uiState.targetMinutes > 0 -> "${uiState.targetMinutes}m"
        else -> "Set target"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.12f),
                        AuraColors.SurfaceCard
                    )
                )
            )
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = uiState.selectedIcon.icon,
                contentDescription = null,
               // tint = if (isSelected) selectedColor else AuraColors.TextMuted,
                modifier = Modifier.size(32.dp)
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = if (uiState.activityName.isBlank()) "Activity name" else uiState.activityName,
                style = AuraTypography.TitleMedium,
                color = if (uiState.activityName.isBlank()) AuraColors.TextMuted else AuraColors.TextPrimary,
                maxLines = 1
            )
            Text(
                text = durationText,
                style = AuraTypography.BodyMedium,
                color = accentColor
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(accentColor.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = uiState.targetType.label.uppercase(),
                style = AuraTypography.LabelMedium,
                color = accentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun FormSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
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
private fun SegmentedTargetSelector(
    selected: TargetType,
    onSelected: (TargetType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AuraColors.BackgroundDark)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TargetType.entries.forEach { type ->
            val isActive = type == selected
            Box(
                modifier = Modifier
                    .weight(1f)
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
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
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
private fun TimeTargetRow(
    hours: Int,
    minutes: Int,
    onHoursChange: (Int) -> Unit,
    onMinutesChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NumberPickerField(
            label = "Hours",
            value = hours,
            range = 0..23,
            onValueChange = onHoursChange,
            modifier = Modifier.weight(1f)
        )
        NumberPickerField(
            label = "Minutes",
            value = minutes,
            range = 0..59,
            step = 5,
            onValueChange = onMinutesChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NumberPickerField(
    label: String,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    step: Int = 1
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = AuraTypography.BodySmall, color = AuraColors.TextMuted)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AuraColors.BackgroundDark)
                .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onValueChange((value - step).coerceAtLeast(range.first)) },
                contentAlignment = Alignment.Center
            ) {
                Text("−", style = AuraTypography.TitleMedium, color = AuraColors.YellowPrimary)
            }
            Text(
                text = "%02d".format(value),
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onValueChange((value + step).coerceAtMost(range.last)) },
                contentAlignment = Alignment.Center
            ) {
                Text("+", style = AuraTypography.TitleMedium, color = AuraColors.YellowPrimary)
            }
        }
    }
}

@Composable
private fun ReminderTimePicker(currentTime: String, onTimeChange: (String) -> Unit) {
    val times = listOf(
        "06:00 AM", "07:00 AM", "08:00 AM", "09:00 AM", "10:00 AM",
        "12:00 PM", "02:00 PM", "04:00 PM", "06:00 PM", "07:00 PM",
        "08:00 PM", "09:00 PM", "10:00 PM"
    )
    var expanded by remember { mutableStateOf(false) }

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
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Schedule, contentDescription = null, tint = AuraColors.YellowPrimary, modifier = Modifier.size(18.dp))
                Text(currentTime, style = AuraTypography.TitleMedium, color = AuraColors.TextPrimary)
            }
            Icon(Icons.Rounded.ExpandMore, contentDescription = null, tint = AuraColors.TextMuted)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = AuraColors.SurfaceCard
        ) {
            times.forEach { t ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = t,
                            color = if (t == currentTime) AuraColors.YellowPrimary else AuraColors.TextPrimary
                        )
                    },
                    onClick = { onTimeChange(t); expanded = false }
                )
            }
        }
    }
}

@Composable
private fun AdvancedSettingsSection(
    uiState: CreateEditUiState,
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
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Settings, contentDescription = null, tint = AuraColors.TextMuted, modifier = Modifier.size(18.dp))
                Text("ADVANCED", style = AuraTypography.LabelMedium, color = AuraColors.TextMuted)
            }
            Icon(
                imageVector = if (uiState.isAdvancedExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                contentDescription = null,
                tint = AuraColors.TextMuted
            )
        }

        if (uiState.isAdvancedExpanded) {
            HorizontalDivider(color = AuraColors.CardBorderDefault, thickness = 1.dp)
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                DropdownSettingRow(
                    label = "Streak on missed day",
                    selectedLabel = uiState.streakBehavior.label,
                    options = StreakBehavior.entries.map { it.label to { onStreakBehaviorSelected(it) } }
                )
                if (uiState.targetType == TargetType.TIMER) {
                    DropdownSettingRow(
                        label = "Completion style",
                        selectedLabel = uiState.completionStyle.label,
                        options = CompletionStyle.entries.map { it.label to { onCompletionStyleSelected(it) } }
                    )
                }
            }
        }
    }
}

@Composable
private fun DropdownSettingRow(
    label: String,
    selectedLabel: String,
    options: List<Pair<String, () -> Unit>>
) {
    var expanded by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = AuraTypography.TitleMedium, color = AuraColors.TextPrimary)
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
                Text(selectedLabel, style = AuraTypography.BodyMedium, color = AuraColors.TextPrimary)
                Icon(Icons.Rounded.ExpandMore, contentDescription = null, tint = AuraColors.TextMuted)
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
                                color = if (optLabel == selectedLabel) AuraColors.YellowPrimary
                                else AuraColors.TextPrimary
                            )
                        },
                        onClick = { action(); expanded = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun SaveButton(label: String, enabled: Boolean, onClick: () -> Unit) {
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
private fun DeleteSection(onDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A0A0A))
            .border(1.dp, Color(0xFFE35D5D).copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("DANGER ZONE", style = AuraTypography.LabelMedium, color = Color(0xFFE35D5D))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE35D5D).copy(alpha = 0.1f))
                .border(1.dp, Color(0xFFE35D5D).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .clickable(onClick = onDeleteClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = Color(0xFFE35D5D), modifier = Modifier.size(20.dp))
            Text("Delete Activity", style = AuraTypography.TitleMedium, color = Color(0xFFE35D5D))
        }
    }
}

@Composable
private fun PremiumTextField(value: String, placeholder: String, onValueChange: (String) -> Unit) {
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
private fun IconOptionChip(
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
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun ColorOptionDot(
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
