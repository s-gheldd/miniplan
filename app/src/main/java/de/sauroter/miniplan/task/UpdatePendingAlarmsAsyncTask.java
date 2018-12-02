package de.sauroter.miniplan.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.util.List;

import de.sauroter.miniplan.activity.SettingsActivity;
import de.sauroter.miniplan.alarm.AlarmReceiver;
import de.sauroter.miniplan.data.AltarService;
import de.sauroter.miniplan.data.AltarServiceDAO;
import de.sauroter.miniplan.data.AltarServicedatabaseAccessor;

public class UpdatePendingAlarmsAsyncTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private final Context applicationContext;

    public UpdatePendingAlarmsAsyncTask(final Context applicationContext) {

        this.applicationContext = applicationContext;
    }

    @Override
    protected Void doInBackground(final Void... voids) {
        final AltarServiceDAO altarServiceDAO = AltarServicedatabaseAccessor.getInstance(applicationContext).altarServicesDao();
        final List<AltarService> altarServices = altarServiceDAO.loadAllDuty();
        for (final AltarService altarService : altarServices) {
            AlarmReceiver.removeAlarmForNotification(altarService.getDate(), altarService.getPlace(), applicationContext);
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        final boolean alarmsEnabled = prefs.getBoolean(SettingsActivity.PREF_ENABLE_ALARM, false);
        final String grace = prefs.getString(SettingsActivity.PREF_ALARM_GRACE, "15");
        final int gracePeriod = Integer.parseInt(grace);

        if (alarmsEnabled) {
            for (final AltarService altarService : altarServices) {
                AlarmReceiver.setAlarmForNotification(gracePeriod, altarService.getDate(), altarService.getPlace(), applicationContext);
            }
        }
        return null;
    }
}
