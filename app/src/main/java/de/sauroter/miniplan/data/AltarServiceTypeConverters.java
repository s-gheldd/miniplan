package de.sauroter.miniplan.data;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

public class AltarServiceTypeConverters {

    @Nullable
    @TypeConverter
    public static Date dateFromTimeStamp(@Nullable final Long value) {
        return value == null ? null : new Date(value);
    }

    @Nullable
    @TypeConverter
    public static Long dateToTimestamp(@Nullable final Date date) {
        return date == null ? null : date.getTime();
    }
}