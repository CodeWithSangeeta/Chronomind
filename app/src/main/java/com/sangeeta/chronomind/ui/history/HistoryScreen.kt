package com.sangeeta.chronomind.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRangeSelected = viewModel::onRangeSelected,
        onFilterSelected = viewModel::onFilterSelected,
        onSortSelected = viewModel::onSortSelected,
    )
}

@Composable
private fun HistoryScreenContent(
    uiState: HistoryUiState,
    onBackClick: () -> Unit,
    onRangeSelected: (HistoryRange) -> Unit,
    onFilterSelected: (HistoryFilter) -> Unit,
    onSortSelected: (HistorySort) -> Unit,
) {
    var showIncompleteInfo by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {


        HistoryTopBar(
            onBackClick = onBackClick,
            onInfoClick = { showIncompleteInfo = true }
        )

        if (showIncompleteInfo) {
            AlertDialog(
                onDismissRequest = { showIncompleteInfo = false },
                title = {
                    Text(
                        text = "About incomplete sessions",
                        style = AuraTypography.TitleMedium,
                        color = AuraColors.TextPrimary
                    )
                },
                text = {
                    Text(
                        text = "An incomplete session is added after midnight if a paused session is still unfinished. It then appears in History with an Incomplete badge.",
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary
                    )
                },
                confirmButton = {
                    Text(
                        text = "Got it",
                        color = AuraColors.YellowPrimary,
                        modifier = Modifier.clickable { showIncompleteInfo = false }
                    )
                },
                containerColor = AuraColors.SurfaceCard
            )
        }

        SummaryCard(
            uiState = uiState,
            onRangeSelected = onRangeSelected,
        )

        ControlRow(
            selectedFilter = uiState.selectedFilter,
            selectedSort = uiState.selectedSort,
            onFilterSelected = onFilterSelected,
            onSortSelected = onSortSelected,
        )

        if (uiState.isEmpty) {
            HistoryEmptyState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 2.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.groupedSessions.forEach { group ->
                    item(key = "header_${group.title}_${group.subtitleDate}") {
                        DateGroupHeader(
                            title = group.title,
                            subtitleDate = group.subtitleDate,
                            sessionCount = group.sessions.size,
                        )
                    }

                    items(
                        items = group.sessions,
                        key = { it.id }
                    ) { session ->
                        HistorySessionCard(session = session)
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTopBar(
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        HistoryIconButton(
            icon = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick,
        )

        Text(
            text = "History",
            modifier = Modifier.weight(1f),
            style = AuraTypography.DisplayMedium,
            color = AuraColors.TextPrimary,
            maxLines = 1,
        )

        HistoryIconButton(
            icon = Icons.Rounded.Info,
            contentDescription = "About incomplete sessions",
            onClick = onInfoClick,
        )
    }
}


@Composable
private fun DateGroupHeader(
    title: String,
    subtitleDate: String,
    sessionCount: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraColors.TextPrimary,
                maxLines = 1,
            )
            Text(
                text = subtitleDate,
                style = AuraTypography.BodySmall,
                color = AuraColors.TextSecondary,
                maxLines = 1,
            )
        }

        Text(
            text = "$sessionCount items",
            style = AuraTypography.BodySmall,
            color = AuraColors.TextMuted,
            maxLines = 1,
        )
    }
}

@Composable
private fun HistoryEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(AuraColors.SurfaceCard)
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "No sessions found",
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary,
        )
        Text(
            text = "Try another range, status, or sort option to inspect your focus history.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary,
        )
    }
}

@Composable
private fun HistoryIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(AuraColors.SurfaceCard)
            .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = AuraColors.TextPrimary,
            modifier = Modifier.size(20.dp),
        )
    }
}