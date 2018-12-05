package de.sauroter.miniplan.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Date;

import de.sauroter.miniplan.data.AltarServiceDAO;
import de.sauroter.miniplan.data.AltarServicedatabaseAccessor;
import de.sauroter.miniplan.data.EventDao;

public class RemovePastDatabaseEntriesTask extends AsyncTask<Date, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;

    public RemovePastDatabaseEntriesTask(final Context context) {


        this.context = context;
    }

    @Override
    protected Void doInBackground(final Date... dates) {
        final Date date = dates[0];

        final AltarServiceDAO altarServiceDAO = AltarServicedatabaseAccessor.getInstance(context).altarServicesDao();
        final EventDao eventDao = AltarServicedatabaseAccessor.getInstance(context).eventDao();

        altarServiceDAO.deleteOlder(date);
        eventDao.deleteOlder(date);

        return null;
    }
}
