package com.sangeeta.chronomind.ui.activities

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.components.ActivityCard
import com.sangeeta.chronomind.ui.model.ActivitySessionState
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun AllActivitiesScreen(
    viewModel: AllActivitiesViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNewActivityClick: () -> Unit,
    onEditActivityClick: (Int) -> Unit,
    onSelectActivityClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        val activityId = viewModel.lastPermissionRequestedActivityId
            ?: return@rememberLauncherForActivityResult

        if (granted) {
            viewModel.continueStartAfterPermission(activityId)
        } else {
            viewModel.onNotificationPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AllActivitiesEvent.NavigateToHome -> onSelectActivityClick()
                is AllActivitiesEvent.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.continueStartAfterPermission(event.activityId)
                    }
                }
            }
        }
    }

    AllActivitiesScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSearchChange = viewModel::onSearchQueryChange,
        onSortSelected = viewModel::onSortSelected,
        onNewActivityClick = onNewActivityClick,
        onEditActivityClick = onEditActivityClick,
        onStartActivityClick = viewModel::onPlayClick,
        onPauseActivityClick = viewModel::onPauseClick
    )
}

@Composable
private fun AllActivitiesScreenContent(
    uiState: AllActivitiesUiState,
    onBackClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSortSelected: (ActivitySortOption) -> Unit,
    onNewActivityClick: () -> Unit,
    onEditActivityClick: (Int) -> Unit,
    onStartActivityClick: (Int) -> Unit,
    onPauseActivityClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        AllActivitiesTopBar(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(16.dp))

        SearchAndSortRow(
            query = uiState.searchQuery,
            selectedSort = uiState.selectedSort,
            onQueryChange = onSearchChange,
            onSortSelected = onSortSelected
        )

        Spacer(modifier = Modifier.height(14.dp))

        NewActivityButton(onClick = onNewActivityClick)

        Spacer(modifier = Modifier.height(18.dp))

        ActivitiesHeader(count = uiState.filteredActivities.size)

        Spacer(modifier = Modifier.height(12.dp))

        when {
            uiState.isEmpty -> {
                ActivitiesEmptyState()
            }

            uiState.isSearchEmpty -> {
                SearchEmptyState()
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(uiState.filteredActivities, key = { it.id }) { activity ->
                        ActivityCard(
                            activity = activity,
                            isSelected = false,
                            onCardClick = { onEditActivityClick(activity.id) },

                            onActionClick = {
                                when (activity.sessionState) {
                                    ActivitySessionState.RUNNING -> onPauseActivityClick(activity.id)
                                    ActivitySessionState.PENDING,
                                    ActivitySessionState.IDLE -> {
                                        if (activity.canStart) {
                                            onStartActivityClick(activity.id)
                                        }
                                    }
                                    ActivitySessionState.COMPLETED_TODAY -> Unit
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AllActivitiesTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        CircularIconButton(
            icon = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick
        )

        Text(
            text = "All Activities",
            style = AuraTypography.DisplayMedium,
            color = AuraColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SearchAndSortRow(
    query: String,
    selectedSort: ActivitySortOption,
    onQueryChange: (String) -> Unit,
    onSortSelected: (ActivitySortOption) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = AuraTypography.BodyMedium.copy(color = AuraColors.TextPrimary),
            placeholder = {
                Text(
                    text = "Search activities",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextMuted
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    tint = AuraColors.TextMuted
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AuraColors.SurfaceCardLight,
                unfocusedContainerColor = AuraColors.SurfaceCardLight,
                disabledContainerColor = AuraColors.SurfaceCardLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = AuraColors.YellowPrimary,
                focusedTextColor = AuraColors.TextPrimary,
                unfocusedTextColor = AuraColors.TextPrimary
            )
        )

        SortButton(
            selectedSort = selectedSort,
            onSortSelected = onSortSelected
        )
    }
}

@Composable
private fun SortButton(
    selectedSort: ActivitySortOption,
    onSortSelected: (ActivitySortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AuraColors.SurfaceCardLight,
                            AuraColors.SurfaceCard
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = AuraColors.CardBorderDefault,
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Sort,
                contentDescription = "Sort",
                tint = AuraColors.YellowPrimary
            )
            Text(
                text = "Sort",
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = AuraColors.SurfaceCard
        ) {
            ActivitySortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.label,
                            color = if (option == selectedSort) {
                                AuraColors.YellowPrimary
                            } else {
                                AuraColors.TextPrimary
                            }
                        )
                    },
                    onClick = {
                        expanded = false
                        onSortSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun NewActivityButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        AuraColors.SurfaceCardLight,
                        AuraColors.SurfaceCard
                    )
                )
            )
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
                .border(
                    width = 1.dp,
                    color = AuraColors.YellowPrimary.copy(alpha = 0.22f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "New Activity",
                tint = AuraColors.YellowPrimary
            )
        }

        Text(
            text = "New Activity",
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.TextPrimary
        )
    }
}

@Composable
private fun ActivitiesHeader(
    count: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "YOUR ACTIVITIES",
            style = AuraTypography.LabelMedium,
            color = AuraColors.TextMuted
        )
        Text(
            text = "$count activities",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextPrimary
        )
    }
}

@Composable
private fun SearchEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "No activities match your search",
            color = AuraColors.TextPrimary
        )
    }
}

@Composable
private fun ActivitiesEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "No activities in your library yet",
            color = AuraColors.TextPrimary
        )
    }
}

@Composable
private fun CircularIconButton(
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
            tint = iconTint
        )
    }
}