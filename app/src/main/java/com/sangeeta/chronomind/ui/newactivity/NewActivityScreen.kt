package com.sangeeta.chronomind.ui.newactivity

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sangeeta.chronomind.ui.components.AuraCTAButton
import com.sangeeta.chronomind.ui.theme.AuraColors
import com.sangeeta.chronomind.ui.theme.AuraTypography

private val colorOptions = listOf(
    Color(0xFFFFCC00), // Yellow
    Color(0xFFFF6B35), // Orange
    Color(0xFFFF4F8B), // Pink
    Color(0xFF4ECDC4), // Teal
    Color(0xFF45B7D1), // Blue
    Color(0xFF96E6A1), // Green
    Color(0xFFBB6BD9), // Purple
    Color(0xFFFF8C94), // Coral
)

private val iconOptions = listOf(
    "📖" to "Study",
    "🏋️" to "Exercise",
    "🎨" to "Creative",
    "💻" to "Work",
    "🪷" to "Meditation",
    "📚" to "Reading",
    "🧠" to "Deep Work",
    "⚽" to "Sport"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewActivityScreen(onBack: () -> Unit) {
    var activityName     by remember { mutableStateOf("") }
    var selectedColor    by remember { mutableStateOf(colorOptions[0]) }
    var selectedIcon     by remember { mutableStateOf(iconOptions[0].first) }
    var motivation       by remember { mutableStateOf("") }
    var targetHours      by remember { mutableStateOf("1") }
    var targetMinutes    by remember { mutableStateOf("00") }
    var reminderEnabled  by remember { mutableStateOf(true) }

    var visible by remember { mutableStateOf(false) }
    val alpha   by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label         = "newAlpha"
    )
    LaunchedEffect(Unit) { visible = true }

    // Ambient glow from selected color
    val infiniteAnim = rememberInfiniteTransition(label = "newGlow")
    val glowAlpha    by infiniteAnim.animateFloat(
        initialValue  = 0.15f,
        targetValue   = 0.35f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorGlow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuraColors.BackgroundDark)
            .graphicsLayer(alpha = alpha)
    ) {
        // Top color ambient glow
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopCenter)
                .offset(y = 20.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            selectedColor.copy(alpha = glowAlpha * 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Top bar
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // X close
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AuraColors.SurfaceCard)
                        .border(1.dp, AuraColors.CardBorderDefault, CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✕", color = AuraColors.TextPrimary, style = AuraTypography.BodyMedium)
                }
                Text(
                    text  = "New Activity",
                    style = AuraTypography.TitleMedium.copy(fontWeight = FontWeight.Bold),
                    color = AuraColors.TextPrimary
                )
                // Save checkmark
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (activityName.isNotBlank()) AuraColors.YellowPrimary
                            else AuraColors.SurfaceCard
                        )
                        .clickable(enabled = activityName.isNotBlank(), onClick = { onBack() }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "✓",
                        color = if (activityName.isNotBlank()) AuraColors.TextOnYellow
                        else AuraColors.TextMuted,
                        style = AuraTypography.TitleMedium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Icon picker with 3D glow
                Column(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(
                                elevation    = 24.dp,
                                shape        = RoundedCornerShape(22.dp),
                                ambientColor = selectedColor.copy(alpha = 0.4f),
                                spotColor    = selectedColor.copy(alpha = 0.4f)
                            )
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        selectedColor.copy(alpha = 0.25f),
                                        AuraColors.SurfaceCard
                                    )
                                )
                            )
                            .border(
                                1.dp,
                                selectedColor.copy(alpha = 0.5f),
                                RoundedCornerShape(22.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = selectedIcon, fontSize = 36.sp)
                    }
                    Text(
                        text  = "Change Icon",
                        style = AuraTypography.BodySmall,
                        color = selectedColor
                    )
                }

                // Icon grid
                FormSection(title = "Choose Icon") {
                    LazyIconRow(
                        options  = iconOptions,
                        selected = selectedIcon,
                        onSelect = { selectedIcon = it }
                    )
                }

                // Activity name input
                FormSection(title = "Activity Name") {
                    SleekTextField(
                        value       = activityName,
                        onValueChange = { activityName = it },
                        placeholder = "e.g. Deep Work, Morning Yoga",
                        accentColor = selectedColor
                    )
                }

                // Color picker
                FormSection(title = "Color") {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        colorOptions.forEach { color ->
                            ColorDot(
                                color      = color,
                                isSelected = selectedColor == color,
                                onClick    = { selectedColor = color }
                            )
                        }
                    }
                }

                // Motivation
                FormSection(title = "Why does this matter to you?") {
                    SleekTextField(
                        value         = motivation,
                        onValueChange = { motivation = it },
                        placeholder   = "To stay healthy and have more energy every day.",
                        accentColor   = selectedColor,
                        minLines      = 3
                    )
                }

                // Target time
                FormSection(title = "Daily focus goal (optional)") {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        SleekTextField(
                            value         = targetHours,
                            onValueChange = { targetHours = it },
                            placeholder   = "0",
                            accentColor   = selectedColor,
                            modifier      = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Text("h", color = AuraColors.TextMuted, style = AuraTypography.BodyMedium)
                        SleekTextField(
                            value         = targetMinutes,
                            onValueChange = { targetMinutes = it },
                            placeholder   = "00",
                            accentColor   = selectedColor,
                            modifier      = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Text("m", color = AuraColors.TextMuted, style = AuraTypography.BodyMedium)
                    }
                }

                // Daily reminder toggle
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(AuraColors.SurfaceCard)
                        .border(1.dp, AuraColors.CardBorderDefault, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text  = "Daily reminder",
                            style = AuraTypography.TitleMedium,
                            color = AuraColors.TextPrimary
                        )
                        Text(
                            text  = "8:00 PM",
                            style = AuraTypography.BodySmall,
                            color = AuraColors.TextMuted
                        )
                    }
                    Switch(
                        checked         = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it },
                        colors          = SwitchDefaults.colors(
                            checkedThumbColor  = AuraColors.TextOnYellow,
                            checkedTrackColor  = AuraColors.YellowPrimary,
                            uncheckedThumbColor = AuraColors.TextMuted,
                            uncheckedTrackColor = AuraColors.SurfaceCardLight
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                AuraCTAButton(
                    text    = "Create Activity",
                    onClick = { if (activityName.isNotBlank()) onBack() },
                    enabled = activityName.isNotBlank()
                )

                // Danger zone
                Text(
                    text      = "Delete Activity",
                    style     = AuraTypography.BodyMedium.copy(fontWeight = FontWeight.Medium),
                    color     = Color(0xFFFF4444),
                    textAlign = TextAlign.Center,
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .clickable { onBack() }
                )
            }
        }
    }
}

@Composable
private fun FormSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text  = title,
            style = AuraTypography.BodySmall.copy(fontWeight = FontWeight.Medium),
            color = AuraColors.TextMuted
        )
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SleekTextField(
    value:           String,
    onValueChange:   (String) -> Unit,
    placeholder:     String,
    accentColor:     Color,
    modifier:        Modifier = Modifier,
    minLines:        Int      = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    BasicTextField(
        value          = value,
        onValueChange  = onValueChange,
        modifier       = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AuraColors.SurfaceCard)
            .border(
                width  = 1.dp,
                color  = if (value.isNotEmpty()) accentColor.copy(alpha = 0.5f)
                else AuraColors.CardBorderDefault,
                shape  = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        textStyle      = AuraTypography.BodyMedium.copy(color = AuraColors.TextPrimary),
        minLines       = minLines,
        keyboardOptions = keyboardOptions,
        decorationBox  = { inner ->
            if (value.isEmpty()) {
                Text(placeholder, color = AuraColors.TextMuted, style = AuraTypography.BodyMedium)
            }
            inner()
        }
    )
}

@Composable
private fun ColorDot(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 34.dp else 28.dp)
            .shadow(
                elevation    = if (isSelected) 12.dp else 0.dp,
                shape        = CircleShape,
                ambientColor = color,
                spotColor    = color
            )
            .clip(CircleShape)
            .background(color)
            .border(
                width  = if (isSelected) 2.dp else 0.dp,
                color  = Color.White.copy(alpha = 0.7f),
                shape  = CircleShape
            )
            .clickable(onClick = onClick)
    )
}

@Composable
private fun LazyIconRow(
    options:  List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEach { (icon, label) ->
            val isSelected = selected == icon
            Column(
                modifier            = Modifier
                    .width(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) AuraColors.SurfaceSelected
                        else AuraColors.SurfaceCard
                    )
                    .border(
                        1.dp,
                        if (isSelected) AuraColors.CardBorderSelected
                        else AuraColors.CardBorderDefault,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelect(icon) }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(icon, fontSize = 22.sp)
                Text(label, style = AuraTypography.BodySmall.copy(fontSize = 9.sp), color = AuraColors.TextMuted, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle.Default,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit
) {
    androidx.compose.foundation.text.BasicTextField(
        value          = value,
        onValueChange  = onValueChange,
        modifier       = modifier,
        textStyle      = textStyle,
        minLines       = minLines,
        keyboardOptions = keyboardOptions,
        decorationBox  = decorationBox
    )
}
