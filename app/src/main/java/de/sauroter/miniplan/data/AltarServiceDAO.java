package de.sauroter.miniplan.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

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
}
