package de.sauroter.miniplan.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

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
}
