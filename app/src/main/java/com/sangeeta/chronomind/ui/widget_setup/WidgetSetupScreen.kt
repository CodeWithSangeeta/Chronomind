package com.sangeeta.chronomind.ui.widget_setup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun WidgetSetupScreen(
    viewModel: WidgetSetupViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSetWidgetClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WidgetSetupContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onHelpClick = { viewModel.showHelp(true) },
        onHelpDismiss = { viewModel.showHelp(false) },
        onActivitySelected = viewModel::selectActivity,
        onSizeSelected = viewModel::selectWidgetSize,
        onSetWidgetClick = {
            viewModel.setWidget()
            onSetWidgetClick()
        }
    )
}

@Composable
private fun WidgetSetupContent(
    uiState: WidgetSetupUiState,
    onBackClick: () -> Unit,
    onHelpClick: () -> Unit,
    onHelpDismiss: () -> Unit,
    onActivitySelected: (Int) -> Unit,
    onSizeSelected: (WidgetSizeOption) -> Unit,
    onSetWidgetClick: () -> Unit
) {
    if (uiState.showHelpDialog) {
        AlertDialog(
            onDismissRequest = onHelpDismiss,
            containerColor = AuraColors.SurfaceCard,
            title = {
                Text(
                    text = "How to place the widget",
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )
            },
            text = {
                Text(
                    text = "Long press your home screen, open widgets, find ChronoMind, choose a size, then place it. After that, select your activity here and confirm the widget.",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary
                )
            },
            confirmButton = {
                Text(
                    text = "Got it",
                    color = AuraColors.YellowPrimary,
                    modifier = Modifier.clickable(onClick = onHelpDismiss)
                )
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            WidgetSetupTopBar(
                onBackClick = onBackClick,
                onHelpClick = onHelpClick
            )
        }

        item {
            Text(
                text = "Activity",
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary
            )
        }

        item {
            ActivitySelectionRow(
                activities = uiState.activities,
                selectedActivityId = uiState.selectedActivityId,
                onActivitySelected = onActivitySelected
            )
        }

        item {
            Text(
                text = "Widget Size",
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary
            )
        }

        item {
            WidgetSizeRow(
                selectedSize = uiState.selectedSize,
                onSizeSelected = onSizeSelected
            )
        }

        item {
            Text(
                text = "Preview",
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary
            )
        }

        item {
            if (uiState.selectedActivity != null) {
//                WidgetPreviewCard(
////                    activity = uiState.selectedActivity,
////                    size = uiState.selectedSize
//                )
            } else {
                EmptyPreviewState()
            }
        }

        item {
            SetWidgetButton(
                enabled = uiState.canSetWidget,
                onClick = onSetWidgetClick
            )
        }
    }
}

@Composable
private fun WidgetSetupTopBar(
    onBackClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleHeaderButton(
            icon = Icons.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = "Widget Setup",
            style = AuraTypography.DisplayMedium,
            color = AuraColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )

        CircleHeaderButton(
            icon = Icons.Rounded.Info,
            contentDescription = "Help",
            onClick = onHelpClick,
            iconTint = AuraColors.YellowPrimary
        )
    }
}

@Composable
private fun ActivitySelectionRow(
    activities: List<WidgetActivityUiModel>,
    selectedActivityId: Int?,
    onActivitySelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        activities.forEach { activity ->
            val selected = activity.id == selectedActivityId
            Row(
                modifier = Modifier
                    .width(270.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF101010))
                    .border(
                        width = 1.dp,
                        color = if (selected) {
                            activity.accentColor.copy(alpha = 0.55f)
                        } else {
                            AuraColors.CardBorderDefault
                        },
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { onActivitySelected(activity.id) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .shadow(
                            elevation = if (selected) 12.dp else 4.dp,
                            shape = CircleShape,
                            ambientColor = activity.accentColor.copy(alpha = 0.28f),
                            spotColor = activity.accentColor.copy(alpha = 0.28f)
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    activity.accentColor.copy(alpha = 0.95f),
                                    activity.accentColor.copy(alpha = 0.55f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = activity.iconEmoji,
                        style = AuraTypography.TitleMedium
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = activity.name,
                        style = AuraTypography.TitleMedium,
                        color = AuraColors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Target: ${activity.targetText} • Streak: ${activity.streakDays} days",
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun WidgetSizeRow(
    selectedSize: WidgetSizeOption,
    onSizeSelected: (WidgetSizeOption) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        WidgetSizeOption.entries.forEach { size ->
            val selected = size == selectedSize

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF101010))
                    .border(
                        width = 1.dp,
                        color = if (selected) {
                            AuraColors.YellowPrimary.copy(alpha = 0.55f)
                        } else {
                            AuraColors.CardBorderDefault
                        },
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { onSizeSelected(size) }
                    .padding(vertical = 18.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                WidgetSizeVisual(
                    size = size,
                    selected = selected
                )

                Text(
                    text = size.label,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )

                Text(
                    text = size.sizeLabel,
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun WidgetSizeVisual(
    size: WidgetSizeOption,
    selected: Boolean
) {
    val borderColor = if (selected) AuraColors.YellowPrimary.copy(alpha = 0.55f) else AuraColors.CardBorderDefault
    val innerColor = if (selected) AuraColors.YellowPrimary else Color.White.copy(alpha = 0.14f)

    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF141414))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        when (size) {
            WidgetSizeOption.SMALL -> {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(innerColor)
                )
            }

            WidgetSizeOption.MEDIUM -> {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .size(width = 18.dp, height = 30.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.10f))
                    )
                    Box(
                        modifier = Modifier
                            .size(width = 18.dp, height = 44.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(innerColor)
                    )
                }
            }

            WidgetSizeOption.LARGE -> {
                Box(
                    modifier = Modifier
                        .size(width = 42.dp, height = 52.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.10f))
                )
            }
        }
    }
}

@Composable
private fun WidgetPreviewCard(
    activity: WidgetActivityUiModel,
    size: WidgetSizeOption
) {
    when (size) {
        WidgetSizeOption.SMALL -> SmallWidgetPreview(activity)
        WidgetSizeOption.MEDIUM -> MediumWidgetPreview(activity)
        WidgetSizeOption.LARGE -> LargeWidgetPreview(activity)
    }
}

@Composable
private fun SmallWidgetPreview(
    activity: WidgetActivityUiModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFF111111), Color(0xFF0D0D0D))
                )
            )
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                WidgetEmojiBadge(activity = activity)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = activity.name,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            CircularProgressMini(
                percent = activity.progressPercent,
                accent = activity.accentColor
            )
        }

        CircleActionChip(
            icon = Icons.Rounded.CheckCircle,
            contentDescription = "Mark complete"
        )
    }
}

@Composable
private fun MediumWidgetPreview(
    activity: WidgetActivityUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF101010), Color(0xFF0C0C0C))
                )
            )
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            WidgetEmojiBadge(activity = activity)

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = activity.name,
                style = AuraTypography.DisplayMedium,
                color = AuraColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )

            CircleActionChip(
                icon = Icons.Rounded.Info,
                contentDescription = "Options"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            CircularProgressCard(
                percent = activity.progressPercent,
                accent = activity.accentColor
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Target",
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary
                    )
                    Text(
                        text = activity.targetText,
                        style = AuraTypography.DisplayMedium,
                        color = AuraColors.TextPrimary
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Progress",
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary
                    )
                    Text(
                        text = activity.progressText,
                        style = AuraTypography.TitleMedium,
                        color = AuraColors.YellowPrimary
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFF1E1E1E))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(activity.progressPercent / 100f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFF8D648),
                                            Color(0xFFCA8E08)
                                        )
                                    )
                                )
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Streak",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary
                )
                Text(
                    text = "🔥",
                    style = AuraTypography.DisplayMedium
                )
                Text(
                    text = activity.streakDays.toString(),
                    style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AuraColors.TextPrimary
                )
                Text(
                    text = "days",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.YellowPrimary
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            WidgetActionButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.CheckCircle,
                text = "Mark Complete"
            )

            WidgetActionButton(
                modifier = Modifier.weight(1f),
                icon = if (activity.isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                text = if (activity.isRunning) "Pause Focus" else "Start Focus"
            )
        }
    }
}

@Composable
private fun LargeWidgetPreview(
    activity: WidgetActivityUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF101010), Color(0xFF0C0C0C))
                )
            )
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            WidgetEmojiBadge(activity = activity)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.name,
                    style = AuraTypography.DisplayMedium,
                    color = AuraColors.TextPrimary
                )
                Text(
                    text = "Target ${activity.targetText}",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            CircularProgressCard(
                percent = activity.progressPercent,
                accent = activity.accentColor
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Progress",
                    style = AuraTypography.BodyMedium,
                    color = AuraColors.TextSecondary
                )
                Text(
                    text = activity.progressText,
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.YellowPrimary
                )
                Text(
                    text = "Streak ${activity.streakDays} days",
                    style = AuraTypography.TitleMedium,
                    color = AuraColors.TextPrimary
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            WidgetActionButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.CheckCircle,
                text = "Mark Complete"
            )

            WidgetActionButton(
                modifier = Modifier.weight(1f),
                icon = if (activity.isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                text = if (activity.isRunning) "Pause Focus" else "Start Focus"
            )
        }
    }
}

@Composable
private fun WidgetEmojiBadge(
    activity: WidgetActivityUiModel
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                ambientColor = activity.accentColor.copy(alpha = 0.32f),
                spotColor = activity.accentColor.copy(alpha = 0.32f)
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        activity.accentColor.copy(alpha = 0.95f),
                        activity.accentColor.copy(alpha = 0.55f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = activity.iconEmoji,
            style = AuraTypography.TitleMedium
        )
    }
}

@Composable
private fun CircularProgressCard(
    percent: Int,
    accent: Color
) {
    Box(
        modifier = Modifier.size(132.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 10.dp.toPx()

            drawArc(
                color = Color.White.copy(alpha = 0.10f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        accent.copy(alpha = 0.6f),
                        AuraColors.YellowPrimary
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360f * (percent / 100f),
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$percent%",
                style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraColors.YellowPrimary
            )
            Text(
                text = "Complete",
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary
            )
        }
    }
}

@Composable
private fun CircularProgressMini(
    percent: Int,
    accent: Color
) {
    Box(
        modifier = Modifier.size(54.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 6.dp.toPx()

            drawArc(
                color = Color.White.copy(alpha = 0.10f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(accent.copy(alpha = 0.55f), AuraColors.YellowPrimary)
                ),
                startAngle = -90f,
                sweepAngle = 360f * (percent / 100f),
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        Text(
            text = "$percent%",
            style = AuraTypography.BodyMedium,
            color = AuraColors.YellowPrimary
        )
    }
}

@Composable
private fun WidgetActionButton(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF141414))
            .border(
                width = 1.dp,
                color = AuraColors.YellowPrimary.copy(alpha = 0.22f),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = AuraColors.YellowPrimary
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = AuraTypography.TitleMedium,
            color = AuraColors.YellowPrimary
        )
    }
}

@Composable
private fun CircleActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF141414))
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = AuraColors.TextPrimary
        )
    }
}

@Composable
private fun EmptyPreviewState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFF101010))
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Select an activity to preview your widget.",
            style = AuraTypography.BodyMedium,
            color = AuraColors.TextSecondary
        )
    }
}

@Composable
private fun SetWidgetButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(
                if (enabled) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF8D74A),
                            Color(0xFFF1BB2E)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF5A5132), Color(0xFF615435))
                    )
                }
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Widgets,
            contentDescription = "Set Widget",
            tint = Color(0xFF151515)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Set Widget",
            style = AuraTypography.DisplayMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xFF151515)
        )
    }
}

@Composable
private fun CircleHeaderButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    iconTint: Color = AuraColors.TextPrimary
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
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint
        )
    }
}