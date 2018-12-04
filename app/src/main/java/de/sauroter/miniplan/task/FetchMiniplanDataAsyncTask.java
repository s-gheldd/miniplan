package de.sauroter.miniplan.task;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import de.sauroter.miniplan.activity.SettingsActivity;
import de.sauroter.miniplan.alarm.AlarmReceiver;
import de.sauroter.miniplan.data.AltarService;
import de.sauroter.miniplan.data.AltarServiceDAO;
import de.sauroter.miniplan.data.AltarServicedatabaseAccessor;
import de.sauroter.miniplan.data.Event;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.util.CsvParser;
import timber.log.Timber;

public class FetchMiniplanDataAsyncTask extends AsyncTask<Void, Void, List<AltarService>> {
    private final String username;
    private final String password;
    private final Application application;

    public FetchMiniplanDataAsyncTask(final String username, final String password, @NonNull final Application application) {
        this.username = username;
        this.password = password;
        this.application = application;
    }

    @Override
    protected List<AltarService> doInBackground(final Void... voids) {

        final Uri uri = new Uri.Builder().scheme("https")
                .authority("majo-minis.de")
                .path("App/Handy.php")
                .appendQueryParameter("Name", username)
                .appendQueryParameter("Passwort", password)
                .build();

        final ArrayList<AltarService> altarServicesList = new ArrayList<>();
        final ArrayList<Event> eventsList = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(uri.toString()).openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);

            final int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                return null;
            }


            final String response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())).readLine();
            final String[] split = TextUtils.split(response, "%");

            final List<String> lines = Arrays.asList(split);
            final Iterator<String> lineIterator = lines.iterator();

            ParsingState state = null;
            final String success = application.getString(R.string.csv_success);
            final String beginMiniplan = application.getString(R.string.csv_begin_miniplan);
            final String beginDates = application.getString(R.string.csv_begin_dates);
            final String error = application.getString(R.string.csv_error);


            while (lineIterator.hasNext()) {
                final String line = lineIterator.next().trim();

                if (success.equals(line)) {
                    state = ParsingState.BEGIN;
                    continue;
                } else if (beginMiniplan.equals(line)) {
                    state = ParsingState.MINIPLAN;
                    continue;
                } else if (beginDates.equals(line)) {
                    state = ParsingState.DATES;
                    continue;
                } else if (error.equals(line)) {
                    break;
                }

                if (state == ParsingState.MINIPLAN) {
                    final AltarService altarService = CsvParser.parseAltarService(line);
                    altarServicesList.add(altarService);
                }
            }

        } catch (final IOException e) {
            Timber.e(e);
            return new ArrayList<>();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(application);
        final boolean alarmEnabled = prefs.getBoolean(SettingsActivity.PREF_ENABLE_ALARM, true);
        if (alarmEnabled) {
            final String grace = prefs.getString(SettingsActivity.PREF_ALARM_GRACE, "15");
            final int gracePeriod = Integer.parseInt(grace);
            final AltarServiceDAO altarServiceDAO = AltarServicedatabaseAccessor
                    .getInstance(this.application)
                    .altarServicesDao();


            for (final AltarService altarService : altarServicesList) {
                if (altarService.isDuty()) {
                    final Date date = altarService.getDate();
                    final String place = altarService.getPlace();

                    final AltarService byDateAndPlace = altarServiceDAO.findByDateAndPlace(date, place);
                    if (byDateAndPlace == null) {
                        AlarmReceiver.setAlarmForNotification(gracePeriod * 1000 * 60, date, place, application.getApplicationContext());
                    }
                }
            }
        }


        AltarServicedatabaseAccessor
                .getInstance(this.application)
                .altarServicesDao()
                .insertAltarServices(altarServicesList);

        AltarServicedatabaseAccessor
                .getInstance(this.application)
                .eventDao()
                .insertEvents(eventsList);

        return altarServicesList;
    }

    private enum ParsingState {
        BEGIN, MINIPLAN, DATES
    }
}
