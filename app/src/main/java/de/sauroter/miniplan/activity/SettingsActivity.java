package de.sauroter.miniplan.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.task.UpdatePendingAlarmsAsyncTask;

public class SettingsActivity extends ManageMiniplanUpdateJobActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    public static final String USER_PREFERENCE = "USER_PREFERENCE";
    public static final String PREF_ENABLE_ALARM = "PREF_ENABLE_ALARM";
    public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
    public static final String PREF_ALARM_GRACE = "PREF_ALARM_GRACE";
    public static final String PREF_SHOW_ALL_DUTY = "PREF_SHOW_ALL_DUTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPointerCaptureChanged(final boolean hasCapture) {

    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (PREF_ENABLE_ALARM.equals(key) || PREF_UPDATE_FREQ.equals(key) ||
                PREF_ALARM_GRACE.equals(key)) {
            super.manageUpdateJob();
            Log.d(SettingsActivity.class.getName(), "changed automatic update settings");
        }

        if (PREF_ALARM_GRACE.equals(key) || PREF_ENABLE_ALARM.equals(key)) {

            final AsyncTask<Void, Void, Void> asyncTask = new UpdatePendingAlarmsAsyncTask(this.getApplicationContext());
            asyncTask.execute();
        }
    }

    public static class PreferencesFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            this.setPreferencesFromResource(R.xml.userpreferences, null);
        }
    }

}
