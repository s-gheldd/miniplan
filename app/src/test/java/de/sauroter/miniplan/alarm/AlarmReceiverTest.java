package de.sauroter.miniplan.alarm;

import android.text.format.DateUtils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AlarmReceiverTest {


    @Test
    public void testHour() {

        final long now = System.currentTimeMillis();

        assertEquals(2, AlarmReceiver.getHour(new Date(now), new Date(now + 2 * DateUtils.HOUR_IN_MILLIS + 32 * DateUtils.MINUTE_IN_MILLIS)));
        assertEquals(0, AlarmReceiver.getHour(new Date(now), new Date(now + 59 * DateUtils.MINUTE_IN_MILLIS)));
        assertEquals(0, AlarmReceiver.getHour(new Date(now), new Date(now + -4 * DateUtils.MINUTE_IN_MILLIS)));
    }

    @Test
    public void testMinute() {

        final long now = System.currentTimeMillis();

        assertEquals(32, AlarmReceiver.getMinute(new Date(now), new Date(now + 2 * DateUtils.HOUR_IN_MILLIS + 32 * DateUtils.MINUTE_IN_MILLIS)));
        assertEquals(59, AlarmReceiver.getMinute(new Date(now), new Date(now + 59 * DateUtils.MINUTE_IN_MILLIS)));
        assertEquals(0, AlarmReceiver.getMinute(new Date(now), new Date(now + -4 * DateUtils.MINUTE_IN_MILLIS)));
    }
}