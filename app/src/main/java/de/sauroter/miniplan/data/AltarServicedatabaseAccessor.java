package de.sauroter.miniplan.data;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Room;

public class AltarServicedatabaseAccessor {
    private static final String ALTAR_SERVICE_DB_TAG = "ALTAR_SERVICE_DB_TAG";

    private static AltarServiceDatabase altarServiceDatabaseInstance;

    public static AltarServiceDatabase getInstance(final Context context) {
        if (altarServiceDatabaseInstance == null) {
            altarServiceDatabaseInstance = Room.databaseBuilder(context, AltarServiceDatabase.class, ALTAR_SERVICE_DB_TAG).build();
        }
        return altarServiceDatabaseInstance;
    }

    @VisibleForTesting
    public static void setAltarServiceDatabaseInstance(final AltarServiceDatabase altarServiceDatabase) {
        altarServiceDatabaseInstance = altarServiceDatabase;
    }
}
