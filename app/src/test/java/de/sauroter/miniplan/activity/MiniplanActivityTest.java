package de.sauroter.miniplan.activity;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import androidx.room.Room;
import de.sauroter.miniplan.data.AltarServiceDatabase;
import de.sauroter.miniplan.data.AltarServicedatabaseAccessor;
import de.sauroter.miniplan.model.AltarServiceUpdateJobService;
import de.sauroter.miniplan.testUtil.ShadowFragmentPagerAdapter;
import de.sauroter.miniplan.testUtil.TestAltarService;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowFragmentPagerAdapter.class})
public class MiniplanActivityTest {

    private AltarServiceDatabase roomDatabase;

    @Before
    public void setUp() throws Exception {
        roomDatabase = Room.inMemoryDatabaseBuilder(getApplicationContext(), AltarServiceDatabase.class).allowMainThreadQueries().build();

        AltarServicedatabaseAccessor.setAltarServiceDatabaseInstance(roomDatabase);
    }

    @Test
    public void test_login_intent_no_credentials() {

        final MiniplanActivity miniplanActivity = Robolectric.setupActivity(MiniplanActivity.class);
        final Intent expectedIntent = new Intent(miniplanActivity, LoginActivity.class);

        final Intent nextStartedActivity = shadowOf((Application) getApplicationContext()).getNextStartedActivity();
        Robolectric.flushBackgroundThreadScheduler();
        assertEquals(expectedIntent.getComponent(), nextStartedActivity.getComponent());
    }

    @Test
    public void test_login_intent_credentials() {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        defaultSharedPreferences.edit().putBoolean("TAG_LOGIN_SUCCESSFUL_COMPLETED", true).apply();
        final MiniplanActivity miniplanActivity = Robolectric.setupActivity(MiniplanActivity.class);
        final Intent nextStartedActivity = shadowOf((Application) getApplicationContext()).getNextStartedActivity();

        assertNull(nextStartedActivity);
    }

    @Test
    public void test_start_remove_past_entries() {
        roomDatabase.altarServicesDao().insertAltarService(new TestAltarService(new Date(System.currentTimeMillis() - 4 * DateUtils.HOUR_IN_MILLIS), "somewhere", true));
        assertFalse(roomDatabase.altarServicesDao().loadAllDuty().isEmpty());

        final MiniplanActivity miniplanActivity = Robolectric.setupActivity(MiniplanActivity.class);
        Robolectric.flushBackgroundThreadScheduler();

        assertTrue(roomDatabase.altarServicesDao().loadAllDuty().isEmpty());
    }

    @Test
    public void test_start_update_job() {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(SettingsActivity.PREF_ENABLE_ALARM, true).commit();
        final MiniplanActivity miniplanActivity = Robolectric.setupActivity(MiniplanActivity.class);
        final JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);


        final JobInfo pendingJob = jobScheduler.getPendingJob(AltarServiceUpdateJobService.ID);

        assertEquals(AltarServiceUpdateJobService.class.getName(), pendingJob.getService().getClassName());
    }
}