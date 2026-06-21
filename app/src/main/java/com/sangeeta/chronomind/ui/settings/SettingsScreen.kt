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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    SettingsTopBar(onBackClick = onBackClick)

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
            SettingsSectionCard(
                title = "GENERAL",
                items = uiState.generalItems,
                onRowClick = onRowClick
            )
        }

        item {
            SettingsSectionCard(
                title = "FOCUS DEFAULTS",
                items = uiState.focusDefaultsItems,
                onRowClick = onRowClick,
                footerText = "These are app-level defaults. Individual activities can override them."
            )
        }

        item {
            SettingsSectionCard(
                title = "WIDGETS",
                items = uiState.widgetItems,
                onRowClick = onRowClick
            )
        }

        item {
            SettingsSectionCard(
                title = "HELP",
                items = uiState.helpItems,
                onRowClick = onRowClick
            )
        }

        item {
            SettingsSectionCard(
                title = "PLAY STORE / TRUST",
                items = uiState.trustItems,
                onRowClick = onRowClick
            )
        }

        item {
            SettingsSectionCard(
                title = "ABOUT",
                items = uiState.aboutItems,
                onRowClick = onRowClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleBackButton(onClick = onBackClick)

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = "Settings",
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary
            )
        }

        Text(
            text = "Customize the app your way.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary,
            modifier = Modifier.padding(start = 58.dp)
        )
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    items: List<SettingsRowUiModel>,
    onRowClick: (String) -> Unit,
    footerText: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF101010), Color(0xFF0C0C0C))
                )
            )
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
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
                        ambientColor = AuraColors.YellowGlow,
                        spotColor = AuraColors.YellowGlow
                    )
                    .clip(CircleShape)
                    .background(Color(0xFF18120A))
                    .border(
                        width = 1.dp,
                        color = AuraColors.YellowPrimary.copy(alpha = 0.26f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "•",
                    color = AuraColors.YellowPrimary,
                    style = AuraTypography.DisplayMedium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
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
                        .background(Color.White.copy(alpha = 0.05f))
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
                    style = AuraTypography.BodyMedium,
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
            .clip(RoundedCornerShape(20.dp))
            .clickable(enabled = !item.isValueOnly, onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF151515))
                .border(
                    width = 1.dp,
                    color = AuraColors.YellowPrimary.copy(alpha = 0.18f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = AuraColors.YellowPrimary
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.title,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary
            )
            Text(
                text = item.subtitle,
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary
            )
        }

        if (!item.value.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (item.isValueOnly) Color.Transparent else Color(0xFF17120A))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = item.value,
                    style = AuraTypography.BodyMedium,
                    color = if (item.isValueOnly) {
                        AuraColors.YellowPrimary
                    } else {
                        AuraColors.YellowPrimary
                    }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        when {
            item.isValueOnly -> Unit
            item.isExternal -> {
                Icon(
                    imageVector = Icons.Rounded.OpenInNew,
                    contentDescription = "Open external",
                    tint = AuraColors.TextSecondary
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = "Open",
                    tint = AuraColors.TextSecondary
                )
            }
        }
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