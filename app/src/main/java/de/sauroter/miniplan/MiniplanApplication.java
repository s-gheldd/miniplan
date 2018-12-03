package de.sauroter.miniplan;

import android.app.Application;

import de.sauroter.miniplan.miniplan.BuildConfig;
import timber.log.Timber;

public class MiniplanApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
