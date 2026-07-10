package com.sangeeta.chronomind.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
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
import androidx.compose.ui.unit.dp
import com.sangeeta.chronomind.R
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

@Composable
fun FaqCard() {
    var expandedQ1 by remember { mutableStateOf(false) }
    var expandedQ2 by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    com.sangeeta.chronomind.ui.create_activity.FormSectionCard(title = "FAQ") {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // FAQ Question 1
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AuraColors.BackgroundDark)
                    .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(14.dp))
                    .clickable { expandedQ1 = !expandedQ1 }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = context.getString(R.string.faq_q1),
                        style = AuraTypography.TitleMedium,
                        color = AuraColors.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expandedQ1) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        contentDescription = null,
                        tint = AuraColors.YellowPrimary
                    )
                }
                if (expandedQ1) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = context.getString(R.string.faq_a1),
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary
                    )
                }
            }

            // FAQ Question 2
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AuraColors.BackgroundDark)
                    .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(14.dp))
                    .clickable { expandedQ2 = !expandedQ2 }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = context.getString(R.string.faq_q2),
                        style = AuraTypography.TitleMedium,
                        color = AuraColors.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expandedQ2) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        contentDescription = null,
                        tint = AuraColors.YellowPrimary
                    )
                }
                if (expandedQ2) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = context.getString(R.string.faq_a2),
                        style = AuraTypography.BodyMedium,
                        color = AuraColors.TextSecondary
                    )
                }
            }
        }
    }
}