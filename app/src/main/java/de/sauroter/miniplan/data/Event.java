package de.sauroter.miniplan.data;


import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;

@Entity(primaryKeys = {"date"})
public class Event {
    @NonNull
    private Date date;

    @Nullable
    private String info;

    public Event(@NonNull final Date date,
                 @Nullable final String info) {
        this.date = date;
        this.info = info;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull final Date date) {
        this.date = date;
    }

    @Nullable
    public String getInfo() {
        return info;
    }

    public void setInfo(@NonNull final String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", info='" + info + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Event event = (Event) o;
        return Objects.equals(date, event.date) &&
                Objects.equals(info, event.info);
    }

    @Override
    public int hashCode() {

        return Objects.hash(date, info);
    }
}
