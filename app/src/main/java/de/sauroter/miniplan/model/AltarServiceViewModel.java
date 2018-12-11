package de.sauroter.miniplan.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import de.sauroter.miniplan.data.AltarService;
import de.sauroter.miniplan.data.AltarServicedatabaseAccessor;
import de.sauroter.miniplan.data.Event;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.task.FetchMiniplanDataAsyncTask;

public class AltarServiceViewModel extends AndroidViewModel {

    @NonNull
    private final String TAG;
    @NonNull
    private final String tagLoginSuccessfulCompleted;
    @NonNull
    private final String tagMiniplanUsername;
    @NonNull
    private final String tagMiniplanPassword;
    @Nullable
    private LiveData<List<AltarService>> altarServices;
    @Nullable
    private LiveData<List<Event>> events;


    public AltarServiceViewModel(@NonNull final Application application) {
        super(application);
        this.TAG = application.getString(R.string.tag_miniplan_update);
        this.tagLoginSuccessfulCompleted = application.getString(R.string.tag_login_successful_completed);
        this.tagMiniplanUsername = application.getString(R.string.tag_miniplan_username);
        this.tagMiniplanPassword = application.getString(R.string.tag_miniplan_password);
    }

    @NonNull
    public LiveData<List<AltarService>> getAltarServices() {

        if (altarServices == null) {
            altarServices = AltarServicedatabaseAccessor
                    .getInstance(this.getApplication())
                    .altarServicesDao()
                    .loadAllAltarServices();
        }
        return altarServices;
    }

    @NonNull
    public LiveData<List<Event>> getEvents() {
        if (events == null) {
            events = AltarServicedatabaseAccessor
                    .getInstance(this.getApplication())
                    .eventDao()
                    .loadAllEvents();
        }
        return events;
    }

    public void loadMiniplanData() {


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        final boolean loginSuccessful = sharedPreferences.getBoolean(tagLoginSuccessfulCompleted, false);

        if (loginSuccessful) {

            final String username = sharedPreferences.getString(tagMiniplanUsername, "");
            final String password = sharedPreferences.getString(tagMiniplanPassword, "");
            final AsyncTask<Void, Void, List<AltarService>> asyncTask = new FetchMiniplanDataAsyncTask(username, password, getApplication());
            asyncTask.execute((Void) null);
        }
    }

}
