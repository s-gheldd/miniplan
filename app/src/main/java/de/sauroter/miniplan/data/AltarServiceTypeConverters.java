package de.sauroter.miniplan.data;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import java.util.Date;

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