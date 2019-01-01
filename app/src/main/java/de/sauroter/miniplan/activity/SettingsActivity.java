package de.sauroter.miniplan.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.task.UpdatePendingAlarmsAsyncTask;
import timber.log.Timber;

public class SettingsActivity extends ManageMiniplanUpdateJobActivity implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {


    public static final String USER_PREFERENCE = "USER_PREFERENCE";
    public static final String PREF_ENABLE_ALARM = "PREF_ENABLE_ALARM";
    public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
    public static final String PREF_ALARM_GRACE = "PREF_ALARM_GRACE";
    public static final String PREF_SHOW_ALL_DUTY = "PREF_SHOW_ALL_DUTY";
    public static final String PREF_DEBUG_MODE_ENABLED = "PREF_DEBUG_MODE_ENABLED";
    public static final String PREF_SERVER_ENDPOINT = "PREF_SERVER_ENDPOINT";
    private Toolbar mToolbar;
    private int mDebugClicks = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnClickListener(this);


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (PREF_ENABLE_ALARM.equals(key) || PREF_UPDATE_FREQ.equals(key) ||
                PREF_ALARM_GRACE.equals(key)) {
            super.manageUpdateJob();
            Timber.d("changed automatic update settings");
        }

        if (PREF_ALARM_GRACE.equals(key) || PREF_ENABLE_ALARM.equals(key)) {

            final AsyncTask<Void, Void, Void> asyncTask = new UpdatePendingAlarmsAsyncTask(this.getApplicationContext());
            asyncTask.execute();
        }
    }

    @Override
    public void onClick(final View v) {
        if (v.equals(mToolbar)) {
            mDebugClicks++;


            switch (mDebugClicks) {
                case 8:
                    Toast.makeText(this, R.string.toast_debug_8, Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    Toast.makeText(this, R.string.toast_debug_9, Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putBoolean(SettingsActivity.PREF_DEBUG_MODE_ENABLED, true).apply();
                    break;
                default:
            }
        }
    }

    public static class PreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

        private Preference mDebugPreference;

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            this.setPreferencesFromResource(R.xml.userpreferences, null);

            mDebugPreference = this.findPreference(PREF_DEBUG_MODE_ENABLED);
            this.getPreferenceScreen().removePreference(mDebugPreference);
        }

        @Override
        public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            final Context context = view.getContext();

            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.registerOnSharedPreferenceChangeListener(this);

            if (prefs.getBoolean(PREF_DEBUG_MODE_ENABLED, false)) {
                this.getPreferenceScreen().addPreference(mDebugPreference);
            } else {
                this.getPreferenceScreen().removePreference(mDebugPreference);
            }
        }

        @Override
        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
            if (PREF_DEBUG_MODE_ENABLED.equals(key)) {
                if (sharedPreferences.getBoolean(PREF_DEBUG_MODE_ENABLED, false)) {
                    this.getPreferenceScreen().addPreference(mDebugPreference);
                } else {
                    this.getPreferenceScreen().removePreference(mDebugPreference);
                }
            }
        }
    }
}
