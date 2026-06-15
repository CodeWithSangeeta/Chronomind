package com.sangeeta.chronomind.ui.onboarding

enum class FocusArea(val icon: String, val label: String) {
    STUDY("📖", "Study"),
    EXERCISE("🏋️", "Exercise"),
    READING("📚", "Reading"),
    WORK("💻", "Work"),
    MEDITATION("🪷", "Meditation"),
    CREATIVE("🎨", "Creative Projects"),
    GROWTH("🌱", "Personal Growth"),
    OTHER("···", "Something Else")
}



enum class AccountabilityType(
    val icon: String,
    val title: String,
    val subtitle: String
) {
    STREAKS(
        "🔥", "Daily streaks",
        "Stay motivated with a daily streak counter"
    ),
    TIME(
        "⏱️", "Tracking my time",
        "See exactly how much time you invest each day"
    ),
    PROGRESS(
        "📊", "Seeing my progress",
        "Visual charts and progress over weeks"
    ),
    REMINDERS(
        "🔔", "Home screen reminders",
        "Widget nudges so you never forget"
    ),
    ALL(
        "🤍", "A little bit for everything",
        "Use all features together"
    )
}


enum class CheckInStyle(
    val icon: String,
    val title: String,
    val subtitle: String
) {
    MANUAL(
        "🤍",
        "I decide when I've completed today.",
        "(Manual check-in)"
    ),
    REMINDER(
        "🔔",
        "Remind me to check in.",
        "(Gentle reminders)"
    )
}


enum class StreakMissChoice(
    val icon: String,
    val title: String,
    val subtitle: String
) {
    CONTINUE(
        "🔄",
        "Continue my streak",
        "Keep the streak going where you left off."
    ),
    RESET(
        "⏮️",
        "Reset to zero",
        "Start fresh from 1."
    )
}

 enum class PreviewStatus { RUNNING, PAUSED, READY }