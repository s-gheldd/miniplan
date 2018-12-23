package de.sauroter.miniplan.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {AltarService.class, Event.class}, version = 1)
@TypeConverters({AltarServiceTypeConverters.class})
public abstract class AltarServiceDatabase extends RoomDatabase {

    public abstract AltarServiceDAO altarServicesDao();

    public abstract EventDao eventDao();
}
