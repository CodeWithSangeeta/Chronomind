package com.sangeeta.chronomind.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography


@Composable
 fun SettingsTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(AuraColors.SurfaceCard)
                .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = AuraColors.TextPrimary
            )
        }
        Text(
            text = "Settings",
            style = AuraTypography.DisplayMedium,
            color = AuraColors.TextPrimary
        )

    }
}

@Composable
 fun SettingsSectionCard(
    title: String,
    footerText: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
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
            .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(22.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = AuraTypography.LabelMedium,
            color = AuraColors.TextMuted
        )

        content()

        if (!footerText.isNullOrBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = AuraColors.TextMuted,
                    modifier = Modifier.size(14.dp)
                )

                Text(
                    text = footerText,
                    fontSize = 10.sp,
                    style = AuraTypography.BodySmall.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = AuraColors.TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
 fun SettingsRow(
    item: SettingsRowUiModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !item.isValueOnly, onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        RowIcon(icon = item.icon)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = item.title,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.subtitle,
                fontSize = 12.sp,
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        when {
            item.isValueOnly && item.value != null -> {
                Text(
                    text = item.value,
                    style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AuraColors.YellowPrimary
                )
            }

            item.value != null -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.value,
                        style = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = AuraColors.TextSecondary
                    )
                    Icon(
                        imageVector = if (item.isExternal) Icons.Rounded.OpenInNew else Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = AuraColors.TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            else -> {
                Icon(
                    imageVector = if (item.isExternal) Icons.Rounded.OpenInNew else Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = AuraColors.TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}



@Composable
fun RowIcon(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(AuraColors.YellowPrimary.copy(alpha = 0.10f))
            .border(1.dp, AuraColors.YellowPrimary.copy(alpha = 0.22f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AuraColors.YellowPrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}



@Composable
fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.05f))
    )
}



@Composable
fun ConfirmDialog(
    title: String,
    body: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AuraColors.SurfaceCard,
        title = {
            Text(
                text = title,
                style = AuraTypography.TitleMedium,
                color = AuraColors.TextPrimary
            )
        },
        text = {
            Text(
                text = body,
                style = AuraTypography.BodyMedium,
                color = AuraColors.TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = AuraColors.YellowPrimary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = AuraColors.TextSecondary
                )
            }
        }
    )
}
