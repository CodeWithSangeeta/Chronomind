package com.sangeeta.chronomind.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

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
        fun fromName(name: String): ActivityIconOption {
            return entries.find { it.name == name } ?: GROWTH
        }
        
        fun toName(icon: ImageVector): String {
            return entries.find { it.icon == icon }?.name ?: GROWTH.name
        }
    }
}



    enum class ActivityColorOption(val hex: String, val color: Color) {
        AMBER("FFC328", Color(0xFFFFC328)),
        ORANGE("FF6B35", Color(0xFFFF6B35)),
        RED("FF4444", Color(0xFFFF4444)),
        GREEN("22C55E", Color(0xFF22C55E)),
        BLUE("3B82F6", Color(0xFF3B82F6)),
        PURPLE("A855F7", Color(0xFFA855F7)),
        PINK("FF69B4", Color(0xFFFF69B4)),
        CYAN("00BCD4", Color(0xFF00BCD4)),
        LIME("8BC34A", Color(0xFF8BC34A)),
        TEAL("009688", Color(0xFF009688)),
        INDIGO("3F51B5", Color(0xFF3F51B5)),
        DEEP_PURPLE("673AB7", Color(0xFF673AB7)),
        LIGHT_BLUE("03A9F4", Color(0xFF03A9F4)),
        MINT("2ECC71", Color(0xFF2ECC71)),
        CORAL("FF7F50", Color(0xFFFF7F50)),
        ROSE("E91E63", Color(0xFFE91E63)),
        GOLD("FFD700", Color(0xFFFFD700)),
        BROWN("795548", Color(0xFF795548)),
        SLATE("607D8B", Color(0xFF607D8B)),
        MAGENTA("C2185B", Color(0xFFC2185B));

        companion object {
            fun fromHex(hex: String): ActivityColorOption {
                val normalized = hex.removePrefix("#").uppercase()
                return entries.find { it.hex.uppercase() == normalized } ?: AMBER
            }
        }

    }

