package de.sauroter.miniplan.data;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
