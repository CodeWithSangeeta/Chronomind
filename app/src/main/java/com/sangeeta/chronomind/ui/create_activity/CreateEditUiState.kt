package com.sangeeta.chronomind.ui.create_activity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.sangeeta.chronomind.local.db.entity.ActivityEntity

enum class CreateEditMode { CREATE, EDIT }

enum class TargetType(val label: String) {
    TIMER("Timer"),
    STOPWATCH("Stopwatch")
}

enum class ActivityIconOption(val icon: ImageVector) {
    STUDY(Icons.AutoMirrored.Rounded.MenuBook),
    EXERCISE(Icons.Rounded.FitnessCenter),
    READING(Icons.Rounded.Search),
    WORK(Icons.Rounded.Work),
    MEDITATION(Icons.Rounded.SelfImprovement),
    CREATIVE(Icons.Rounded.Palette),
    GROWTH(Icons.AutoMirrored.Rounded.TrendingUp),
    WRITING(Icons.Rounded.Edit),
    CODING(Icons.Rounded.Code),
    MUSIC(Icons.Rounded.Headphones),
    WALKING(Icons.AutoMirrored.Rounded.DirectionsWalk),
    RUNNING(Icons.AutoMirrored.Rounded.DirectionsRun),
    PLANNING(Icons.AutoMirrored.Rounded.EventNote),
    MEETING(Icons.Rounded.Groups),
    CALL(Icons.Rounded.Call),
    JOURNAL(Icons.AutoMirrored.Rounded.MenuBook),
    COOKING(Icons.Rounded.Restaurant),
    CLEANING(Icons.Rounded.CleaningServices),
    SLEEP(Icons.Rounded.Bedtime),
    SHOPPING(Icons.Rounded.ShoppingCart),
    OTHER(Icons.Rounded.MoreHoriz);

    companion object {
        /**
         * Resolves an icon option from its string name (used when loading from DB).
         */
        fun fromName(name: String): ActivityIconOption {
            return entries.find { it.name == name } ?: GROWTH
        }

        /**
         * Resolves an icon option from an ImageVector (used for UI state mapping).
         */
        fun fromIcon(icon: ImageVector): ActivityIconOption {
            return entries.find { it.icon == icon } ?: GROWTH
        }
    }
}

enum class ActivityColorOption(val hex: String, val color: androidx.compose.ui.graphics.Color) {
    AMBER("FFC328", Color(0xFFFFC328)),
    ORANGE("FF6B35", Color(0xFFFF6B35)),
    RED("FF4444", Color(0xFFFF4444)),
    PINK("FF69B4", Color(0xFFFF69B4)),
    PURPLE("8E55EA", Color(0xFF8E55EA)),
    BLUE("2196F3", Color(0xFF2196F3)),
    CYAN("00BCD4", Color(0xFF00BCD4)),
    GREEN("4CAF50", Color(0xFF4CAF50)),
    TEAL("009688", Color(0xFF009688)),
    LIME("8BC34A", Color(0xFF8BC34A)),
    INDIGO("3F51B5", Color(0xFF3F51B5)),
    DEEP_PURPLE("673AB7", Color(0xFF673AB7)),
    LIGHT_BLUE("03A9F4", Color(0xFF03A9F4)),
    MINT("2ECC71", Color(0xFF2ECC71)),
    CORAL("FF7F50", Color(0xFFFF7F50)),
    ROSE("E91E63", Color(0xFFE91E63)),
    GOLD("FFD700", Color(0xFFFFD700)),
    BROWN("795548", Color(0xFF795548)),
    SLATE("607D8B", Color(0xFF607D8B)),
    MAGENTA("C2185B", Color(0xFFC2185B)),
}

enum class StreakBehavior(val label: String, val description: String) {
    CONTINUE("Continue streak on miss", "Streak never resets, even if you skip a day"),
    RESET("Reset streak on miss", "Streak resets to 0 if you miss a day")
}

enum class CompletionStyle(val label: String, val description: String) {
    MANUAL("Manual", "You tap Finish when done"),
    TIMER_END("Auto (when timer ends)", "Session completes automatically when target is reached")
}

data class CreateEditUiState(
    val mode: CreateEditMode = CreateEditMode.CREATE,
    val activityId: Int = 0,
    val screenTitle: String = "New Activity",

    val activityName: String = "",
    val selectedIcon: ActivityIconOption = ActivityIconOption.GROWTH,
    val selectedColor: ActivityColorOption = ActivityColorOption.AMBER,


    val targetType: TargetType = TargetType.TIMER,
    val targetHours: Int = 0,
    val targetMinutes: Int = 0,

    val reminderEnabled: Boolean = false,
    val reminderTime: String = "07:00 PM",


    val isAdvancedExpanded: Boolean = false,
    val streakBehavior: StreakBehavior = StreakBehavior.CONTINUE,
    val completionStyle: CompletionStyle = CompletionStyle.MANUAL,

    val isSaving: Boolean = false,

    val showDeleteConfirm: Boolean = false,
) {
    val isValid: Boolean
        get() = activityName.isNotBlank() &&
                (targetType == TargetType.STOPWATCH || targetHours > 0 || targetMinutes > 0)

    val totalTargetMinutes: Int
        get() = targetHours * 60 + targetMinutes

    fun toEntity(existingEntity: ActivityEntity? = null): ActivityEntity {
        val base = existingEntity ?: ActivityEntity(
            name = activityName,
            icon = selectedIcon.icon,
            targetMinutes = 0
        )
        return base.copy(
            name = activityName.trim(),
            icon = selectedIcon.icon,
            colorHex = selectedColor.hex,
            targetMinutes = if (targetType == TargetType.STOPWATCH) 0 else totalTargetMinutes,
            targetType = targetType.name,
            completionStyle = if (targetType == TargetType.STOPWATCH) "MANUAL" else completionStyle.name,
            continueStreakOnMiss = streakBehavior == StreakBehavior.CONTINUE,
            reminderEnabled = reminderEnabled,
            reminderTime = reminderTime,
            elapsedSeconds = existingEntity?.elapsedSeconds ?: 0L,
            isRunning = existingEntity?.isRunning ?: false,
            streakDays = existingEntity?.streakDays ?: 0,
            lastActiveDate = existingEntity?.lastActiveDate ?: "",
            orderIndex = existingEntity?.orderIndex ?: 0,
        )
    }
}
