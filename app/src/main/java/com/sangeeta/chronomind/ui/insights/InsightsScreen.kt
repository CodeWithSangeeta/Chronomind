package com.sangeeta.chronomind.ui.insights

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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Start
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InsightsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRangeSelected = viewModel::onRangeSelected
    )
}

@Composable
private fun InsightsScreenContent(
    uiState: InsightsUiState,
    onBackClick: () -> Unit,
    onRangeSelected: (InsightsRange) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            InsightsTopBar(onBackClick = onBackClick)

            RangeTabs(
                selected = uiState.selectedRange,
                onSelected = onRangeSelected
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                SummaryRow(summary = uiState.summary)
            }

            item {
                TrendCard(
                    selectedRange = uiState.selectedRange,
                    trend = uiState.trend,
                    pattern = uiState.pattern
                )
            }

            item {
                ConsistencyCard(consistency = uiState.consistency)
            }

            item {
                TopActivitiesCard(activities = uiState.topActivities)
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
@Composable
private fun InsightsTopBar(
    onBackClick: () -> Unit
) {
    var showInfoDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        CircularHeaderButton(
            icon = Icons.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Insights",
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary,
                maxLines = 1
            )
        }

        CircularHeaderButton(
            icon = Icons.Rounded.Info,
            contentDescription = "About insights",
            onClick = { showInfoDialog = true },
            iconTint = AuraColors.YellowPrimary
        )
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            containerColor = AuraColors.SurfaceCard,
            title = {
                Text(
                    text = "About Insights",
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Start,
                            contentDescription = null,
                            tint = AuraColors.YellowPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Insights shows data from your tracked sessions, not just the activities you created.",
                            style = AuraTypography.BodyMedium,
                            color = AuraColors.TextSecondary,
                            fontSize = 13.sp,
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Start,
                            contentDescription = null,
                            tint = AuraColors.YellowPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                    Text(
                        text = "Completed sessions and completion rate are based only on sessions you finished successfully.",
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary,
                        fontSize = 13.sp
                    )
                  }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Start,
                            contentDescription = null,
                            tint = AuraColors.YellowPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                    Text(
                        text = "Charts start from the date you began using the app, so earlier days or months may not appear.",
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary,
                        fontSize = 13.sp
                    )
                  }
                }
            },
            confirmButton = {
                Text(
                    text = "Got it",
                    color = AuraColors.YellowPrimary,
                    modifier = Modifier.clickable { showInfoDialog = false }
                )
            }
        )
    }
}

@Composable
private fun CircularHeaderButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    iconTint: Color = AuraColors.TextPrimary
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
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
    }
}