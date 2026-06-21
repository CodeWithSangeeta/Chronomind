package com.sangeeta.chronomind.ui.create_activity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Mood
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CreateEditUiState(
    val mode: CreateEditMode = CreateEditMode.CREATE,
    val isLoading: Boolean = false,
    val activityId: Int? = null,
    val activityName: String = "",
    val selectedIcon: ActivityIconOption = ActivityIconOption.CREATIVE,
    val selectedColor: ActivityColorOption = ActivityColorOption.AMBER,
    val targetType: TargetType = TargetType.TIME,
    val targetHours: Int = 1,
    val targetMinutes: Int = 30,
    val targetCount: String = "",
    val targetUnit: String = "",
    val reminderEnabled: Boolean = true,
    val reminderTime: String = "07:00 PM",
    val advancedExpanded: Boolean = true,
    val streakBehavior: StreakBehavior = StreakBehavior.CONTINUE,
    val completionStyle: CompletionStyle = CompletionStyle.MANUAL,
    val showDeleteConfirm: Boolean = false
) {
    val screenTitle: String
        get() = if (mode == CreateEditMode.CREATE) "New Activity" else "Edit Activity"

    val primaryButtonText: String
        get() = if (mode == CreateEditMode.CREATE) "Create Activity" else "Save Changes"

    val showDeleteAction: Boolean
        get() = mode == CreateEditMode.EDIT

    val canSubmit: Boolean
        get() = activityName.trim().isNotEmpty()
}

enum class CreateEditMode {
    CREATE,
    EDIT
}

enum class TargetType {
    TIME,
    COUNT
}

enum class StreakBehavior(val label: String) {
    CONTINUE("Continue streak"),
    RESET("Reset streak")
}

enum class CompletionStyle(val label: String) {
    MANUAL("Manual complete"),
    TIMER_END("Complete on timer end"),
    COUNT_CHECK("Mark when count reached")
}

enum class ActivityIconOption(
    val label: String,
    val icon: ImageVector,
    val emoji: String
) {
    CREATIVE("Creative", Icons.Rounded.Brush, "🎨"),
    STUDY("Study", Icons.Rounded.Book, "📘"),
    CODE("Code", Icons.Rounded.Code, "💻"),
    WORKOUT("Workout", Icons.Rounded.FitnessCenter, "🏋️"),
    MEDITATION("Meditation", Icons.Rounded.Mood, "🧘"),
    WRITING("Writing", Icons.Rounded.Edit, "✍️"),
    GROWTH("Growth", Icons.Rounded.BarChart, "📈"),
    WORK("Work", Icons.Rounded.Work, "💼")
}

enum class ActivityColorOption(
    val label: String,
    val color: Color
) {
    AMBER("Amber", Color(0xFFF6C445)),
    ORANGE("Orange", Color(0xFFF08A13)),
    RED("Red", Color(0xFFE04B3F)),
    PINK("Pink", Color(0xFFD93B8A)),
    PURPLE("Purple", Color(0xFF8D49D7)),
    BLUE("Blue", Color(0xFF2F7DE1)),
    CYAN("Cyan", Color(0xFF24AFC8)),
    GREEN("Green", Color(0xFF4AA84F))
}