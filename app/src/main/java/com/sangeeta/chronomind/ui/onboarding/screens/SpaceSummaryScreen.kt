package com.sangeeta.chronomind.ui.onboarding.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.components.AuraBotBubble
import com.sangeeta.chronomind.ui.onboarding.OnboardingScaffold
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun SpaceSummaryScreen(
    focusAreas: List<String>,
    accountabilityLabel: List<String>,
    checkInLabel: String,
    streakMissLabel: String,
    onCreateSpace: () -> Unit,
    currentStep: Int = 6,
    totalSteps: Int = 7,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600),
        label = "summaryAlpha"
    )
    val slideY by animateDpAsState(
        targetValue = if (visible) 0.dp else 24.dp,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "summarySlide"
    )

    LaunchedEffect(Unit) { visible = true }

    OnboardingScaffold(
        buttonText = "Create My Space",
        onButtonClick = onCreateSpace,
        currentStep = currentStep,
        totalSteps = totalSteps,
        topContent = {
            AuraBotBubble(
                message = "Your setup is ready.",
                botImageSize = 160.dp
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = alpha)
                .offset(y = slideY)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 14.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = AuraColors.YellowGlow,
                        spotColor = AuraColors.YellowGlow
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AuraColors.SurfaceCardLight,
                                AuraColors.SurfaceCard
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = AuraColors.CardBorderDefault,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(AuraColors.YellowPrimary.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = AuraColors.YellowPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Your Space Summary",
                                fontSize = 18.sp,
                                style = AuraTypography.TitleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = AuraColors.TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Review your setup before creating your space.",
                                style = AuraTypography.BodySmall,
                                color = AuraColors.TextMuted
                            )
                        }
                    }

                    SummarySection(
                        icon = Icons.Rounded.TrackChanges,
                        label = "Focus areas"
                    ) {
                        SummaryChipGroup(items = focusAreas)
                    }

                    SummarySection(
                        icon = Icons.Rounded.LocalFireDepartment,
                        label = "Staying on track"
                    ) {
                        SummaryChipGroup(items = accountabilityLabel)
                    }

                    SummarySection(
                        icon = Icons.Rounded.CheckCircle,
                        label = "Daily check-in"
                    ) {
                        SummaryValueCard(value = checkInLabel)
                    }

                    SummarySection(
                        icon = Icons.Rounded.Replay,
                        label = "Missed-day streak rule"
                    ) {
                        SummaryValueCard(value = streakMissLabel)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}



@Composable
private fun SummarySection(
    icon: ImageVector,
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(AuraColors.SurfaceSelected),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AuraColors.YellowPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = label,
                fontSize = 16.sp,
                style = AuraTypography.LabelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = AuraColors.TextMuted
            )
        }
        content()
    }
}

@Composable
private fun SummaryChipGroup(
    items: List<String>
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(AuraColors.SurfaceSelected)
                    .border(
                        width = 1.dp,
                        color = AuraColors.CardBorderSelected,
                        shape = RoundedCornerShape(999.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text = item,
                    style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
                    color = AuraColors.TextPrimary
                )
            }
        }
    }
}



@Composable
private fun SummaryValueCard(
    value: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(AuraColors.SurfaceSelected.copy(alpha = 0.65f))
            .border(
                width = 1.dp,
                color = AuraColors.CardBorderDefault,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = value,
            style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.Medium),
            color = AuraColors.TextPrimary
        )
    }
}