package com.meditrack.util;

import androidx.room.TypeConverter;
import java.time.LocalDateTime;

public class Converters {
    @TypeConverter
    public static LocalDateTime fromTimestamp(Long value) {
        return value == null ? null : LocalDateTime.ofEpochSecond(value, 0, java.time.ZoneOffset.UTC);
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDateTime date) {
        return date == null ? null : date.toEpochSecond(java.time.ZoneOffset.UTC);
    }
}
