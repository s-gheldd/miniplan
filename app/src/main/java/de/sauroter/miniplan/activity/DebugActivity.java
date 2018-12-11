package de.sauroter.miniplan.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.sauroter.miniplan.alarm.AlarmReceiver;
import de.sauroter.miniplan.data.AltarService;
import de.sauroter.miniplan.data.AltarServiceDAO;
import de.sauroter.miniplan.data.AltarServicedatabaseAccessor;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.task.RemovePastDatabaseEntriesTask;
import de.sauroter.miniplan.task.UpdatePendingAlarmsAsyncTask;

public class DebugActivity extends AppCompatActivity {

    @BindView(R.id.debug_text_edit_grace)
    EditText mInputGrace;

    @BindView(R.id.debug_text_edit_delay)
    EditText mInputDelay;

    @BindView(R.id.debug_notification)
    Button mNotificationButton;

    @BindView(R.id.debug_alarm)
    Button mAlarmButton;

    @BindView(R.id.debug_altar_service)
    Button mAltarServiceButton;

    @BindView(R.id.debug_clear)
    Button mClearButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnTextChanged(value = {R.id.debug_text_edit_delay},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterDelayInput(final Editable editable) {
        if (!validateTextInput(editable.toString())) {
            mInputDelay.setError("Needs to be an Integer");
        }
    }


    @OnTextChanged(value = {R.id.debug_text_edit_grace},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterGraceInput(final Editable editable) {
        if (!validateTextInput(editable.toString())) {
            mInputGrace.setError("Needs to be an Integer");
        }
    }


    private boolean validateTextInput(final String editable) {
        try {
            Integer.parseInt(editable);
        } catch (final NumberFormatException nf) {
            return false;
        }
        return true;
    }

    @OnClick({R.id.debug_notification, R.id.debug_alarm, R.id.debug_altar_service, R.id.debug_clear})
    void onClick(final Button button) {


        final Date date = new Date();

        final String graceString = mInputGrace.getText().toString();
        final String delayString = mInputDelay.getText().toString();
        if (!validateTextInput(delayString)) {
            mInputDelay.setError("Needs to be an Integer");
            return;
        }

        if (!validateTextInput(graceString)) {
            mInputGrace.setError("Needs to be an Integer");
            return;
        }

        final int grace = Integer.parseInt(graceString);
        final int delay = Integer.parseInt(delayString);

        if (button.equals(mNotificationButton)) {
            AlarmReceiver.sendNotification(date, "St. Georg", this.getApplicationContext());
        } else if (button.equals(mAlarmButton)) {
            AlarmReceiver.setAlarmForNotification(grace * 1000, new Date(date.getTime() + delay * 1000), "St. Moritz", this.getApplicationContext());
        } else if (button.equals(mAltarServiceButton)) {
            new AddDebugAltarServiceAsyncTask(delay, this.getApplicationContext()).execute();
        } else if (button.equals(mClearButton)) {
            new RemovePastDatabaseEntriesTask(this.getApplicationContext()).execute(new Date(Long.MAX_VALUE));
        }
    }


    private static class AddDebugAltarServiceAsyncTask extends AsyncTask<Void, Void, Void> {


        private final int delay;
        @SuppressLint("StaticFieldLeak")
        private final Context applicationContext;

        private AddDebugAltarServiceAsyncTask(final int delay, final Context applicationContext) {
            this.delay = delay;
            this.applicationContext = applicationContext;
        }

        @Override
        protected Void doInBackground(final Void... voids) {


            final AltarServiceDAO altarServiceDAO = AltarServicedatabaseAccessor.getInstance(applicationContext).altarServicesDao();

            altarServiceDAO.insertAltarService(new AltarService(new Date(new Date().getTime() + delay * 1000),
                    "St. Gallen",
                    "Das ist sonderbar",
                    "Mary",
                    "Joseph",
                    "Jakob",
                    "John",
                    "Peter",
                    true));

            new UpdatePendingAlarmsAsyncTask(applicationContext).execute();

            return null;
        }
    }
}
