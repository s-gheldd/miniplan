package de.sauroter.miniplan.data;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AltarServiceDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAltarServices(final List<AltarService> altarServices);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAltarService(final AltarService altarService);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAltarservice(final AltarService altarService);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAltarservices(final List<AltarService> altarServices);

    @Delete
    void deleteAltarService(final AltarService altarService);

    @NonNull
    @Query("SELECT * FROM altarservice ORDER BY date ASC")
    LiveData<List<AltarService>> loadAllAltarServices();

    @Nullable
    @Query("SELECT * FROM altarservice WHERE date = :date AND place = :place")
    AltarService findByDateAndPlace(final Date date, final String place);

    @NonNull
    @Query("SELECT * FROM altarservice WHERE duty")
    List<AltarService> loadAllDuty();

    @Query("DELETE FROM altarService WHERE date < :date")
    void deleteOlder(final Date date);
}
