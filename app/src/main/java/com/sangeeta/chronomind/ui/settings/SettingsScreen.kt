package com.sangeeta.chronomind.ui.settings

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.ui.create_activity.CompletionStyle
import com.sangeeta.chronomind.ui.create_activity.StreakBehavior
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onRowClick: (String) -> Unit,
    onResetOnboarding: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onNotificationPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SettingsEvent.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.onNotificationPermissionResult(true)
                    }
                }
            }
        }
    }


    SettingsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRowClick = onRowClick,
        onNotificationsToggle = viewModel::onNotificationsToggle,
        onDailyReminderChanged = viewModel::onDailyReminderToggle,
        onReminderHourChange = viewModel::onReminderHourChange,
        onReminderMinuteChange = viewModel::onReminderMinuteChange,
        onReminderAmPmChange = viewModel::onReminderAmPmChange,
        onDefaultCompletionStyleSelected = viewModel::setDefaultCompletionStyle,
        onDefaultStreakOnMissSelected = viewModel::setDefaultStreakOnMiss,
        onShowClearDataConfirm = viewModel::showClearDataConfirm,
        onShowResetConfirm = viewModel::showResetConfirm,
        onConfirmClearData = viewModel::clearAllData,
        onConfirmReset = { viewModel.resetOnboarding(onResetOnboarding) }
    )
}



@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onRowClick: (String) -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    onDailyReminderChanged: (Boolean) -> Unit,
    onReminderHourChange: (Int) -> Unit,
    onReminderMinuteChange: (Int) -> Unit,
    onReminderAmPmChange: (String) -> Unit,
    onDefaultCompletionStyleSelected: (CompletionStyle) -> Unit,
    onDefaultStreakOnMissSelected: (StreakBehavior) -> Unit,
    onShowClearDataConfirm: (Boolean) -> Unit,
    onShowResetConfirm: (Boolean) -> Unit,
    onConfirmClearData: () -> Unit,
    onConfirmReset: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        SettingsTopBar(
            onBackClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                SettingsSectionCard(title = "GENERAL") {
                    SettingsToggleRow(
                        icon = Icons.Rounded.Notifications,
                        label = "Notifications",
                        subtitle = "Show the active timer in a notification",
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = onNotificationsToggle
                    )

                    SettingsDivider()

                    SettingsReminderSection(
                        isEnabled = uiState.isDailyReminderEnabled,
                        selectedHour = uiState.reminderHour,
                        selectedMinute = uiState.reminderMinute,
                        selectedAmPm = uiState.reminderAmPm,
                        onReminderToggle = onDailyReminderChanged,
                        onHourChange = onReminderHourChange,
                        onMinuteChange = onReminderMinuteChange,
                        onAmPmChange = onReminderAmPmChange
                    )
                }
            }

            item {
                SettingsSectionCard(
                    title = "FOCUS",
                    footerText = "Defaults apply to new activities. You can override them per activity."                ) {
                    SettingsDropdownRow(
                        icon = Icons.Rounded.RestartAlt,
                        label = "Streak on missed day",
                        subtitle = "What new activities do after a missed day",
                        selected = uiState.defaultStreakOnMiss,
                        options = listOf(
                            StreakBehavior.CONTINUE_STREAK,
                            StreakBehavior.RESET_TO_ZERO
                        ),
                        labelFor = { it.label },
                        onSelected = onDefaultStreakOnMissSelected
                    )
                    SettingsDivider()

                    SettingsDropdownRow(
                        icon = Icons.Rounded.CheckCircle,
                        label = "Completion check",
                        subtitle = "How new activities are marked complete",
                        selected = uiState.defaultCompletionStyle,
                        options = listOf(
                            CompletionStyle.MANUAL_CHECK,
                            CompletionStyle.AUTO_CHECK
                        ),
                        labelFor = { it.label },
                        onSelected = onDefaultCompletionStyleSelected
                    )
                }
            }


            item {
                var isFaqExpanded by remember { mutableStateOf(false) }

                var showAns1 by remember { mutableStateOf(false) }
                var showAns2 by remember { mutableStateOf(false) }
                var showAns3 by remember { mutableStateOf(false) }
                var showAns4 by remember { mutableStateOf(false) }
                val context = androidx.compose.ui.platform.LocalContext.current

                SettingsSectionCard(title = "HELP") {
                    uiState.helpItems.forEachIndexed { index, item ->
                        if (item.id == "faq") {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { isFaqExpanded = !isFaqExpanded }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    RowIcon(icon = item.icon)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = item.title, style = AuraTypography.TitleMedium, color = AuraColors.TextPrimary)
                                        Text(text = item.subtitle, fontSize = 12.sp, style = AuraTypography.BodyMedium, color = AuraColors.TextMuted)
                                    }
                                    Icon(
                                        imageVector = if (isFaqExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                        contentDescription = null,
                                        tint = AuraColors.TextSecondary
                                    )
                                }

                                if (isFaqExpanded) {
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 54.dp, top = 8.dp, bottom = 8.dp)
                                            .fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {

                                            Column(modifier = Modifier.clickable { showAns1 = !showAns1 }) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = context.getString(R.string.faq_q1), fontWeight = FontWeight.SemiBold, color = AuraColors.YellowPrimary, style = AuraTypography.BodyMedium, modifier = Modifier.weight(1f))
                                                    Icon(imageVector = if (showAns1) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown, contentDescription = null, tint = AuraColors.TextSecondary, modifier = Modifier.size(16.dp))
                                                }
                                                if (showAns1) Text(text = context.getString(R.string.faq_a1), color = AuraColors.TextSecondary, style = AuraTypography.BodySmall, modifier = Modifier.padding(top = 4.dp))
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Column(modifier = Modifier.clickable { showAns2 = !showAns2 }) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = context.getString(R.string.faq_q2), fontWeight = FontWeight.SemiBold, color = AuraColors.YellowPrimary, style = AuraTypography.BodyMedium, modifier = Modifier.weight(1f))
                                                    Icon(imageVector = if (showAns2) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown, contentDescription = null, tint = AuraColors.TextSecondary, modifier = Modifier.size(16.dp))
                                                }
                                                if (showAns2) Text(text = context.getString(R.string.faq_a2), color = AuraColors.TextSecondary, style = AuraTypography.BodySmall, modifier = Modifier.padding(top = 4.dp))
                                            }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Column(modifier = Modifier.clickable { showAns3 = !showAns3 }) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = context.getString(R.string.faq_q3), fontWeight = FontWeight.SemiBold, color = AuraColors.YellowPrimary, style = AuraTypography.BodyMedium, modifier = Modifier.weight(1f))
                                                    Icon(imageVector = if (showAns3) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown, contentDescription = null, tint = AuraColors.TextSecondary, modifier = Modifier.size(16.dp))
                                                }
                                                if (showAns3) Text(text = context.getString(R.string.faq_a3), color = AuraColors.TextSecondary, style = AuraTypography.BodySmall, modifier = Modifier.padding(top = 4.dp))
                                            }

                                        Spacer(modifier = Modifier.height(4.dp))

                                            Column(modifier = Modifier.clickable { showAns4 = !showAns4 }) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = context.getString(R.string.faq_q4), fontWeight = FontWeight.SemiBold, color = AuraColors.YellowPrimary, style = AuraTypography.BodyMedium, modifier = Modifier.weight(1f))
                                                    Icon(imageVector = if (showAns4) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown, contentDescription = null, tint = AuraColors.TextSecondary, modifier = Modifier.size(16.dp))
                                                }
                                                if (showAns4) Text(text = context.getString(R.string.faq_a4), color = AuraColors.TextSecondary, style = AuraTypography.BodySmall, modifier = Modifier.padding(top = 4.dp))
                                        }

                                    }
                                }
                            }
                        } else {
                            SettingsRow(
                                item = item,
                                onClick = { onRowClick(item.id) }
                            )
                        }

                        if (index != uiState.helpItems.lastIndex) {
                            SettingsDivider()
                        }
                    }
                }
            }

            item {
                SettingsSectionCard(title = "PLAY STORE TRUST") {
                    uiState.trustItems.forEachIndexed { index, item ->
                        SettingsRow(
                            item = item,
                            onClick = { onRowClick(item.id) }
                        )
                        if (index != uiState.trustItems.lastIndex) {
                            SettingsDivider()
                        }
                    }
                }
            }

            item {
                SettingsSectionCard(title = "ABOUT") {
                    uiState.aboutItems.forEachIndexed { index, item ->
                        SettingsRow(
                            item = item,
                            onClick = { onRowClick(item.id) }
                        )
                        if (index != uiState.aboutItems.lastIndex) {
                            SettingsDivider()
                        }
                    }
                }
            }

            item {
                DangerZoneCard(
                    onClearDataClick = { onShowClearDataConfirm(true) },
                    onResetOnboardingClick = { onShowResetConfirm(true) }
                )
            }
        }

        if (uiState.showClearDataConfirm) {
            ConfirmDialog(
                title = "Clear all activities?",
                body = "This will permanently delete all your activities and session history.",
                confirmText = "Yes, clear all",
                onConfirm = onConfirmClearData,
                onDismiss = { onShowClearDataConfirm(false) }
            )
        }

        if (uiState.showResetConfirm) {
            ConfirmDialog(
                title = "Reset onboarding?",
                body = "You will go through the onboarding flow again. Your activities are not affected.",
                confirmText = "Yes, reset",
                onConfirm = onConfirmReset,
                onDismiss = { onShowResetConfirm(false) }
            )
        }
    }
}