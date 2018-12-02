package de.sauroter.miniplan.data;

import android.arch.persistence.room.Room;
import android.content.Context;

public class AltarServicedatabaseAccessor {
    private static final String ALTAR_SERVICE_DB_TAG = "ALTAR_SERVICE_DB_TAG";

    private  static AltarServiceDatabase altarServiceDatabaseInstance;

    public static AltarServiceDatabase getInstance(final Context context) {
        if (altarServiceDatabaseInstance == null) {
            altarServiceDatabaseInstance = Room.databaseBuilder(context, AltarServiceDatabase.class, ALTAR_SERVICE_DB_TAG).build();
        }
        return altarServiceDatabaseInstance;
    }
}
