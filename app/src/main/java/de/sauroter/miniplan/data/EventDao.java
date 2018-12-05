package de.sauroter.miniplan.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

@Dao
public interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvents(final List<Event> events);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(final Event event);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEvents(final Event event);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEvents(final List<Event> events);

    @Delete
    void deleteEvent(final Event event);

    @NonNull
    @Query("SELECT * FROM event ORDER BY date ASC")
    LiveData<List<Event>> loadAllEvents();

    @Query("DELETE FROM event WHERE date < :date")
    void deleteOlder(final Date date);
}
