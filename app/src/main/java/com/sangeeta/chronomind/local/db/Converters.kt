package com.sangeeta.chronomind.local.db

import androidx.room.TypeConverter
import androidx.compose.ui.graphics.vector.ImageVector
import com.sangeeta.chronomind.ui.model.ActivityIconOption

class Converters {
    @TypeConverter
    fun fromImageVector(icon: ImageVector): String {
        // Find the enum entry name that matches this icon for storage
        return ActivityIconOption.toName(icon)
    }

    @TypeConverter
    fun toImageVector(name: String): ImageVector {
        // Map the stored string back to the ImageVector using the helper
        return ActivityIconOption.fromName(name).icon
    }
}
