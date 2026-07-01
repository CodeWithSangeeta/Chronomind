package com.sangeeta.chronomind.ui.activities

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.components.ActivityCard
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
        onDeleteActivityClick = viewModel::onDeleteActivity,
        onStartActivityClick = viewModel::onPlayClick
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
    onDeleteActivityClick: (Int) -> Unit,
    onStartActivityClick: (Int) -> Unit
) {
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
            AllActivitiesTopBar(onBackClick = onBackClick)
        }

        item {
            SearchAndSortRow(
                query = uiState.searchQuery,
                selectedSort = uiState.selectedSort,
                onQueryChange = onSearchChange,
                onSortSelected = onSortSelected
            )
        }

        item {
            NewActivityButton(onClick = onNewActivityClick)
        }

        item {
            ActivitiesHeader(count = uiState.filteredActivities.size)
        }

        when {
            uiState.isEmpty -> {
                item {
                    ActivitiesEmptyState()
                }
            }

            uiState.isSearchEmpty -> {
                item {
                    SearchEmptyState()
                }
            }

            else -> {
                items(uiState.filteredActivities, key = { it.id }) { activity ->
                    ActivityCard(
                        activity = activity,
                        isSelected = false,
                        onCardClick = { onEditActivityClick(activity.id) },
                        onActionClick = {
                            if (activity.canStart) {
                                onStartActivityClick(activity.id)
                            }
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AllActivitiesTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        CircularIconButton(
            icon = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "All Activities",
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary
            )
            Text(
                text = "Your focus library • Edit, start or manage activities",
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary
            )
        }
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
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AuraColors.SurfaceCard,
                unfocusedContainerColor = AuraColors.SurfaceCard,
                disabledContainerColor = AuraColors.SurfaceCard,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
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
                .clip(RoundedCornerShape(24.dp))
                .background(AuraColors.SurfaceCard)
                .border(
                    width = 1.dp,
                    color = AuraColors.CardBorderDefault,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { expanded = true }
                .padding(horizontal = 18.dp, vertical = 18.dp),
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
                color = AuraColors.YellowPrimary
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
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF131313),
                        Color(0xFF171205)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = AuraColors.YellowPrimary.copy(alpha = 0.45f),
                shape = RoundedCornerShape(26.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = AuraColors.YellowPrimary.copy(alpha = 0.5f),
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

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = "New Activity",
            style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AuraColors.YellowPrimary
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("No activities match your search", color = AuraColors.TextPrimary)
    }
}

@Composable
private fun ActivitiesEmptyState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("No activities in your library yet", color = AuraColors.TextPrimary)
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
        Icon(icon, contentDescription, tint = iconTint)
    }
}
