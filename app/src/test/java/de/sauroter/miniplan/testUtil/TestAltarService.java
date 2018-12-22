package de.sauroter.miniplan.testUtil;

import android.support.annotation.NonNull;

import java.util.Date;

import de.sauroter.miniplan.data.AltarService;

public class TestAltarService extends AltarService {

    static String additionalInformation = "Something important";
    static String lector = "John";
    static String lightOne = "Peter";
    static String lightTwo = "Mary";
    static String altarOne = "Sheba";
    static String altarTwo = "David";

    public TestAltarService(@NonNull final Date date, @NonNull final String place, final boolean duty) {
        super(date, place, additionalInformation, lector, lightOne, lightTwo, altarOne, altarTwo, duty);
    }
}
