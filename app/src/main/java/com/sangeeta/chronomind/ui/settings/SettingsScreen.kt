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