package com.sangeeta.chronomind.local.db

import androidx.room.TypeConverter
import androidx.compose.ui.graphics.vector.ImageVector
import com.sangeeta.chronomind.ui.model.ActivityIconOption

class Converters {
    @TypeConverter
    fun fromImageVector(icon: ImageVector): String {
        return ActivityIconOption.toName(icon)
    }

    @TypeConverter
    fun toImageVector(name: String): ImageVector {
        return ActivityIconOption.fromName(name).icon
    }
}
