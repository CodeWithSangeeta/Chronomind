package com.sangeeta.chronomind.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.model.ActivityDisplayState
import com.sangeeta.chronomind.ui.model.ActivitySessionState
import com.sangeeta.chronomind.ui.model.ActivityUiModel
import kotlin.math.min

internal val CardOuter = Color(0xFF070707)
internal val CardInner = Color(0xFF101010)
internal val SurfaceLow = Color(0xFF0C0C0C)
internal val BorderSoft = Color.White.copy(alpha = 0.07f)
internal val TextPrimary = Color(0xFFF5F2EA)
internal val TextSecondary = Color(0xFFB7B0A1)
internal val TextMuted = Color(0xFF8A857B)
internal val Gold = Color(0xFFFFC328)
internal val GoldSoft = Color(0xFFFFD76A)
internal val GoldDim = Color(0xFFD39A08)
internal val Success = Color(0xFF6ED38B)

@Composable
fun FocusTimerCard(
    heroState: ActivityDisplayState?,
    selectedActivity: ActivityUiModel?,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onSwitch: () -> Unit,
    onNoActivitySelected: () -> Unit = {},
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
            .background(Brush.verticalGradient(listOf(CardOuter, CardInner, SurfaceLow)))
            .border(1.dp, BorderSoft, RoundedCornerShape(34.dp))
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
                        colors = listOf(Gold.copy(alpha = glowAlpha * 0.16f), Color.Transparent),
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
                        .background(Brush.verticalGradient(listOf(Color(0xFF1B1B1B), Color(0xFF0C0C0C))))
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
                    modifier = Modifier.weight(2.05f).aspectRatio(1f).padding(1.dp),
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
                                    colors = listOf(Color(0xFF202020), Color(0xFF111111), Color(0xFF090909))
                                )
                            )
                            .border(
                                1.dp,
                                Brush.linearGradient(
                                    listOf(Color.White.copy(alpha = 0.10f), Color.Black.copy(alpha = 0.24f))
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
                        modifier = Modifier.matchParentSize().padding(38.dp),
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
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(
                                            Brush.verticalGradient(listOf(Color(0xFF161616), Color(0xFF0D0D0D)))
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
                                overflow = TextOverflow.Visible
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
                onSwitch = onSwitch,
                onNoActivitySelected = onNoActivitySelected
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
internal fun InfoChipCompact3d(
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
            .background(Brush.verticalGradient(listOf(Color(0xFF141414), Color(0xFF0C0C0C))))
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.08f), Color.Black.copy(alpha = 0.20f))
                ),
                RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(accent.copy(alpha = 0.12f), Color(0xFF111111))))
                .border(1.dp, BorderSoft, CircleShape),
            contentAlignment = Alignment.Center
        ) { icon() }

        Spacer(modifier = Modifier.width(6.dp))

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(text = label, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Medium, maxLines = 1)
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