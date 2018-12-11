package de.sauroter.miniplan.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;

import de.sauroter.miniplan.model.AltarServiceUpdateJobService;

import static de.sauroter.miniplan.activity.SettingsActivity.PREF_ENABLE_ALARM;
import static de.sauroter.miniplan.activity.SettingsActivity.PREF_UPDATE_FREQ;

public abstract class ManageMiniplanUpdateJobActivity extends AppCompatActivity {

    protected void manageUpdateJob() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean activate = sharedPreferences.getBoolean(PREF_ENABLE_ALARM, false);

        final JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (activate) {

            final String updateFreq = sharedPreferences.getString(PREF_UPDATE_FREQ, "24");
            final int frequency = Integer.parseInt(updateFreq);


            final JobInfo.Builder builder = new JobInfo.Builder(AltarServiceUpdateJobService.ID,
                    new ComponentName(this, AltarServiceUpdateJobService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setPeriodic(frequency * DateUtils.HOUR_IN_MILLIS);
            jobScheduler.schedule(builder.build());

        } else {
            jobScheduler.cancel(AltarServiceUpdateJobService.ID);
        }
    }
}
