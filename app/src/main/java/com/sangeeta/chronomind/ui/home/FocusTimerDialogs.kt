package com.sangeeta.chronomind.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sangeeta.chronomind.ui.model.ActivityDisplayState
import com.sangeeta.chronomind.ui.model.ActivityUiModel

@Composable
internal fun ActivityDetailsDialog(
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
                .background(Brush.verticalGradient(listOf(Color(0xFF141414), Color(0xFF0C0C0C))))
                .border(
                    1.dp,
                    Brush.linearGradient(listOf(Gold.copy(alpha = 0.28f), Color.White.copy(alpha = 0.10f))),
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
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close", tint = TextSecondary, modifier = Modifier.size(18.dp))
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                            .background(Brush.radialGradient(listOf(Gold.copy(alpha = 0.22f), Color(0xFF181818))))
                            .border(1.dp, BorderSoft, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (activity != null) {
                            Icon(imageVector = activity.icon, contentDescription = null, tint = Gold, modifier = Modifier.size(24.dp))
                        } else {
                            TargetGlyphCompact()
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = activity?.name ?: heroState?.name ?: "No activity selected",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(text = "Activity details", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow(
                        label = "Mode",
                        value = if (activity?.isStopwatch == true || heroState?.isStopwatch == true) "Stopwatch" else "Timer"
                    )
                    DetailRow(
                        label = "Target",
                        value = if (activity?.isStopwatch == true || heroState?.isStopwatch == true) {
                            "No fixed target"
                        } else {
                            "${((heroState?.targetSeconds ?: activity?.targetSeconds ?: 0L) / 60L)} min"
                        }
                    )
                    DetailRow(label = "Streak", value = "${heroState?.streakDays ?: activity?.streakDays ?: 0} days")
                    DetailRow(
                        label = "Missed streak",
                        value = if (activity?.continueOnMiss == true) "Continue streak" else "Reset to zero"
                    )
                    DetailRow(
                        label = "Completion mark",
                        value = when {
                            activity?.completionStyle.equals("TIMER_END", true) -> "Auto-check on timer end"
                            activity?.completionStyle.equals("AUTO", true) -> "Auto-check on timer end"
                            else -> "Manual check"
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF151515), Color(0xFF101010))))
            .border(1.dp, BorderSoft, RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun FinishSessionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 360.dp)
                .shadow(
                    elevation = 26.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Gold.copy(alpha = 0.12f),
                    spotColor = Color.Black
                )
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF141414), Color(0xFF0C0C0C))))
                .border(
                    1.dp,
                    Brush.linearGradient(listOf(Gold.copy(alpha = 0.28f), Color.White.copy(alpha = 0.10f))),
                    RoundedCornerShape(28.dp)
                )
                .padding(22.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(Gold.copy(alpha = 0.20f), Color(0xFF181818))))
                        .border(1.dp, Gold.copy(alpha = 0.30f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null, tint = Gold, modifier = Modifier.size(26.dp))
                }

                Text(
                    text = "Mark as complete?",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "This will finish today's session. You won't be able to run this activity again today.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1B1B1B))
                            .border(1.dp, BorderSoft, RoundedCornerShape(16.dp))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onDismiss() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Cancel", color = TextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.verticalGradient(listOf(GoldSoft, GoldDim)))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onConfirm() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Mark Complete", color = Color(0xFF1A1200), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}