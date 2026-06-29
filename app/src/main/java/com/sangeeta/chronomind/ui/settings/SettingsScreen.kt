//package com.sangeeta.chronomind.ui.settings
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.navigationBars
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.windowInsetsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.ArrowBack
//import androidx.compose.material.icons.rounded.ChevronRight
//import androidx.compose.material.icons.rounded.OpenInNew
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.automirrored.rounded.Help
//import androidx.compose.material.icons.rounded.Help
//import androidx.compose.material.icons.rounded.Info
//import androidx.compose.material.icons.rounded.Settings
//import androidx.compose.material.icons.rounded.Shield
//import androidx.compose.material.icons.rounded.TrackChanges
//import androidx.compose.material.icons.rounded.Widgets
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.sangeeta.chronomind.ui.theme.AuraColors
//import com.sangeeta.chronomind.ui.theme.AuraTypography
//
//@Composable
//fun SettingsScreen(
//    viewModel: SettingsViewModel = hiltViewModel(),
//    onBackClick: () -> Unit,
//    onRowClick: (String) -> Unit = {}
//) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    SettingsScreenContent(
//        uiState = uiState,
//        onBackClick = onBackClick,
//        onRowClick = onRowClick
//    )
//}
//
//@Composable
//private fun SettingsScreenContent(
//    uiState: SettingsUiState,
//    onBackClick: () -> Unit,
//    onRowClick: (String) -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AuraColors.BackgroundDark)
//            .statusBarsPadding()
//            .windowInsetsPadding(WindowInsets.navigationBars)
//    ) {
//        SettingsTopBar(
//            onBackClick = onBackClick,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp, vertical = 18.dp)
//        )
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize(),
//            contentPadding = PaddingValues(
//                start = 20.dp,
//                end = 20.dp,
//                top = 4.dp,
//                bottom = 20.dp
//            ),
//            verticalArrangement = Arrangement.spacedBy(18.dp)
//        ) {
//            item {
//                SettingsSectionCard(
//                    title = "GENERAL",
//                    icon = Icons.Rounded.Settings,
//                    items = uiState.generalItems,
//                    onRowClick = onRowClick
//                )
//            }
//
//            item {
//                SettingsSectionCard(
//                    title = "FOCUS DEFAULTS",
//                    icon = Icons.Rounded.TrackChanges,
//                    items = uiState.focusDefaultsItems,
//                    onRowClick = onRowClick,
//                    footerText = "These are app-level defaults. Individual activities can override them."
//                )
//            }
//
//            item {
//                SettingsSectionCard(
//                    title = "WIDGETS",
//                    icon = Icons.Rounded.Widgets,
//                    items = uiState.widgetItems,
//                    onRowClick = onRowClick
//                )
//            }
//
//            item {
//                SettingsSectionCard(
//                    title = "HELP",
//                    icon = Icons.Rounded.Help,
//                    items = uiState.helpItems,
//                    onRowClick = onRowClick
//                )
//            }
//
//            item {
//                SettingsSectionCard(
//                    title = "PLAY STORE / TRUST",
//                    icon = Icons.Rounded.Shield,
//                    items = uiState.trustItems,
//                    onRowClick = onRowClick
//                )
//            }
//
//            item {
//                SettingsSectionCard(
//                    title = "ABOUT",
//                    icon = Icons.Rounded.Info,
//                    items = uiState.aboutItems,
//                    onRowClick = onRowClick
//                )
//            }
//
//            item {
//                Spacer(modifier = Modifier.height(6.dp))
//            }
//        }
//    }
//}
//
//@Composable
//private fun SettingsTopBar(
//    onBackClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            CircleBackButton(onClick = onBackClick)
//
//            Spacer(modifier = Modifier.width(14.dp))
//
//            Text(
//                text = "Settings",
//                fontSize = 22.sp,
//                style = AuraTypography.TitleMedium.copy(
//                    fontWeight = FontWeight.Bold
//                ),
//                color = AuraColors.TextPrimary
//            )
//        }
//
//        Text(
//            text = "Customize the app your way.",
//            fontSize = 12.sp,
//            style = AuraTypography.BodySmall,
//            color = AuraColors.TextSecondary,
//            modifier = Modifier.padding(start = 58.dp)
//        )
//    }
//}
//
//@Composable
//private fun SettingsSectionCard(
//    title: String,
//    icon : ImageVector,
//    items: List<SettingsRowUiModel>,
//    onRowClick: (String) -> Unit,
//    footerText: String? = null
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(
//                elevation = 14.dp,
//                shape = RoundedCornerShape(28.dp),
//                ambientColor = AuraColors.YellowGlow.copy(alpha = 0.08f),
//                spotColor = AuraColors.YellowGlow.copy(alpha = 0.08f)
//            )
//            .clip(RoundedCornerShape(28.dp))
//            .background(
////                Brush.verticalGradient(
////                    colors = listOf(Color(0xFF101010), Color(0xFF0C0C0C))
////                )
//                Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF121212),
//                        Color(0xFF0C0C0C)
//                    )
//                    )
//            )
//            .border(
//                width = 1.dp,
//                color = AuraColors.YellowPrimary.copy(alpha = 0.10f),
//                shape = RoundedCornerShape(28.dp)
//            )
//            .padding(18.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(38.dp)
//                    .shadow(
//                        elevation = 10.dp,
//                        shape = CircleShape,
//                        ambientColor = AuraColors.YellowGlow.copy(alpha = 0.20f),
//                        spotColor = AuraColors.YellowGlow.copy(alpha = 0.20f)
//                    )
//                    .clip(CircleShape)
//                    .background(Color(0xFF18130A))
//                    .border(
//                        width = 1.dp,
//                        color = AuraColors.YellowPrimary.copy(alpha = 0.22f),
//                        shape = CircleShape
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = title,
//                    tint = AuraColors.YellowPrimary,
//                    modifier = Modifier.size(18.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Text(
//                text = title,
//                style = AuraTypography.LabelMedium.copy(
//                    fontWeight = FontWeight.Bold,
//                    letterSpacing = 1.2.sp
//                ),
//                color = AuraColors.YellowPrimary
//            )
//        }
//
//        items.forEachIndexed { index, item ->
//            SettingsRow(
//                item = item,
//                onClick = { onRowClick(item.id) }
//            )
//
//            if (index != items.lastIndex) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(1.dp)
//                        .background(Color.White.copy(alpha = 0.04f))
//                )
//            }
//        }
//
//        if (!footerText.isNullOrBlank()) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(18.dp))
//                    .background(Color(0xFF171717))
//                    .padding(horizontal = 14.dp, vertical = 12.dp)
//            ) {
//                Text(
//                    text = footerText,
//                    style = AuraTypography.BodySmall,
//                    color = AuraColors.TextSecondary
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun SettingsRow(
//    item: SettingsRowUiModel,
//    onClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(18.dp))
//            .clickable(enabled = !item.isValueOnly, onClick = onClick)
//            .padding(vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Box(
//            modifier = Modifier
//                .size(42.dp)
//                .clip(CircleShape)
//                .background(Color(0xFF151515))
//                .border(
//                    width = 1.dp,
//                    color = AuraColors.YellowPrimary.copy(alpha = 0.16f),
//                    shape = CircleShape
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = item.icon,
//                contentDescription = item.title,
//                tint = AuraColors.YellowPrimary,
//                modifier = Modifier.size(20.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.width(14.dp))
//
//        Column(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(2.dp)
//        ) {
//            Text(
//                text = item.title,
//                style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.Medium),
//                color = AuraColors.TextPrimary,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            Text(
//                text = item.subtitle,
//                style = AuraTypography.BodySmall,
//                color = AuraColors.TextSecondary,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//
//        if (!item.value.isNullOrBlank()) {
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(14.dp))
//                    .background(Color(0xFF1A1409))
//                    .padding(horizontal = 10.dp, vertical = 7.dp)
//            ) {
//                Text(
//                    text = item.value,
//                    style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
//                    color = AuraColors.YellowPrimary,
//                    maxLines = 1
//                )
//            }
//
//            Spacer(modifier = Modifier.width(8.dp))
//        }
//
//        Icon(
//            imageVector = if (item.isExternal) Icons.Rounded.OpenInNew else Icons.Rounded.ChevronRight,
//            contentDescription = null,
//            tint = AuraColors.TextSecondary
//        )
//    }
//}
//
//@Composable
//private fun CircleBackButton(
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .size(44.dp)
//            .clip(CircleShape)
//            .background(Color(0xFF111111))
//            .border(
//                width = 1.dp,
//                color = AuraColors.CardBorderDefault,
//                shape = CircleShape
//            )
//            .clickable(onClick = onClick),
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = Icons.Rounded.ArrowBack,
//            contentDescription = "Back",
//            tint = AuraColors.TextPrimary
//        )
//    }
//}




package com.sangeeta.chronomind.ui.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material.icons.rounded.Widgets
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
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Info
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onRowClick: (String) -> Unit,
    onResetOnboarding: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        val newState = when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
                NotificationPermissionState.Granted
            }

            granted -> {
                NotificationPermissionState.Granted
            }

            activity != null && !ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                NotificationPermissionState.DeniedPermanently
            }

            else -> {
                NotificationPermissionState.DeniedRequestable
            }
        }

        viewModel.updateNotificationPermissionState(newState)
    }

    LaunchedEffect(Unit) {
        viewModel.updateNotificationPermissionState(
            resolveNotificationPermissionState(activity)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SettingsEvent.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                SettingsEvent.OpenAppNotificationSettings -> {
                    context.startActivity(
                        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                    )
                }
            }
        }
    }

    SettingsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRowClick = onRowClick,
        onNotificationsClick = viewModel::onNotificationsClick,
        onDailyReminderChanged = viewModel::setDailyReminder,
        onReminderTimeSelected = viewModel::setReminderTime,
        onCheckInStyleSelected = viewModel::setCheckInStyle,
        onStreakOnMissSelected = viewModel::setStreakOnMiss,
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
    onNotificationsClick: () -> Unit,
    onDailyReminderChanged: (Boolean) -> Unit,
    onReminderTimeSelected: (String) -> Unit,
    onCheckInStyleSelected: (String) -> Unit,
    onStreakOnMissSelected: (String) -> Unit,
    onShowClearDataConfirm: (Boolean) -> Unit,
    onShowResetConfirm: (Boolean) -> Unit,
    onConfirmClearData: () -> Unit,
    onConfirmReset: () -> Unit
) {
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
                .padding(horizontal = 20.dp, vertical = 18.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 4.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                SettingsSectionCard(
                    title = "GENERAL",
                    icon = Icons.Rounded.Settings
                ) {
                    SettingsRow(
                        item = SettingsRowUiModel(
                            id = "notifications",
                            title = "Notifications",
                            subtitle = "Reminders and alerts",
                            icon = Icons.Rounded.Notifications,
                            value = uiState.notificationPermissionState.toDisplayLabel()
                        ),
                        onClick = onNotificationsClick
                    )

                    SettingsDivider()

                    SettingsToggleRow(
                        icon = Icons.Rounded.NotificationsActive,
                        label = "Daily reminder",
                        subtitle = "Default reminder for new activities",
                        checked = uiState.isDailyReminderEnabled,
                        onCheckedChange = onDailyReminderChanged
                    )

                    AnimatedVisibility(
                        visible = uiState.isDailyReminderEnabled,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            ReminderTimePicker(
                                selected = uiState.reminderTime,
                                onSelected = onReminderTimeSelected
                            )
                        }
                    }
                }
            }

            item {
                SettingsSectionCard(
                    title = "FOCUS DEFAULTS",
                    icon = Icons.Rounded.TrackChanges,
                    footerText = "These are app-level defaults. Individual activities can override them."
                ) {
                    SettingsDropdownRow(
                        label = "Missed Streak",
                        subtitle = "Default rule for new activities",
                        selected = uiState.streakOnMiss,
                        options = listOf("CONTINUE", "RESET"),
                        displayMap = mapOf(
                            "CONTINUE" to "Continue streak",
                            "RESET" to "Reset to zero"
                        ),
                        onSelected = onStreakOnMissSelected
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SettingsDropdownRow(
                        label = "Check-in Style",
                        subtitle = "Default way to mark complete",
                        selected = uiState.checkInStyle,
                        options = listOf("MANUAL", "AUTO_CHECK"),
                        displayMap = mapOf(
                            "MANUAL" to "Manual check-in",
                            "AUTO_CHECK" to "Auto check-in"
                        ),
                        onSelected = onCheckInStyleSelected
                    )
                }
            }

            item {
                SettingsSectionCard(
                    title = "WIDGETS",
                    icon = Icons.Rounded.Widgets,
                    items = uiState.widgetItems,
                    onRowClick = onRowClick
                )
            }

            item {
                SettingsSectionCard(
                    title = "HELP",
                    icon = Icons.Rounded.Help,
                    items = uiState.helpItems,
                    onRowClick = onRowClick
                )
            }

            item {
                SettingsSectionCard(
                    title = "PLAY STORE TRUST",
                    icon = Icons.Rounded.Shield,
                    items = uiState.trustItems,
                    onRowClick = onRowClick
                )
            }

            item {
                SettingsSectionCard(
                    title = "ABOUT",
                    icon = Icons.Rounded.Info,
                    items = uiState.aboutItems,
                    onRowClick = onRowClick
                )
            }

            item {
                SettingsSectionCard(
                    title = "YOUR STATS",
                    icon = Icons.Rounded.CheckCircle
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatBadge(
                            label = "Activities",
                            value = uiState.totalActivities.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                DangerZoneCard(
                    onClearDataClick = { onShowClearDataConfirm(true) },
                    onResetOnboardingClick = { onShowResetConfirm(true) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
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

@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleBackButton(onClick = onBackClick)
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = "Settings",
                fontSize = 22.sp,
                style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
                color = AuraColors.TextPrimary
            )
        }

        Text(
            text = "Customize the app your way.",
            fontSize = 12.sp,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary,
            modifier = Modifier.padding(start = 58.dp)
        )
    }
}

@Composable
private fun CircleBackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF111111))
            .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "Back",
            tint = AuraColors.TextPrimary
        )
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    icon: ImageVector,
    items: List<SettingsRowUiModel>,
    onRowClick: (String) -> Unit,
    footerText: String? = null
) {
    SettingsSectionCard(
        title = title,
        icon = icon,
        footerText = footerText
    ) {
        items.forEachIndexed { index, item ->
            SettingsRow(
                item = item,
                onClick = { onRowClick(item.id) }
            )

            if (index != items.lastIndex) {
                SettingsDivider()
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    icon: ImageVector,
    footerText: String? = null,
    content: @Composable Column.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = AuraColors.YellowGlow.copy(alpha = 0.08f),
                spotColor = AuraColors.YellowGlow.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF0C0C0C))
                )
            )
            .border(
                width = 1.dp,
                color = AuraColors.YellowPrimary.copy(alpha = 0.10f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        ambientColor = AuraColors.YellowGlow.copy(alpha = 0.20f),
                        spotColor = AuraColors.YellowGlow.copy(alpha = 0.20f)
                    )
                    .clip(CircleShape)
                    .background(Color(0xFF18130A))
                    .border(
                        width = 1.dp,
                        color = AuraColors.YellowPrimary.copy(alpha = 0.22f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = AuraColors.YellowPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                style = AuraTypography.LabelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = AuraColors.YellowPrimary
            )
        }

        content()

        if (!footerText.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF171717))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = footerText,
                    style = AuraTypography.BodySmall,
                    color = AuraColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    item: SettingsRowUiModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF18120A))
                .border(
                    1.dp,
                    AuraColors.YellowPrimary.copy(alpha = 0.25f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.subtitle,
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary
            )
        }

        when {
            item.isValueOnly && item.value != null -> {
                Text(
                    text = item.value,
                    style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AuraColors.YellowPrimary
                )
            }

            item.value != null -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.value,
                        style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = AuraColors.TextSecondary
                    )
                    Icon(
                        imageVector = if (item.isExternal) Icons.Rounded.OpenInNew else Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = AuraColors.TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            else -> {
                Icon(
                    imageVector = if (item.isExternal) Icons.Rounded.OpenInNew else Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = AuraColors.TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.04f))
    )
}

@Composable
private fun SettingsToggleRow(
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
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF18120A))
                .border(
                    1.dp,
                    AuraColors.YellowPrimary.copy(alpha = 0.25f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary
            )
            Text(
                text = subtitle,
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AuraColors.YellowPrimary,
                uncheckedThumbColor = AuraColors.TextMuted,
                uncheckedTrackColor = Color(0xFF2B2B2B)
            )
        )
    }
}

@Composable
private fun SettingsDropdownRow(
    label: String,
    subtitle: String,
    selected: String,
    options: List<String>,
    displayMap: Map<String, String> = emptyMap(),
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column {
            Text(
                text = label,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary
            )
            Text(
                text = subtitle,
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary
            )
        }

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF121212))
                    .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayMap[selected] ?: selected,
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
                        text = {
                            Text(
                                text = displayMap[option] ?: option,
                                color = if (option == selected) {
                                    AuraColors.YellowPrimary
                                } else {
                                    AuraColors.TextPrimary
                                },
                                style = AuraTypography.TitleMedium
                            )
                        },
                        onClick = {
                            expanded = false
                            onSelected(option)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderTimePicker(
    selected: String,
    onSelected: (String) -> Unit
) {
    val times = listOf(
        "06:00 AM",
        "07:00 AM",
        "08:00 AM",
        "09:00 AM",
        "12:00 PM",
        "05:00 PM",
        "07:00 PM",
        "08:00 PM",
        "09:00 PM"
    )
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Reminder time",
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary
        )

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF121212))
                    .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        tint = AuraColors.TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = selected,
                        style = AuraTypography.TitleMedium,
                        color = AuraColors.TextPrimary
                    )
                }

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
                times.forEach { time ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = time,
                                color = if (time == selected) {
                                    AuraColors.YellowPrimary
                                } else {
                                    AuraColors.TextPrimary
                                },
                                style = AuraTypography.TitleMedium
                            )
                        },
                        onClick = {
                            expanded = false
                            onSelected(time)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatBadge(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF151515))
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = AuraTypography.BodySmall,
            color = AuraColors.TextSecondary
        )
        Text(
            text = value,
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
            color = AuraColors.YellowPrimary
        )
    }
}

@Composable
private fun DangerZoneCard(
    onClearDataClick: () -> Unit,
    onResetOnboardingClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF120E0E))
            .border(1.dp, Color(0xFF442626), RoundedCornerShape(24.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Danger Zone",
            style = AuraTypography.TitleMedium,
            color = Color(0xFFE35D5D)
        )

        Text(
            text = "These actions are permanent and cannot be undone.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary
        )

        DangerButton(
            icon = Icons.Rounded.DeleteForever,
            label = "Clear all activities",
            onClick = onClearDataClick
        )

        DangerButton(
            icon = Icons.Rounded.RestartAlt,
            label = "Reset onboarding",
            onClick = onResetOnboardingClick
        )
    }
}

@Composable
private fun DangerButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF1B1212))
            .border(1.dp, Color(0xFF5B2D2D), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF2A1515)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFE35D5D),
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = label,
            style = AuraTypography.TitleMedium,
            color = Color(0xFFFFD2D2),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFE35D5D),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun ConfirmDialog(
    title: String,
    body: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AuraColors.SurfaceCard,
        title = {
            Text(
                text = title,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary
            )
        },
        text = {
            Text(
                text = body,
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = AuraColors.YellowPrimary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = AuraColors.TextSecondary
                )
            }
        }
    )
}

private fun resolveNotificationPermissionState(activity: Activity?): NotificationPermissionState {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return NotificationPermissionState.Granted
    }

    val safeActivity = activity ?: return NotificationPermissionState.Unknown

    return when {
        ContextCompat.checkSelfPermission(
            safeActivity,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED -> {
            NotificationPermissionState.Granted
        }

        ActivityCompat.shouldShowRequestPermissionRationale(
            safeActivity,
            Manifest.permission.POST_NOTIFICATIONS
        ) -> {
            NotificationPermissionState.DeniedRequestable
        }

        else -> {
            NotificationPermissionState.DeniedPermanently
        }
    }
}

private fun NotificationPermissionState.toDisplayLabel(): String {
    return when (this) {
        NotificationPermissionState.Granted -> "Allowed"
        NotificationPermissionState.DeniedRequestable -> "Tap to allow"
        NotificationPermissionState.DeniedPermanently -> "Open settings"
        NotificationPermissionState.Unknown -> "Manage"
    }
}
//}