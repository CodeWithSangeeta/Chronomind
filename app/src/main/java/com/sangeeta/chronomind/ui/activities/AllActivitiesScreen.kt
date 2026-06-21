package com.sangeeta.chronomind.ui.activities


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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.sangeeta.chronomind.ui.model.ActivitySortOption
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import com.sangeeta.chronomind.ui.model.AllActivitiesUiState
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun AllActivitiesScreen(
    viewModel: AllActivitiesViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNewActivityClick: () -> Unit,
    onEditActivityClick: (Int) -> Unit,
    onStartActivityClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AllActivitiesScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSearchChange = viewModel::onSearchQueryChange,
        onSortSelected = viewModel::onSortSelected,
        onNewActivityClick = onNewActivityClick,
        onEditActivityClick = onEditActivityClick,
        onDeleteActivityClick = viewModel::onDeleteActivity,
        onStartActivityClick = { id ->
            viewModel.onStartActivity(id)
            onStartActivityClick(id)
        }
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
                    ActivitiesEmptyState(onCreateClick = onNewActivityClick)
                }
            }

            uiState.isSearchEmpty -> {
                item {
                    SearchEmptyState()
                }
            }

            else -> {
                items(uiState.filteredActivities, key = { it.id }) { activity ->
                    ActivityLibraryCard(
                        activity = activity,
                        onCardClick = { onEditActivityClick(activity.id) },
                        onStartClick = { onStartActivityClick(activity.id) },
                        onEditClick = { onEditActivityClick(activity.id) },
                        onDeleteClick = { onDeleteActivityClick(activity.id) }
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
            icon = Icons.Rounded.ArrowBack,
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
private fun ActivityLibraryCard(
    activity: ActivityUiModel,
    onCardClick: () -> Unit,
    onStartClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF151515),
                        Color(0xFF101010),
                        Color(0xFF141414)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onCardClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ActivityBadge(
            emoji = activity.icon,
            badgeColor = activity.colorHex
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = activity.name,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${formatTargetDuration(activity.targetSeconds)} • Last used ${activity.lastActiveDate.lowercase()}",
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        CircularIconButton(
            icon = Icons.Rounded.PlayArrow,
            contentDescription = "Start ${activity.name}",
            onClick = onStartClick,
            iconTint = AuraColors.YellowPrimary,
            containerColor = AuraColors.SurfaceCard,
            glow = true
        )

        Box {
            CircularIconButton(
                icon = Icons.Rounded.MoreVert,
                contentDescription = "More actions for ${activity.name}",
                onClick = { menuExpanded = true },
                iconTint = AuraColors.TextPrimary,
                containerColor = Color.Transparent,
                bordered = false
            )

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                containerColor = AuraColors.SurfaceCard
            ) {
                DropdownMenuItem(
                    text = { Text("Edit", color = AuraColors.TextPrimary) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null,
                            tint = AuraColors.TextPrimary
                        )
                    },
                    onClick = {
                        menuExpanded = false
                        onEditClick()
                    }
                )

                DropdownMenuItem(
                    text = { Text("Delete", color = AuraColors.TextPrimary) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            tint = AuraColors.TextPrimary
                        )
                    },
                    onClick = {
                        menuExpanded = false
                        onDeleteClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun ActivityBadge(
    emoji: String,
    badgeColor: String
) {
    val tint = runCatching { Color(android.graphics.Color.parseColor(badgeColor)) }
        .getOrElse { AuraColors.YellowPrimary }

    Box(
        modifier = Modifier
            .size(56.dp)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
                ambientColor = tint.copy(alpha = 0.30f),
                spotColor = tint.copy(alpha = 0.30f)
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        tint.copy(alpha = 0.95f),
                        tint.copy(alpha = 0.55f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            style = AuraTypography.TitleMedium
        )
    }
}

@Composable
private fun CircularIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    iconTint: Color = AuraColors.TextPrimary,
    containerColor: Color = AuraColors.SurfaceCard,
    bordered: Boolean = true,
    glow: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .shadow(
                elevation = if (glow) 12.dp else 0.dp,
                shape = CircleShape,
                ambientColor = AuraColors.YellowGlow,
                spotColor = AuraColors.YellowGlow
            )
            .clip(CircleShape)
            .background(containerColor)
            .then(
                if (bordered) {
                    Modifier.border(
                        width = 1.dp,
                        color = AuraColors.CardBorderDefault,
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            )
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

@Composable
private fun ActivitiesEmptyState(
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(AuraColors.SurfaceCard)
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "No activities yet",
            style = AuraTypography.TitleMedium,
            color = AuraColors.TextPrimary
        )
        Text(
            text = "Create your first focus activity to start building your library.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary
        )
        NewActivityButton(onClick = onCreateClick)
    }
}

@Composable
private fun SearchEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AuraColors.SurfaceCard)
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(22.dp)
    ) {
        Text(
            text = "No activities match your search.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary
        )
    }
}

private fun formatTargetDuration(targetSeconds: Long): String {
    val totalMinutes = targetSeconds / 60
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h 00m"
        else -> "${minutes}m"
    }
}