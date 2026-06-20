package com.sangeeta.chronomind.ui.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material.icons.rounded.Work
import androidx.compose.ui.graphics.vector.ImageVector

enum class FocusArea(
    val icon: ImageVector,
    val label: String
) {
    STUDY(Icons.Rounded.Book, "Study"),
    EXERCISE(Icons.Rounded.FitnessCenter, "Exercise"),
    READING(Icons.Rounded.Search, "Research"),
    WORK(Icons.Rounded.Work, "Work"),
    MEDITATION(Icons.Rounded.SelfImprovement, "Meditation"),
    CREATIVE(Icons.Rounded.Palette, "Creative work"),
    GROWTH(Icons.Rounded.TrendingUp, "Personal growth"),
    OTHER(Icons.Rounded.MoreHoriz, "Other")
}

enum class AccountabilityType(
    val icon: ImageVector,
    val title: String,
) {
    STREAKS(
        Icons.Rounded.LocalFireDepartment,
        "Daily streaks"
    ),
    TIME(
        Icons.Rounded.Timer,
        "Time tracking"
    ),
    PROGRESS(
        Icons.Rounded.BarChart,
        "Progress tracking"
    ),
    REMINDERS(
        Icons.Rounded.Widgets,
        "Home screen widget"
    )
}

enum class CheckInStyle(
    val icon: ImageVector,
    val title: String,
    val subtitle: String
) {
    MANUAL(
        Icons.Rounded.TaskAlt,
        "I’ll mark my day complete",
        "Manual check-in"
    ),
    REMINDER(
        Icons.Rounded.Notifications,
        "Remind me to check in",
        "Gentle reminders"
    )
}

enum class StreakMissChoice(
    val icon: ImageVector,
    val title: String,
    val subtitle: String
) {
    CONTINUE(
        Icons.Rounded.Autorenew,
        "Continue my streak",
        "Pick up where I left off"
    ),
    RESET(
        Icons.Rounded.Replay,
        "Reset to zero",
        "Start over from 1"
    )
}

enum class PreviewStatus {
    RUNNING,
    PAUSED,
    READY
}