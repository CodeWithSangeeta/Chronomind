package com.sangeeta.chronomind.ui.create_activity


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.model.ActivityIconOption
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.sangeeta.chronomind.ui.model.ActivityColorOption

@Composable
fun CreateEditActivityScreen(
    viewModel: CreateEditViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateHomeAfterStart: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                CreateEditActivityEvent.NavigateBack -> onNavigateBack()
                CreateEditActivityEvent.NavigateHomeAfterStart -> onNavigateHomeAfterStart()
            }
        }
    }

    CreateEditActivityContent(
        uiState = uiState.value,
        onBackClick = onBackClick,
        onSaveClick = viewModel::submit,
        onDeleteClick = { viewModel.deleteActivity() },
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
    onReminderToggle: (Boolean) -> Unit,
    onReminderTimeChange: (String) -> Unit,
    onAdvancedToggle: () -> Unit,
    onStreakBehaviorSelected: (StreakBehavior) -> Unit,
    onCompletionStyleSelected: (CompletionStyle) -> Unit,
    onDeleteConfirmToggle: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {

        var reminderHour by remember { mutableStateOf(7) }
        var reminderMinute by remember { mutableStateOf(30) }
        var reminderAmPm by remember { mutableStateOf("PM") }

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
                                style = AuraTypography.BodySmall,
                                color = AuraColors.TextMuted,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }



            item {
                ReminderSection(
                    isEnabled = uiState.reminderEnabled,
                    selectedHour = reminderHour,
                    selectedMinute = reminderMinute,
                    selectedAmPm = reminderAmPm,
                    onReminderToggle = onReminderToggle,
                    onHourChange = { /* handle later */ },
                    onMinuteChange = { /* handle later */ },
                    onAmPmChange = { /* handle later */ }
                )
            }
            item {
                AdvancedSettingsSection(
                    isExpanded = uiState.isAdvancedExpanded,
                    selectedStreakBehavior = uiState.streakBehavior,
                    selectedCompletionStyle = uiState.completionStyle,
                    onToggle = onAdvancedToggle,
                    onStreakBehaviorSelected = onStreakBehaviorSelected,
                    onCompletionStyleSelected = onCompletionStyleSelected
                )
            }

            item {
                if (uiState.mode != CreateEditMode.EDIT) {
                    SaveButton(
                        label = "Create Activity",
                        enabled = uiState.isValid && !uiState.isSaving,
                        onClick = onSaveClick
                    )
                }
            }

            if (uiState.mode == CreateEditMode.EDIT) {
                item {
                    DeleteSection(onDeleteClick = { onDeleteConfirmToggle(true) })
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    if (uiState.showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { onDeleteConfirmToggle(false) },
            title = { Text("Delete Activity", color = AuraColors.TextPrimary) },
            text = { Text("This will permanently remove the activity from your Activity list. This action cannot be undone.", color = AuraColors.TextSecondary) },
            confirmButton = {
                Text(
                    text = "Delete",
                    color = Color(0xFFE35D5D),
                    modifier = Modifier.clickable { onDeleteConfirmToggle(false);
                        onDeleteClick() }
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

}