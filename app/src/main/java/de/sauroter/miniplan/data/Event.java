package de.sauroter.miniplan.data;


import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(primaryKeys = {"date", "place"})
public class Event {
    @NonNull
    private Date date;

    @NonNull
    private String place;

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull final Date date) {
        this.date = date;
    }

    @NonNull
    public String getPlace() {
        return place;
    }

    public void setPlace(@NonNull final String place) {
        this.place = place;
    }
}
