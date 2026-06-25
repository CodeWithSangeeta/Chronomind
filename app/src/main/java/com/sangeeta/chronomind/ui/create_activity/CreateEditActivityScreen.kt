package com.sangeeta.chronomind.ui.create_activity


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
        onSaveClick = {
            viewModel.submit()
            onSaved()
        },
        onDeleteClick = {
            viewModel.deleteActivity()
            onDeleted()
        },
        onActivityNameChange = viewModel::updateActivityName,
        onIconSelected = viewModel::selectIcon,
        onColorSelected = viewModel::selectColor,
        onTargetTypeSelected = viewModel::selectTargetType,
        onHoursChange = viewModel::updateTargetHours,
        onMinutesChange = viewModel::updateTargetMinutes,
        onCountChange = viewModel::updateTargetCount,
        onUnitChange = viewModel::updateTargetUnit,
        onReminderToggle = viewModel::toggleReminder,
        onReminderTimeChange = viewModel::setReminderTime,
        onAdvancedToggle = viewModel::toggleAdvancedExpanded,
        onStreakBehaviorSelected = viewModel::selectStreakBehavior,
        onCompletionStyleSelected = viewModel::selectCompletionStyle,
        onDeleteConfirmToggle = viewModel::showDeleteConfirm
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
    onCountChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onReminderToggle: (Boolean) -> Unit,
    onReminderTimeChange: (String) -> Unit,
    onAdvancedToggle: () -> Unit,
    onStreakBehaviorSelected: (StreakBehavior) -> Unit,
    onCompletionStyleSelected: (CompletionStyle) -> Unit,
    onDeleteConfirmToggle: (Boolean) -> Unit
) {
    if (uiState.showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { onDeleteConfirmToggle(false) },
            title = { Text("Delete Activity", color = AuraColors.TextPrimary) },
            text = {
                Text(
                    "This activity will be removed from your library.",
                    color = AuraColors.TextSecondary
                )
            },
            confirmButton = {
                Text(
                    text = "Delete",
                    color = Color(0xFFE35D5D),
                    modifier = Modifier.clickable {
                        onDeleteConfirmToggle(false)
                        onDeleteClick()
                    }
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            CreateEditTopBar(
                title = uiState.screenTitle,
                showSaveAction = uiState.mode == CreateEditMode.EDIT,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick
            )
        }

        item {
            ActivityPreviewCard(uiState = uiState)
        }

        item {
            FormSectionCard(title = "Basics") {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Activity name",
                                style = AuraTypography.TitleMedium,
                                color = AuraColors.TextPrimary
                            )
                            Text(
                                text = "${uiState.activityName.length}/40",
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
                            text = "Choose icon",
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
                            text = "Accent color",
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

                    when (uiState.targetType) {
                        TargetType.TIME -> {
                            TimeTargetRow(
                                hours = uiState.targetHours,
                                minutes = uiState.targetMinutes,
                                onHoursChange = onHoursChange,
                                onMinutesChange = onMinutesChange
                            )
                        }

                        TargetType.COUNT -> {
                            CountTargetFields(
                                count = uiState.targetCount,
                                unit = uiState.targetUnit,
                                onCountChange = onCountChange,
                                onUnitChange = onUnitChange
                            )
                        }
                    }
                }
            }
        }

        item {
            ReminderSection(
                enabled = uiState.reminderEnabled,
                reminderTime = uiState.reminderTime,
                onToggle = onReminderToggle,
                onTimeChange = onReminderTimeChange
            )
        }

        item {
            AdvancedSettingsSection(
                expanded = uiState.advancedExpanded,
                streakBehavior = uiState.streakBehavior,
                completionStyle = uiState.completionStyle,
                onExpandedToggle = onAdvancedToggle,
                onStreakBehaviorSelected = onStreakBehaviorSelected,
                onCompletionStyleSelected = onCompletionStyleSelected
            )
        }

        if (uiState.showDeleteAction) {
            item {
                DangerZone(onDeleteClick = { onDeleteConfirmToggle(true) })
            }
        }

        item {
            PrimaryBottomButton(
                text = uiState.primaryButtonText,
                enabled = uiState.canSubmit,
                onClick = onSaveClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun CreateEditTopBar(
    title: String,
    showSaveAction: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        CircularTopButton(
            icon = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            style = AuraTypography.DisplayMedium,
            color = AuraColors.TextPrimary
        )

        if (showSaveAction) {
            CircularTopButton(
                icon = Icons.Rounded.Check,
                contentDescription = "Save changes",
                onClick = onSaveClick,
                iconTint = AuraColors.YellowPrimary,
                glow = true
            )
        } else {
            Spacer(modifier = Modifier.width(44.dp))
        }
    }
}

@Composable
private fun ActivityPreviewCard(
    uiState: CreateEditUiState
) {
    val accent = uiState.selectedColor.color
    val previewName = uiState.activityName.ifBlank { "Preview" }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF0D0D0D))
                )
            )
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 24.dp, vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .shadow(
                        elevation = 18.dp,
                        shape = CircleShape,
                        ambientColor = accent.copy(alpha = 0.45f),
                        spotColor = accent.copy(alpha = 0.45f)
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.35f),
                                Color(0xFF141414)
                            )
                        )
                    )
                    .border(
                        width = 1.2.dp,
                        color = accent.copy(alpha = 0.75f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.selectedIcon.emoji,
                    style = AuraTypography.DisplayMedium
                )
            }

            Text(
                text = previewName,
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFF111111))
                    .border(
                        width = 1.dp,
                        color = accent.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Preview",
                    style = AuraTypography.BodyMedium,
                    color = accent
                )
            }
        }
    }
}

@Composable
private fun FormSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFF0F0F0F))
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(26.dp)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = AuraTypography.DisplayMedium,
            color = AuraColors.TextPrimary
        )
        content()
    }
}

@Composable
private fun PremiumTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = AuraTypography.BodyMedium.copy(color = AuraColors.TextPrimary),
        placeholder = {
            Text(
                text = placeholder,
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextMuted
            )
        },
        shape = RoundedCornerShape(18.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF121212),
            unfocusedContainerColor = Color(0xFF121212),
            disabledContainerColor = Color(0xFF121212),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = AuraColors.YellowPrimary
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
            .size(54.dp)
            .shadow(
                elevation = if (isSelected) 10.dp else 0.dp,
                shape = CircleShape,
                ambientColor = selectedColor.copy(alpha = 0.30f),
                spotColor = selectedColor.copy(alpha = 0.30f)
            )
            .clip(CircleShape)
            .background(if (isSelected) Color(0xFF17120A) else Color(0xFF1A1A1A))
            .border(
                width = 1.dp,
                color = if (isSelected) selectedColor else AuraColors.CardBorderDefault,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = option.label,
            tint = if (isSelected) selectedColor else AuraColors.TextPrimary
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
            .clip(CircleShape)
            .background(option.color)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color.White.copy(alpha = 0.25f) else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    )
}

@Composable
private fun SegmentedTargetSelector(
    selected: TargetType,
    onSelected: (TargetType) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF151515))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        TargetType.entries.forEach { type ->
            val active = type == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (active) Color(0xFF20180A) else Color.Transparent)
                    .border(
                        width = if (active) 1.dp else 0.dp,
                        color = if (active) AuraColors.YellowPrimary.copy(alpha = 0.30f) else Color.Transparent,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onSelected(type) }
                    .padding(horizontal = 22.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (type == TargetType.TIME) "Time" else "Count",
                    style = AuraTypography.TitleMedium,
                    color = if (active) AuraColors.YellowPrimary else AuraColors.TextPrimary
                )
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
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberSelector(
            label = "Hours",
            value = hours,
            range = (0..12).toList(),
            suffix = "",
            onValueSelected = onHoursChange,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = ":",
            style = AuraTypography.DisplayMedium,
            color = AuraColors.TextPrimary
        )

        NumberSelector(
            label = "Minutes",
            value = minutes,
            range = listOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55),
            suffix = "min",
            onValueSelected = onMinutesChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CountTargetFields(
    count: String,
    unit: String,
    onCountChange: (String) -> Unit,
    onUnitChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PremiumTextField(
            value = count,
            placeholder = "Target count",
            onValueChange = onCountChange
        )
        PremiumTextField(
            value = unit,
            placeholder = "Unit (optional) e.g. pages",
            onValueChange = onUnitChange
        )
    }
}

@Composable
private fun NumberSelector(
    label: String,
    value: Int,
    range: List<Int>,
    suffix: String,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary
        )

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF121212))
                    .border(
                        width = 1.dp,
                        color = AuraColors.CardBorderDefault,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildString {
                        append(value.toString().padStart(2, '0'))
                        if (suffix.isNotBlank()) append(" $suffix")
                    },
                    style = AuraTypography.TitleMedium,
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
                range.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item.toString().padStart(2, '0'),
                                color = AuraColors.TextPrimary
                            )
                        },
                        onClick = {
                            expanded = false
                            onValueSelected(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderSection(
    enabled: Boolean,
    reminderTime: String,
    onToggle: (Boolean) -> Unit,
    onTimeChange: (String) -> Unit
) {
    val timeOptions = listOf("06:00 AM", "07:00 AM", "07:00 PM", "08:00 PM", "09:00 PM")

    FormSectionCard(title = "") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF18120A))
                        .border(
                            width = 1.dp,
                            color = AuraColors.YellowPrimary.copy(alpha = 0.25f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = null,
                        tint = AuraColors.YellowPrimary
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reminder",
                        style = AuraTypography.TitleMedium,
                        color = AuraColors.TextPrimary
                    )
                    Text(
                        text = "Get reminded to stay consistent",
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary
                    )
                }

                Switch(
                    checked = enabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AuraColors.YellowPrimary,
                        uncheckedThumbColor = AuraColors.TextMuted,
                        uncheckedTrackColor = Color(0xFF2B2B2B)
                    )
                )
            }

            if (enabled) {
                ReminderTimeSelector(
                    time = reminderTime,
                    options = timeOptions,
                    onTimeSelected = onTimeChange
                )
            }
        }
    }
}

@Composable
private fun ReminderTimeSelector(
    time: String,
    options: List<String>,
    onTimeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Reminder time",
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary
        )

        Box {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF121212))
                    .border(
                        width = 1.dp,
                        color = AuraColors.CardBorderDefault,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    tint = AuraColors.TextSecondary
                )
                Text(
                    text = time,
                    style = AuraTypography.TitleMedium,
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
                        text = { Text(option, color = AuraColors.TextPrimary) },
                        onClick = {
                            expanded = false
                            onTimeSelected(option)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdvancedSettingsSection(
    expanded: Boolean,
    streakBehavior: StreakBehavior,
    completionStyle: CompletionStyle,
    onExpandedToggle: () -> Unit,
    onStreakBehaviorSelected: (StreakBehavior) -> Unit,
    onCompletionStyleSelected: (CompletionStyle) -> Unit
) {
    FormSectionCard(title = "") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onExpandedToggle),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF18120A))
                        .border(
                            width = 1.dp,
                            color = AuraColors.YellowPrimary.copy(alpha = 0.25f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = null,
                        tint = AuraColors.YellowPrimary
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Advanced Settings",
                        style = AuraTypography.TitleMedium,
                        color = AuraColors.TextPrimary
                    )
                    Text(
                        text = "Customize how this activity behaves",
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = null,
                    tint = AuraColors.TextSecondary
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF111111))
                        .border(
                            width = 1.dp,
                            color = AuraColors.CardBorderDefault,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    AdvancedSettingRow(
                        title = "Streak on missed day",
                        subtitle = "What happens to your streak if you miss a day?",
                        selectedValue = streakBehavior.label,
                        options = StreakBehavior.entries.map { it.label },
                        onOptionSelected = { selected ->
                            onStreakBehaviorSelected(
                                StreakBehavior.entries.first { it.label == selected }
                            )
                        }
                    )

                    AdvancedSettingRow(
                        title = "Completion / Mark style",
                        subtitle = "How you mark this activity complete",
                        selectedValue = completionStyle.label,
                        options = CompletionStyle.entries.map { it.label },
                        onOptionSelected = { selected ->
                            onCompletionStyleSelected(
                                CompletionStyle.entries.first { it.label == selected }
                            )
                        }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF181818))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "These settings apply everywhere — app, widget, history & insights. Activity rules stay consistent across the app.",
                            style = AuraTypography.BodyMedium,
                            color = AuraColors.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdvancedSettingRow(
    title: String,
    subtitle: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary
        )
        Text(
            text = subtitle,
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary
        )

        Box {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF121212))
                    .border(
                        width = 1.dp,
                        color = AuraColors.CardBorderDefault,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedValue,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.YellowPrimary
                )
                Spacer(modifier = Modifier.width(20.dp))
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
                        text = { Text(option, color = AuraColors.TextPrimary) },
                        onClick = {
                            expanded = false
                            onOptionSelected(option)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DangerZone(
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF120E0E))
            .border(
                width = 1.dp,
                color = Color(0xFF442626),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Danger Zone",
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary
        )
        Text(
            text = "Delete this activity permanently from your library.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF1B1111))
                .border(
                    width = 1.dp,
                    color = Color(0xFF6E2D2D),
                    shape = RoundedCornerShape(18.dp)
                )
                .clickable(onClick = onDeleteClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete Activity",
                tint = Color(0xFFE35D5D)
            )
            Text(
                text = "Delete Activity",
                style = AuraTypography.TitleMedium,
                color = Color(0xFFE35D5D)
            )
        }
    }
}

@Composable
private fun PrimaryBottomButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (enabled) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF7D54D),
                            Color(0xFFF2BA2E)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF5E5330),
                            Color(0xFF665936)
                        )
                    )
                }
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xFF151515)
        )
    }
}

@Composable
private fun CircularTopButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    iconTint: Color = AuraColors.TextPrimary,
    glow: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .shadow(
                elevation = if (glow) 12.dp else 0.dp,
                shape = CircleShape,
                ambientColor = AuraColors.YellowGlow,
                spotColor = AuraColors.YellowGlow
            )
            .clip(CircleShape)
            .background(Color(0xFF111111))
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint
        )
    }
}