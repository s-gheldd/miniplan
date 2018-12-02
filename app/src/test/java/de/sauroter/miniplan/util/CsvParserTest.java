package de.sauroter.miniplan.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import de.sauroter.miniplan.data.AltarService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CsvParserTest {

    @Test
    public void test_parse_altar_service_line() {
        // Datum	Tag	Ort	Uhrzeit	Extra_Infos	Lektor	Leuchter	Leuchter	Altar	Altar	duty
        final String line = "08.11.2018;Donnerstag;St. Josef;19:00;;Mimi;Leo;Melvin;Rosa;Jana H.;0;";

        final AltarService altarService = CsvParser.parseAltarService(line);

        assertNotNull(altarService);
        assertEquals(1541700000000L, altarService.getDate().getTime());
        assertEquals("St. Josef", altarService.getPlace());
        assertNull(altarService.getAdditionalInformation());
        assertEquals("Mimi", altarService.getLector());
        assertEquals("Leo", altarService.getLightOne());
        assertEquals("Melvin", altarService.getLightTwo());
        assertEquals("Rosa", altarService.getAltarOne());
        assertEquals("Jana H.", altarService.getAltarTwo());
        assertFalse(altarService.isDuty());
    }

    @Test
    public void test_parse_altar_service_date() {

        final String dateString = "08.11.2018";
        final String timeString = "19:00";

        final Date date = CsvParser.parseAltarServiceDate(dateString, timeString);

        assertNotNull(date);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        assertEquals(8, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(10, calendar.get(Calendar.MONTH));
        assertEquals(2018, calendar.get(Calendar.YEAR));
        assertEquals(19, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
    }
}