package de.sauroter.miniplan.data;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

@Entity(primaryKeys = {"date", "place"})
public class AltarService {

    @NonNull
    private Date date;

    @NonNull
    private String place;

    @Nullable
    private String additionalInformation;

    @Nullable
    private String lector;

    @Nullable
    private String lightOne;

    @Nullable
    private String lightTwo;

    @Nullable
    private String altarOne;

    @Nullable
    private String altarTwo;

    private boolean duty;

    public AltarService(final @NonNull Date date,
                        final @NonNull String place,
                        final @Nullable String additionalInformation,
                        final @Nullable String lector,
                        final @Nullable String lightOne,
                        final @Nullable String lightTwo,
                        final @Nullable String altarOne,
                        final @Nullable String altarTwo,
                        final boolean duty) {
        this.date = date;
        this.place = place;
        this.additionalInformation = additionalInformation;
        this.lector = lector;
        this.lightOne = lightOne;
        this.lightTwo = lightTwo;
        this.altarOne = altarOne;
        this.altarTwo = altarTwo;
        this.duty = duty;
    }

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

    @Nullable
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(@Nullable final String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Nullable
    public String getLector() {
        return lector;
    }

    public void setLector(@Nullable final String lector) {
        this.lector = lector;
    }

    @Nullable
    public String getLightOne() {
        return lightOne;
    }

    public void setLightOne(@Nullable final String lightOne) {
        this.lightOne = lightOne;
    }

    @Nullable
    public String getLightTwo() {
        return lightTwo;
    }

    public void setLightTwo(@Nullable final String lightTwo) {
        this.lightTwo = lightTwo;
    }

    @Nullable
    public String getAltarOne() {
        return altarOne;
    }

    public void setAltarOne(@Nullable final String altarOne) {
        this.altarOne = altarOne;
    }

    @Nullable
    public String getAltarTwo() {
        return altarTwo;
    }

    public void setAltarTwo(@Nullable final String altarTwo) {
        this.altarTwo = altarTwo;
    }

    public boolean isDuty() {
        return duty;
    }

    public void setDuty(final boolean duty) {
        this.duty = duty;
    }

    @Override
    public String toString() {
        return "AltarService{" +
                "date=" + date +
                ", place='" + place + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", lector='" + lector + '\'' +
                ", lightOne='" + lightOne + '\'' +
                ", lightTwo='" + lightTwo + '\'' +
                ", altarOne='" + altarOne + '\'' +
                ", altarTwo='" + altarTwo + '\'' +
                ", duty=" + duty +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AltarService that = (AltarService) o;
        return duty == that.duty &&
                Objects.equals(date, that.date) &&
                Objects.equals(place, that.place) &&
                Objects.equals(additionalInformation, that.additionalInformation) &&
                Objects.equals(lector, that.lector) &&
                Objects.equals(lightOne, that.lightOne) &&
                Objects.equals(lightTwo, that.lightTwo) &&
                Objects.equals(altarOne, that.altarOne) &&
                Objects.equals(altarTwo, that.altarTwo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(date, place, additionalInformation, lector, lightOne, lightTwo, altarOne, altarTwo, duty);
    }
}
