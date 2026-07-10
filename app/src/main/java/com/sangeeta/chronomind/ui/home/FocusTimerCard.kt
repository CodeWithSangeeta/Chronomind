package com.sangeeta.chronomind.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sangeeta.chronomind.ui.model.ActivityDisplayState
import com.sangeeta.chronomind.ui.model.ActivitySessionState
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private val CardOuter = Color(0xFF070707)
private val CardInner = Color(0xFF101010)
private val SurfaceTop = Color(0xFF171717)
private val SurfaceMid = Color(0xFF131313)
private val SurfaceLow = Color(0xFF0C0C0C)
private val BorderSoft = Color.White.copy(alpha = 0.07f)
private val BorderGlow = Color(0x33FFC328)
private val TextPrimary = Color(0xFFF5F2EA)
private val TextSecondary = Color(0xFFB7B0A1)
private val TextMuted = Color(0xFF8A857B)
private val Gold = Color(0xFFFFC328)
private val GoldSoft = Color(0xFFFFD76A)
private val GoldDim = Color(0xFFD39A08)
private val Success = Color(0xFF6ED38B)

@Composable
fun FocusTimerCard(
    heroState: ActivityDisplayState?,
    selectedActivity: ActivityUiModel?,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onSwitch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = heroState?.progress ?: 0f
    val timeText = heroState?.displayTime ?: "00:00"
    val isRunning = heroState?.isRunning == true
    val isCompleted = heroState?.sessionState == ActivitySessionState.COMPLETED_TODAY
    val isStopwatch = heroState?.isStopwatch == true
    val targetMinutes = ((heroState?.targetSeconds ?: 0L) / 60L).toInt().coerceAtLeast(0)
    val streakDays = heroState?.streakDays ?: 0

    val statusText = when (heroState?.sessionState) {
        ActivitySessionState.RUNNING -> "Running"
        ActivitySessionState.PENDING -> "Paused"
        ActivitySessionState.COMPLETED_TODAY -> "Done"
        else -> "Ready"
    }

    val pulse = rememberInfiniteTransition(label = "focus-card-pulse")
    val glowAlpha by pulse.animateFloat(
        initialValue = if (isRunning) 0.28f else 0.12f,
        targetValue = if (isRunning) 0.62f else 0.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "focus-glow"
    )

    var showDetailsDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 30.dp,
                shape = RoundedCornerShape(34.dp),
                ambientColor = Gold.copy(alpha = 0.10f),
                spotColor = Color.Black
            )
            .clip(RoundedCornerShape(34.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CardOuter, CardInner, SurfaceLow)
                )
            )
            .border(
                width = 1.dp,
                color = BorderSoft,
                shape = RoundedCornerShape(34.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(34.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.06f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.20f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(1.dp)
                .clip(RoundedCornerShape(33.dp))
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Gold.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        start = Offset.Zero,
                        end = Offset(500f, 500f)
                    ),
                    shape = RoundedCornerShape(33.dp)
                )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(1.dp)
                .clip(RoundedCornerShape(33.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Gold.copy(alpha = glowAlpha * 0.16f),
                            Color.Transparent
                        ),
                        center = Offset(220f, 150f),
                        radius = 760f
                    )
                )
        )

        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(
                            elevation = 18.dp,
                            shape = CircleShape,
                            ambientColor = Color.Black,
                            spotColor = Gold.copy(alpha = 0.18f)
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1B1B1B), Color(0xFF0C0C0C))
                            )
                        )
                        .border(1.dp, BorderSoft, CircleShape)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { showDetailsDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "More details",
                        tint = Gold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(2.05f)
                        .aspectRatio(1f)
                        .padding(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(3.dp)
                            .shadow(
                                elevation = 24.dp,
                                shape = CircleShape,
                                ambientColor = Gold.copy(alpha = 0.06f),
                                spotColor = Color.Black
                            )
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF202020),
                                        Color(0xFF111111),
                                        Color(0xFF090909)
                                    )
                                )
                            )
                            .border(
                                1.dp,
                                Brush.linearGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.10f),
                                        Color.Black.copy(alpha = 0.24f)
                                    )
                                ),
                                CircleShape
                            )
                    )

                    DialCanvas(
                        progress = progress,
                        glowAlpha = glowAlpha,
                        modifier = Modifier.matchParentSize()
                    )

                    BoxWithConstraints(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val density = LocalDensity.current
                        val minSideDp = min(maxWidth.value, maxHeight.value)
                        val segmentCount = timeText.count { it == ':' } + 1
                        val timerSizeSp = with(density) {
                            val multiplier = if (segmentCount == 3) 0.26f else 0.34f
                            (minSideDp * multiplier).dp.toSp()
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (selectedActivity != null) {
                                Box(
                                    modifier = Modifier
                                       // .widthIn(max = maxWidth * 0.9f)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFF161616), Color(0xFF0D0D0D))
                                            )
                                        )
                                        .border(
                                            1.dp,
                                            Brush.verticalGradient(
                                                listOf(
                                                    Color.White.copy(alpha = 0.08f),
                                                    Color.Black.copy(alpha = 0.22f)
                                                )
                                            ),
                                            RoundedCornerShape(18.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = selectedActivity.icon,
                                            contentDescription = null,
                                            tint = GoldSoft,
                                            modifier = Modifier.size(12.dp)
                                        )

                                        Text(
                                            text = selectedActivity.name,
                                            color = GoldSoft,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }

                            Text(
                                text = timeText,
                                color = Gold,
                                fontSize = timerSizeSp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.3.sp,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Visible,
                                modifier = Modifier
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(0.85f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoChipCompact3d(
                        label = if (isStopwatch) "Mode" else "Target",
                        value = if (isStopwatch) "Stopwatch" else "$targetMinutes min",
                        accent = Gold,
                        icon = { TargetGlyphCompact() }
                    )

                    InfoChipCompact3d(
                        label = "Streak",
                        value = "$streakDays days",
                        accent = Gold,
                        icon = { StreakGlyphCompact() }
                    )

                    InfoChipCompact3d(
                        label = "Status",
                        value = statusText,
                        accent = if (isCompleted) Success else Gold,
                        icon = { StatusGlyph(isCompleted = isCompleted) }
                    )
                }
            }

            ControlBar(
                heroState = heroState,
                onStartFocus = onStartFocus,
                onPause = onPause,
                onFinish = onFinish,
                onSwitch = onSwitch
            )
        }
    }

    if (showDetailsDialog) {
        ActivityDetailsDialog(
            activity = selectedActivity,
            heroState = heroState,
            onDismiss = { showDetailsDialog = false }
        )
    }
}

@Composable
private fun ActivityDetailsDialog(
    activity: ActivityUiModel?,
    heroState: ActivityDisplayState?,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 380.dp)
                .shadow(
                    elevation = 26.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Gold.copy(alpha = 0.12f),
                    spotColor = Color.Black
                )
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF141414), Color(0xFF0C0C0C))
                    )
                )
                .border(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            Gold.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.10f)
                        )
                    ),
                    RoundedCornerShape(28.dp)
                )
                .padding(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF151515))
                    .border(1.dp, BorderSoft, CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(end = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = CircleShape,
                                ambientColor = Gold.copy(alpha = 0.16f),
                                spotColor = Color.Black
                            )
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Gold.copy(alpha = 0.22f),
                                        Color(0xFF181818)
                                    )
                                )
                            )
                            .border(1.dp, BorderSoft, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (activity != null) {
                            Icon(
                                imageVector = activity.icon,
                                contentDescription = null,
                                tint = Gold,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            TargetGlyphCompact()
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = activity?.name ?: heroState?.name ?: "No activity selected",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Activity details",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailRow(
                        label = "Mode",
                        value = if (activity?.isStopwatch == true || heroState?.isStopwatch == true) {
                            "Stopwatch"
                        } else {
                            "Timer"
                        }
                    )

                    DetailRow(
                        label = "Target",
                        value = if (activity?.isStopwatch == true || heroState?.isStopwatch == true) {
                            "No fixed target"
                        } else {
                            "${((heroState?.targetSeconds ?: activity?.targetSeconds ?: 0L) / 60L).toInt()} min"
                        }
                    )

                    DetailRow(
                        label = "Streak",
                        value = "${heroState?.streakDays ?: activity?.streakDays ?: 0} days"
                    )

                    DetailRow(
                        label = "Missed streak",
                        value = if (activity?.continueOnMiss == true) {
                            "Continue streak"
                        } else {
                            "Reset to zero"
                        }
                    )

                    DetailRow(
                        label = "Completion mark",
                        value = when {
                            activity?.completionStyle.equals("TIMEREND", true) -> "Auto-check on timer end"
                            activity?.completionStyle.equals("AUTO", true) -> "Auto-check on timer end"
                            else -> "Manual check"
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Close",
                            color = Gold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF151515), Color(0xFF101010))
                )
            )
            .border(1.dp, BorderSoft, RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = value,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
@Composable
private fun InfoChipCompact3d(
    label: String,
    value: String,
    accent: Color,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = accent.copy(alpha = 0.05f),
                spotColor = Color.Black
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF141414), Color(0xFF0C0C0C)))
            )
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.08f), Color.Black.copy(alpha = 0.20f))
                ),
                RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
       // horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(listOf(accent.copy(alpha = 0.12f), Color(0xFF111111)))
                )
                .border(1.dp, BorderSoft, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(6.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Text(
                text = label,
                color = TextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            Text(
                text = value,
                color = TextPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Visible,
                softWrap = false
            )
        }
    }
}


@Composable
private fun ControlBar(
    heroState: ActivityDisplayState?,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onSwitch: () -> Unit
) {
    val isRunning = heroState?.isRunning == true
    val isCompleted = heroState?.sessionState == ActivitySessionState.COMPLETED_TODAY

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Gold.copy(alpha = 0.05f),
                spotColor = Color.Black
            )
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF101010), Color(0xFF080808))
                )
            )
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.08f),
                        Color.Black.copy(alpha = 0.22f)
                    )
                ),
                RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            heroState == null -> {
                ActionButton(
                    label = "Start",
                    onClick = onStartFocus,
                    icon = { PlayGlyph() },
                    modifier = Modifier.weight(1f)
                )
            }

            isCompleted -> {
                ActionButton(
                    label = "Completed",
                    onClick = onStartFocus,
                    icon = { StatusGlyph(isCompleted = true) },
                    modifier = Modifier.weight(1f),
                    accent = Success
                )
            }

            isRunning -> {
                ActionButton(
                    label = "Pause",
                    onClick = onPause,
                    icon = { PauseGlyph() },
                    modifier = Modifier.weight(1f)
                )
                VerticalActionDivider()
                ActionButton(
                    label = "Finish",
                    onClick = onFinish,
                    icon = { FinishGlyph() },
                    modifier = Modifier.weight(1f)
                )
                VerticalActionDivider()
                ActionButton(
                    label = "Switch",
                    onClick = onSwitch,
                    icon = { SwitchGlyph() },
                    modifier = Modifier.weight(1f)
                )
            }

            else -> {
                ActionButton(
                    label = "Start",
                    onClick = onStartFocus,
                    icon = { PlayGlyph() },
                    modifier = Modifier.weight(1f)
                )
                VerticalActionDivider()
                ActionButton(
                    label = "Finish",
                    onClick = onFinish,
                    icon = { FinishGlyph() },
                    modifier = Modifier.weight(1f)
                )
                VerticalActionDivider()
                ActionButton(
                    label = "Switch",
                    onClick = onSwitch,
                    icon = { SwitchGlyph() },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun VerticalActionDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(34.dp)
            .background(Color.White.copy(alpha = 0.07f))
    )
}

@Composable
private fun ActionButton(
    label: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = Gold
) {
    Box(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = accent.copy(alpha = 0.08f),
                spotColor = Color.Black
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF161616),
                        Color(0xFF0C0C0C)
                    )
                )
            )
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.10f),
                        Color.Black.copy(alpha = 0.25f)
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        ambientColor = accent.copy(alpha = 0.10f),
                        spotColor = Color.Black
                    ),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Text(
                text = label,
                color = if (accent == Success) Success else TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DialCanvas(
    progress: Float,
    glowAlpha: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val outerR = size.minDimension / 2f
        val ringR = outerR * 0.80f
        val ringRect = Rect(
            left = cx - ringR,
            top = cy - ringR,
            right = cx + ringR,
            bottom = cy + ringR
        )

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF262626), Color(0xFF101010), Color(0xFF080808)),
                center = Offset(cx, cy),
                radius = outerR
            ),
            radius = outerR
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.05f),
            radius = outerR,
            style = Stroke(width = 1.5.dp.toPx())
        )

        drawCircle(
            color = Color.Black.copy(alpha = 0.28f),
            radius = outerR * 0.93f,
            style = Stroke(width = 9.dp.toPx())
        )

        val tickCount = 60
        repeat(tickCount) { i ->
            val angle = Math.toRadians((i * 360.0 / tickCount) - 90.0)
            val isMajor = i % 5 == 0
            val tickStart = ringR - if (isMajor) 15.dp.toPx() else 10.dp.toPx()
            val tickEnd = ringR - 4.dp.toPx()

            drawLine(
                color = if (isMajor) Gold.copy(alpha = 0.75f) else Color.White.copy(alpha = 0.12f),
                start = Offset(
                    x = cx + cos(angle).toFloat() * tickStart,
                    y = cy + sin(angle).toFloat() * tickStart
                ),
                end = Offset(
                    x = cx + cos(angle).toFloat() * tickEnd,
                    y = cy + sin(angle).toFloat() * tickEnd
                ),
                strokeWidth = if (isMajor) 1.8.dp.toPx() else 1.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        drawArc(
            color = Color.White.copy(alpha = 0.08f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = ringRect.topLeft,
            size = ringRect.size,
            style = Stroke(width = 11.dp.toPx(), cap = StrokeCap.Round)
        )

        val sweep = progress.coerceIn(0f, 1f) * 360f
        if (sweep > 0f) {
            drawArc(
                color = Gold.copy(alpha = glowAlpha * 0.24f),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = ringRect.topLeft,
                size = ringRect.size,
                style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(GoldDim, Gold, GoldSoft, Gold, GoldDim),
                    center = Offset(cx, cy)
                ),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = ringRect.topLeft,
                size = ringRect.size,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )

            val endAngle = Math.toRadians((sweep - 90f).toDouble())
            val knobX = cx + cos(endAngle).toFloat() * ringR
            val knobY = cy + sin(endAngle).toFloat() * ringR

            drawCircle(
                color = Gold.copy(alpha = 0.20f),
                radius = 18.dp.toPx(),
                center = Offset(knobX, knobY)
            )
            drawCircle(
                color = GoldSoft,
                radius = 8.dp.toPx(),
                center = Offset(knobX, knobY)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.65f),
                radius = 2.5.dp.toPx(),
                center = Offset(knobX - 2.dp.toPx(), knobY - 2.dp.toPx())
            )
        }

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFF171717), Color(0xFF0B0B0B)),
                center = Offset(cx, cy),
                radius = outerR * 0.63f
            ),
            radius = outerR * 0.63f
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.035f),
            radius = outerR * 0.63f,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

@Composable
private fun TargetGlyphCompact() {
    Canvas(modifier = Modifier.size(18.dp)) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val r = size.minDimension / 2f
        drawCircle(Gold, r, c, style = Stroke(1.8.dp.toPx()))
        drawCircle(Gold, r * 0.58f, c, style = Stroke(1.4.dp.toPx()))
        drawCircle(Gold, r * 0.18f, c)
    }
}

@Composable
private fun StreakGlyphCompact() {
    Canvas(modifier = Modifier.size(17.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.48f, 0f)
            cubicTo(
                size.width * 0.92f, size.height * 0.24f,
                size.width, size.height * 0.58f,
                size.width * 0.58f, size.height
            )
            cubicTo(
                size.width * 0.12f, size.height * 0.84f,
                size.width * 0.10f, size.height * 0.42f,
                size.width * 0.48f, 0f
            )
        }
        drawPath(path, Gold)
    }
}

@Composable
private fun StatusGlyph(isCompleted: Boolean) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val color = if (isCompleted) Success else Gold
        drawCircle(color, radius = size.minDimension / 2f, style = Stroke(2.dp.toPx()))
        val check = Path().apply {
            moveTo(size.width * 0.24f, size.height * 0.55f)
            lineTo(size.width * 0.43f, size.height * 0.74f)
            lineTo(size.width * 0.77f, size.height * 0.34f)
        }
        drawPath(
            path = check,
            color = color,
            style = Stroke(
                width = 2.2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
private fun PauseGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val barWidth = size.width * 0.18f
        val top = size.height * 0.16f
        val height = size.height * 0.68f

        drawRoundRect(
            color = Gold,
            topLeft = Offset(size.width * 0.20f, top),
            size = Size(barWidth, height),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        drawRoundRect(
            color = Gold,
            topLeft = Offset(size.width * 0.60f, top),
            size = Size(barWidth, height),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
    }
}

@Composable
private fun FinishGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        drawRoundRect(
            color = Gold,
            topLeft = Offset(size.width * 0.20f, size.height * 0.20f),
            size = Size(size.width * 0.60f, size.height * 0.60f),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
    }
}

@Composable
private fun SwitchGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)

        drawArc(
            color = Gold,
            startAngle = 35f,
            sweepAngle = 250f,
            useCenter = false,
            topLeft = Offset(size.width * 0.12f, size.height * 0.14f),
            size = Size(size.width * 0.70f, size.height * 0.70f),
            style = stroke
        )

        val path = Path().apply {
            moveTo(size.width * 0.74f, size.height * 0.18f)
            lineTo(size.width * 0.92f, size.height * 0.20f)
            lineTo(size.width * 0.82f, size.height * 0.35f)
        }
        drawPath(
            path = path,
            color = Gold,
            style = Stroke(
                2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
private fun PlayGlyph() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.28f, size.height * 0.18f)
            lineTo(size.width * 0.78f, size.height * 0.50f)
            lineTo(size.width * 0.28f, size.height * 0.82f)
            close()
        }
        drawPath(path, Gold)
    }
}