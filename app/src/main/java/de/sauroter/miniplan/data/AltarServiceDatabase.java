package de.sauroter.miniplan.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {AltarService.class, Event.class}, version = 1)
@TypeConverters({AltarServiceTypeConverters.class})
public abstract class AltarServiceDatabase extends RoomDatabase {

    public abstract AltarServiceDAO altarServicesDao();

    public abstract EventDao eventDao();
}
