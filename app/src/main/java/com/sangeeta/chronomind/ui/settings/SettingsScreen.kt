package com.sangeeta.chronomind.ui.settings

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
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onRowClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRowClick = onRowClick
    )
}

@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onRowClick: (String) -> Unit
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
            modifier = Modifier
                .fillMaxSize(),
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
                    icon = Icons.Rounded.Settings,
                    items = uiState.generalItems,
                    onRowClick = onRowClick
                )
            }

            item {
                SettingsSectionCard(
                    title = "FOCUS DEFAULTS",
                    icon = Icons.Rounded.TrackChanges,
                    items = uiState.focusDefaultsItems,
                    onRowClick = onRowClick,
                    footerText = "These are app-level defaults. Individual activities can override them."
                )
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
                    title = "PLAY STORE / TRUST",
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
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleBackButton(onClick = onBackClick)

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = "Settings",
                fontSize = 22.sp,
                style = AuraTypography.TitleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
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
private fun SettingsSectionCard(
    title: String,
    icon : ImageVector,
    items: List<SettingsRowUiModel>,
    onRowClick: (String) -> Unit,
    footerText: String? = null
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
//                Brush.verticalGradient(
//                    colors = listOf(Color(0xFF101010), Color(0xFF0C0C0C))
//                )
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF121212),
                        Color(0xFF0C0C0C)
                    )
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        items.forEachIndexed { index, item ->
            SettingsRow(
                item = item,
                onClick = { onRowClick(item.id) }
            )

            if (index != items.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.04f))
                )
            }
        }

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
            .clip(RoundedCornerShape(18.dp))
            .clickable(enabled = !item.isValueOnly, onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFF151515))
                .border(
                    width = 1.dp,
                    color = AuraColors.YellowPrimary.copy(alpha = 0.16f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = AuraColors.YellowPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = item.title,
                style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.Medium),
                color = AuraColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.subtitle,
                style = AuraTypography.BodySmall,
                color = AuraColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (!item.value.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1A1409))
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Text(
                    text = item.value,
                    style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                    color = AuraColors.YellowPrimary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            imageVector = if (item.isExternal) Icons.Rounded.OpenInNew else Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = AuraColors.TextSecondary
        )
    }
}

@Composable
private fun CircleBackButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
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
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "Back",
            tint = AuraColors.TextPrimary
        )
    }
}




//package com.sangeeta.chronomind.ui.settings
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.sangeeta.chronomind.ui.theme.AuraColors
//import com.sangeeta.chronomind.ui.theme.AuraTypography
//
//@Composable
//fun SettingsScreen(
//    viewModel: SettingsViewModel = hiltViewModel(),
//    onBackClick: () -> Unit,
//    onResetOnboarding: () -> Unit = {}
//) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    // Show save success toast briefly
//    LaunchedEffect(uiState.saveSuccess) {
//        if (uiState.saveSuccess) {
//            kotlinx.coroutines.delay(1500)
//            viewModel.clearSaveSuccess()
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize().background(AuraColors.BackgroundDark)) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .statusBarsPadding()
//                .windowInsetsPadding(WindowInsets.navigationBars),
//            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            item { SettingsTopBar(onBackClick = onBackClick) }
//
//            // ── Profile section ──────────────────────────────────────────────
//            item {
//                SettingsSectionCard(title = "Profile") {
//                    SettingsTextField(
//                        label = "Your name",
//                        value = uiState.userName,
//                        onValueChange = viewModel::updateUserName,
//                        placeholder = "Enter your name"
//                    )
//                    Spacer(Modifier.height(14.dp))
//                    SaveButton(text = "Save Profile", onClick = viewModel::saveProfile)
//                }
//            }
//
//            // ── Behaviour preferences ────────────────────────────────────────
//            item {
//                SettingsSectionCard(title = "Focus Behaviour") {
//                    SettingsDropdownRow(
//                        label = "Accountability style",
//                        subtitle = "How you want to be held accountable",
//                        selected = uiState.accountabilityType,
//                        options = listOf("STREAKS", "DAILY_GOAL", "NONE"),
//                        displayMap = mapOf(
//                            "STREAKS" to "Streaks",
//                            "DAILY_GOAL" to "Daily Goal",
//                            "NONE" to "None"
//                        ),
//                        onSelected = viewModel::setAccountabilityType
//                    )
//                    Spacer(Modifier.height(14.dp))
//                    SettingsDropdownRow(
//                        label = "Check-in style",
//                        subtitle = "How sessions are marked complete",
//                        selected = uiState.checkInStyle,
//                        options = listOf("MANUAL", "TIMER_END", "COUNT_CHECK"),
//                        displayMap = mapOf(
//                            "MANUAL" to "Manual",
//                            "TIMER_END" to "Timer End",
//                            "COUNT_CHECK" to "Count Check"
//                        ),
//                        onSelected = viewModel::setCheckInStyle
//                    )
//                    Spacer(Modifier.height(14.dp))
//                    SettingsDropdownRow(
//                        label = "Streak on missed day",
//                        subtitle = "What happens if you miss a day",
//                        selected = uiState.streakOnMiss,
//                        options = listOf("CONTINUE", "RESET"),
//                        displayMap = mapOf("CONTINUE" to "Continue Streak", "RESET" to "Reset Streak"),
//                        onSelected = viewModel::setStreakOnMiss
//                    )
//                    Spacer(Modifier.height(14.dp))
//                    SaveButton(text = "Save Preferences", onClick = viewModel::saveBehaviourPrefs)
//                }
//            }
//
//            // ── App preferences ──────────────────────────────────────────────
//            item {
//                SettingsSectionCard(title = "App Preferences") {
//                    SettingsToggleRow(
//                        icon = Icons.Rounded.VolumeUp,
//                        label = "Sound effects",
//                        subtitle = "Play sounds on start & finish",
//                        checked = uiState.isSoundEnabled,
//                        onCheckedChange = viewModel::setSoundEnabled
//                    )
//                    SettingsDivider()
//                    SettingsToggleRow(
//                        icon = Icons.Rounded.Vibration,
//                        label = "Vibration",
//                        subtitle = "Haptic feedback on key events",
//                        checked = uiState.isVibrationEnabled,
//                        onCheckedChange = viewModel::setVibrationEnabled
//                    )
//                    SettingsDivider()
//                    SettingsToggleRow(
//                        icon = Icons.Rounded.BrightnessMedium,
//                        label = "Keep screen on",
//                        subtitle = "Prevent screen from sleeping during session",
//                        checked = uiState.isKeepScreenOn,
//                        onCheckedChange = viewModel::setKeepScreenOn
//                    )
//                    SettingsDivider()
//                    SettingsToggleRow(
//                        icon = Icons.Rounded.NotificationsActive,
//                        label = "Daily reminder",
//                        subtitle = "Remind me to focus each day",
//                        checked = uiState.isDailyReminderEnabled,
//                        onCheckedChange = viewModel::setDailyReminder
//                    )
//                    AnimatedVisibility(
//                        visible = uiState.isDailyReminderEnabled,
//                        enter = fadeIn(),
//                        exit = fadeOut()
//                    ) {
//                        Column {
//                            Spacer(Modifier.height(12.dp))
//                            ReminderTimePicker(
//                                selected = uiState.reminderTime,
//                                onSelected = viewModel::setReminderTime
//                            )
//                        }
//                    }
//                }
//            }
//
//            // ── Stats ────────────────────────────────────────────────────────
//            item {
//                SettingsSectionCard(title = "Your Stats") {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
//                        StatBadge(
//                            label = "Activities",
//                            value = uiState.totalActivities.toString(),
//                            modifier = Modifier.weight(1f)
//                        )
//                    }
//                }
//            }
//
//            // ── Danger zone ──────────────────────────────────────────────────
//            item {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(24.dp))
//                        .background(Color(0xFF120E0E))
//                        .border(1.dp, Color(0xFF442626), RoundedCornerShape(24.dp))
//                        .padding(18.dp),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    Text("Danger Zone", style = AuraTypography.TitleMedium, color = Color(0xFFE35D5D))
//                    Text(
//                        "These actions are permanent and cannot be undone.",
//                        style = AuraTypography.BodyMedium,
//                        color = AuraColors.TextSecondary
//                    )
//                    DangerButton(
//                        icon = Icons.Rounded.DeleteForever,
//                        label = "Clear all activities",
//                        onClick = { viewModel.showClearDataConfirm(true) }
//                    )
//                    DangerButton(
//                        icon = Icons.Rounded.RestartAlt,
//                        label = "Reset onboarding",
//                        onClick = { viewModel.showResetConfirm(true) }
//                    )
//                }
//            }
//
//            item { Spacer(Modifier.height(8.dp)) }
//        }
//
//        // ── Save success toast ───────────────────────────────────────────────
//        AnimatedVisibility(
//            visible = uiState.saveSuccess,
//            enter = fadeIn(),
//            exit = fadeOut(),
//            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(50.dp))
//                    .background(AuraColors.YellowPrimary)
//                    .padding(horizontal = 24.dp, vertical = 12.dp)
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(Icons.Rounded.Check, contentDescription = null, tint = Color(0xFF151515), modifier = Modifier.size(16.dp))
//                    Text("Saved!", style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold), color = Color(0xFF151515))
//                }
//            }
//        }
//    }
//
//    // ── Confirm dialogs ──────────────────────────────────────────────────────
//    if (uiState.showClearDataConfirm) {
//        ConfirmDialog(
//            title = "Clear all activities?",
//            body = "This will permanently delete all your activities and session history.",
//            confirmText = "Yes, clear all",
//            onConfirm = viewModel::clearAllData,
//            onDismiss = { viewModel.showClearDataConfirm(false) }
//        )
//    }
//
//    if (uiState.showResetConfirm) {
//        ConfirmDialog(
//            title = "Reset onboarding?",
//            body = "You will go through the onboarding flow again. Your activities are not affected.",
//            confirmText = "Yes, reset",
//            onConfirm = {
//                viewModel.resetOnboarding()
//                onResetOnboarding()
//            },
//            onDismiss = { viewModel.showResetConfirm(false) }
//        )
//    }
//}
//
//// ─── Top bar ──────────────────────────────────────────────────────────────────
//
//@Composable
//private fun SettingsTopBar(onBackClick: () -> Unit) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.Top,
//        horizontalArrangement = Arrangement.spacedBy(14.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(44.dp)
//                .clip(CircleShape)
//                .background(Color(0xFF111111))
//                .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
//                .clickable(onClick = onBackClick),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = AuraColors.TextPrimary)
//        }
//        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//            Text("Settings", style = AuraTypography.DisplayMedium, color = AuraColors.TextPrimary)
//            Text("Manage your profile & preferences", style = AuraTypography.BodyMedium, color = AuraColors.TextSecondary)
//        }
//    }
//}
//
//// ─── Section card ─────────────────────────────────────────────────────────────
//
//@Composable
//private fun SettingsSectionCard(
//    title: String,
//    content: @Composable ColumnScope.() -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(24.dp))
//            .background(
//                Brush.verticalGradient(listOf(Color(0xFF141414), AuraColors.SurfaceCard))
//            )
//            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(24.dp))
//            .padding(18.dp),
//        verticalArrangement = Arrangement.spacedBy(0.dp)
//    ) {
//        Text(
//            text = title,
//            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
//            color = AuraColors.YellowPrimary,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//        content()
//    }
//}
//
//// ─── Toggle row ───────────────────────────────────────────────────────────────
//
//@Composable
//private fun SettingsToggleRow(
//    icon: ImageVector,
//    label: String,
//    subtitle: String,
//    checked: Boolean,
//    onCheckedChange: (Boolean) -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(14.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(40.dp)
//                .clip(CircleShape)
//                .background(Color(0xFF18120A))
//                .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.25f), CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(icon, contentDescription = null, tint = AuraColors.YellowPrimary, modifier = Modifier.size(20.dp))
//        }
//        Column(modifier = Modifier.weight(1f)) {
//            Text(label, style = AuraTypography.TitleMedium, color = AuraColors.TextPrimary)
//            Text(subtitle, style = AuraTypography.BodyMedium, color = AuraColors.TextSecondary)
//        }
//        Switch(
//            checked = checked,
//            onCheckedChange = onCheckedChange,
//            colors = SwitchDefaults.colors(
//                checkedThumbColor = Color.White,
//                checkedTrackColor = AuraColors.YellowPrimary,
//                uncheckedThumbColor = AuraColors.TextMuted,
//                uncheckedTrackColor = Color(0xFF2B2B2B)
//            )
//        )
//    }
//}
//
//// ─── Dropdown row ────────────────────────────────────────────────────────────
//
//@Composable
//private fun SettingsDropdownRow(
//    label: String,
//    subtitle: String,
//    selected: String,
//    options: List<String>,
//    displayMap: Map<String, String> = emptyMap(),
//    onSelected: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        Column {
//            Text(label, style = AuraTypography.TitleMedium, color = AuraColors.TextPrimary)
//            Text(subtitle, style = AuraTypography.BodyMedium, color = AuraColors.TextSecondary)
//        }
//        Box {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(Color(0xFF121212))
//                    .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
//                    .clickable { expanded = true }
//                    .padding(horizontal = 16.dp, vertical = 14.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = displayMap[selected] ?: selected,
//                    style = AuraTypography.TitleMedium,
//                    color = AuraColors.YellowPrimary
//                )
//                Icon(Icons.Rounded.ExpandMore, contentDescription = null, tint = AuraColors.TextSecondary)
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false },
//                containerColor = AuraColors.SurfaceCard
//            ) {
//                options.forEach { option ->
//                    DropdownMenuItem(
//                        text = {
//                            Text(
//                                text = displayMap[option] ?: option,
//                                color = if (option == selected) AuraColors.YellowPrimary else AuraColors.TextPrimary,
//                                style = AuraTypography.TitleMedium
//                            )
//                        },
//                        onClick = {
//                            expanded = false
//                            onSelected(option)
//                        }
//                    )
//                }
//            }
//        }
//    }
//}
//
//// ─── TextField ────────────────────────────────────────────────────────────────
//
//@Composable
//private fun SettingsTextField(
//    label: String,
//    value: String,
//    onValueChange: (String) -> Unit,
//    placeholder: String = ""
//) {
//    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        Text(label, style = AuraTypography.TitleMedium, color = AuraColors.TextPrimary)
//        TextField(
//            value = value,
//            onValueChange = { if (it.length <= 40) onValueChange(it) },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
//            textStyle = AuraTypography.TitleMedium.copy(color = AuraColors.TextPrimary),
//            placeholder = {
//                Text(placeholder, style = AuraTypography.BodyMedium, color = AuraColors.TextMuted)
//            },
//            shape = RoundedCornerShape(16.dp),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = Color(0xFF121212),
//                unfocusedContainerColor = Color(0xFF121212),
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                cursorColor = AuraColors.YellowPrimary,
//                focusedTextColor = AuraColors.TextPrimary,
//                unfocusedTextColor = AuraColors.TextPrimary
//            )
//        )
//    }
//}
//
//// ─── Reminder time picker ─────────────────────────────────────────────────────
//
//@Composable
//private fun ReminderTimePicker(selected: String, onSelected: (String) -> Unit) {
//    val times = listOf("06:00 AM", "07:00 AM", "08:00 AM", "09:00 AM",
//        "12:00 PM", "05:00 PM", "07:00 PM", "08:00 PM", "09:00 PM")
//    var expanded by remember { mutableStateOf(false) }
//    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
//        Text("Reminder time", style = AuraTypography.TitleMedium, color = AuraColors.TextPrimary)
//        Box {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(Color(0xFF121212))
//                    .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
//                    .clickable { expanded = true }
//                    .padding(horizontal = 16.dp, vertical = 14.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//                    Icon(Icons.Rounded.Schedule, contentDescription = null, tint = AuraColors.TextSecondary, modifier = Modifier.size(18.dp))
//                    Text(selected, style = AuraTypography.TitleMedium, color = AuraColors.TextPrimary)
//                }
//                Icon(Icons.Rounded.ExpandMore, contentDescription = null, tint = AuraColors.TextSecondary)
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false },
//                containerColor = AuraColors.SurfaceCard
//            ) {
//                times.forEach { time ->
//                    DropdownMenuItem(
//                        text = {
//                            Text(
//                                text = time,
//                                color = if (time == selected) AuraColors.YellowPrimary else AuraColors.TextPrimary,
//                                style = AuraTypography.TitleMedium
//                            )
//                        },
//                        onClick = { expanded = false; onSelected(time) }
//                    )
//                }
//            }
//        }
//    }
//}
//
//// ─── Save button ──────────────────────────────────────────────────────────────
//
//@Composable
//private fun SaveButton(text: String, onClick: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16.dp))
//            .background(
//                Brush.horizontalGradient(
//                    listOf(AuraColors.YellowPrimary, Color(0xFFF2BA2E))
//                )
//            )
//            .clickable(onClick = onClick)
//            .padding(vertical = 14.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text,
//            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
//            color = Color(0xFF151515)
//        )
//    }
//}
//
//// ─── Stat badge ───────────────────────────────────────────────────────────────
//
//@Composable
//private fun StatBadge(label: String, value: String, modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier
//            .clip(RoundedCornerShape(16.dp))
//            .background(Color(0xFF111111))
//            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(16.dp))
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(4.dp)
//    ) {
//        Text(value, style = AuraTypography.DisplayMedium, color = AuraColors.YellowPrimary)
//        Text(label, style = AuraTypography.BodyMedium, color = AuraColors.TextSecondary)
//    }
//}
//
//// ─── Danger button ────────────────────────────────────────────────────────────
//
//@Composable
//private fun DangerButton(icon: ImageVector, label: String, onClick: () -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16.dp))
//            .background(Color(0xFF1B1111))
//            .border(1.dp, Color(0xFF6E2D2D), RoundedCornerShape(16.dp))
//            .clickable(onClick = onClick)
//            .padding(horizontal = 16.dp, vertical = 14.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(10.dp)
//    ) {
//        Icon(icon, contentDescription = label, tint = Color(0xFFE35D5D))
//        Text(label, style = AuraTypography.TitleMedium, color = Color(0xFFE35D5D))
//    }
//}
//
//// ─── Divider ──────────────────────────────────────────────────────────────────
//
//@Composable
//private fun SettingsDivider() {
//    HorizontalDivider(
//        modifier = Modifier.padding(vertical = 8.dp),
//        color = AuraColors.CardBorderDefault.copy(alpha = 0.5f),
//        thickness = 0.5.dp
//    )
//}
//
//// ─── Confirm dialog ───────────────────────────────────────────────────────────
//
//@Composable
//private fun ConfirmDialog(
//    title: String,
//    body: String,
//    confirmText: String,
//    onConfirm: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        containerColor = AuraColors.SurfaceCard,
//        title = { Text(title, style = AuraTypography.TitleMedium, color = AuraColors.TextPrimary) },
//        text = { Text(body, style = AuraTypography.BodyMedium, color = AuraColors.TextSecondary) },
//        confirmButton = {
//            TextButton(onClick = onConfirm) {
//                Text(confirmText, color = Color(0xFFE35D5D), style = AuraTypography.TitleMedium)
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancel", color = AuraColors.TextMuted, style = AuraTypography.TitleMedium)
//            }
//        }
//    )
//}