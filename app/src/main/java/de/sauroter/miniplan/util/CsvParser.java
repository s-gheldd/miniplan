package de.sauroter.miniplan.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.sauroter.miniplan.data.AltarService;
import de.sauroter.miniplan.data.Event;
import timber.log.Timber;

public class CsvParser {

    @Nullable
    public static AltarService parseAltarService(final String csvLine) {
        final String[] split = csvLine.split(";");

        if (split.length != 11) {
            return null;
        }

        //Datum	Tag	Ort	Uhrzeit	Extra_Infos	Lektor	Leuchter	Leuchter	Altar	Altar	duty

        final String date = split[0];
        final String day = split[1];
        final String place = split[2];
        final String time = split[3];
        final String additionaleInformation = split[4];
        final String lector = split[5];
        final String lightOne = split[6];
        final String lightTwo = split[7];
        final String altarOne = split[8];
        final String altarTwo = split[9];
        final String duty = split[10];

        if (date == null || place == null || time == null || duty == null) {
            return null;
        }

        final Date parsedDate = parseAltarServiceDate(date, time);
        if (parsedDate == null) {
            return null;
        }

        final Boolean parsedDuty = parseDuty(duty);
        if (parsedDuty == null) {
            return null;
        }


        return new AltarService(parsedDate,
                place,
                additionaleInformation.isEmpty() ? null : additionaleInformation,
                lector.isEmpty() ? null : lector,
                lightOne.isEmpty() ? null : lightOne,
                lightTwo.isEmpty() ? null : lightTwo,
                altarOne.isEmpty() ? null : altarOne,
                altarTwo.isEmpty() ? null : altarTwo,
                parsedDuty);
    }

    @Nullable
    public static Boolean parseDuty(@NonNull final String duty) {
        try {

            final int i = Integer.parseInt(duty);

            if (i == 0) {
                return false;
            } else if (i == 1) {
                return true;
            } else {
                return null;
            }
        } catch (final NumberFormatException numberException) {

            return null;
        }
    }

    @Nullable
    public static Date parseAltarServiceDate(@NonNull final String date, @NonNull final String time) {
        final String[] dateSplit = date.split("\\.");

        if (dateSplit.length != 3) {
            return null;
        }

        final String[] timeSplit = time.split(":");
        if (timeSplit.length != 2) {
            return null;
        }

        try {
            final int day = Integer.parseInt(dateSplit[0]);
            final int month = Integer.parseInt(dateSplit[1]);
            final int year = Integer.parseInt(dateSplit[2]);

            final int hour = Integer.parseInt(timeSplit[0]);
            final int minute = Integer.parseInt(timeSplit[1]);

            final Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(year, month - 1, day, hour, minute);

            return calendar.getTime();

        } catch (final NumberFormatException numberException) {

            return null;
        }
    }

    private static final DateFormat eventDateFormat = new SimpleDateFormat("dd.MM HH:mm", Locale.GERMANY);

    public static List<Event> parseEventLine(@NonNull final String line) {
        eventDateFormat.setLenient(true);

        final ArrayList<Event> events = new ArrayList<>();

        final String[] eventStrings = line.split(";;");

        final Calendar used = Calendar.getInstance(Locale.GERMANY);
        final int year = used.get(Calendar.YEAR);

        for (final String eventString : eventStrings) {
            final String[] split = eventString.split(";");

            if (split.length != 2) {
                continue;
            }

            try {
                final Date date = eventDateFormat.parse(split[0]);
                used.setTime(date);
                used.set(Calendar.YEAR, year);


                events.add(new Event(used.getTime(), split[1]));
            } catch (final ParseException e) {
                Timber.d(e, "Could not parse event line %s", eventString);
            }
        }

        return events;
    }

}
