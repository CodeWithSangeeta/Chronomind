package com.sangeeta.chronomind.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.model.ActivityDisplayState
import com.sangeeta.chronomind.ui.model.ActivitySessionState

@Composable
internal fun ControlBar(
    heroState: ActivityDisplayState?,
    onStartFocus: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onSwitch: () -> Unit,
    onNoActivitySelected: () -> Unit = {}
) {
    val isRunning = heroState?.isRunning == true
    val isCompleted = heroState?.sessionState == ActivitySessionState.COMPLETED_TODAY
    val isPaused = heroState?.sessionState == ActivitySessionState.PENDING

    val canFinish = isRunning || isPaused

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
            .background(Brush.verticalGradient(listOf(Color(0xFF101010), Color(0xFF080808))))
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.08f), Color.Black.copy(alpha = 0.22f))
                ),
                RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            heroState == null -> {
                // No activity exists at all — starting should prompt the
                // user to create/select one rather than silently no-op.
                ActionButton(
                    label = "Start",
                    onClick = onNoActivitySelected,
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
                ActionButton(label = "Pause", onClick = onPause, icon = { PauseGlyph() }, modifier = Modifier.weight(1f))
                VerticalActionDivider()
                ActionButton(label = "Finish", onClick = onFinish, icon = { FinishGlyph() }, modifier = Modifier.weight(1f))
                VerticalActionDivider()
                ActionButton(label = "Switch", onClick = onSwitch, icon = { SwitchGlyph() }, modifier = Modifier.weight(1f))
            }

            isPaused -> {
                ActionButton(label = "Start", onClick = onStartFocus, icon = { PlayGlyph() }, modifier = Modifier.weight(1f))
                VerticalActionDivider()
                ActionButton(label = "Finish", onClick = onFinish, icon = { FinishGlyph() }, modifier = Modifier.weight(1f))
                VerticalActionDivider()
                ActionButton(label = "Switch", onClick = onSwitch, icon = { SwitchGlyph() }, modifier = Modifier.weight(1f))
            }

            else -> {
                // IDLE — activity selected but never started. No Finish here.
                ActionButton(label = "Start", onClick = onStartFocus, icon = { PlayGlyph() }, modifier = Modifier.weight(1f))
                VerticalActionDivider()
                ActionButton(label = "Switch", onClick = onSwitch, icon = { SwitchGlyph() }, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
internal fun VerticalActionDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(34.dp)
            .background(Color.White.copy(alpha = 0.07f))
    )
}

@Composable
internal fun ActionButton(
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
            .background(Brush.verticalGradient(listOf(Color(0xFF161616), Color(0xFF0C0C0C))))
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.10f), Color.Black.copy(alpha = 0.25f))
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
            ) { icon() }

            Text(
                text = label,
                color = if (accent == Success) Success else TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}