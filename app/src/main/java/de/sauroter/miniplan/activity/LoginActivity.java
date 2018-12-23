package de.sauroter.miniplan.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.task.CheckUserCredentialsTask;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int RESULT = 1;

    @BindView(R.id.login_text_edit_username)
    EditText mUsernameEditText;

    @BindView(R.id.login_text_edit_password)
    EditText mPasswordEditText;

    @BindView(R.id.login_button_connect)
    Button _connect;

    @BindView(R.id.login_progress_bar)
    ProgressBar _progress_bar;

    @BindString(R.string.tag_login_successful_completed)
    String tagLoginSuccessfulCompleted;

    @BindString(R.string.tag_miniplan_username)
    String tagUsername;

    @BindString(R.string.tag_miniplan_password)
    String tagPassword;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        _connect.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        login();
    }

    @SuppressLint("StaticFieldLeak")
    void login() {
        final String username = mUsernameEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();
        final Context activityContext = this;

        if (username.isEmpty()) {
            mUsernameEditText.setError(activityContext.getString(R.string.error_field_required));
            return;
        }
        if (password.isEmpty()) {
            mPasswordEditText.setError(activityContext.getString(R.string.error_field_required));
            return;
        }

        final ConnectivityManager cm = (ConnectivityManager) activityContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            _connect.setError(activityContext.getString(R.string.error_network));
            Toast.makeText(activityContext, R.string.error_network, Toast.LENGTH_LONG).show();

            return;
        }


        final CheckUserCredentialsTask checkUserCredentialsTask = new CheckUserCredentialsTask(username, password, getApplication()) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                _progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                super.onPostExecute(success);
                _progress_bar.setVisibility(View.GONE);
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                final boolean wasAuthenticated = preferences.getBoolean(tagLoginSuccessfulCompleted, false);

                if (success) {
                    final SharedPreferences.Editor preferenceEdit = preferences.edit();
                    preferenceEdit.putBoolean(tagLoginSuccessfulCompleted, success);
                    preferenceEdit.putString(tagUsername, username);
                    preferenceEdit.putString(tagPassword, password);
                    preferenceEdit.apply();
                    Toast.makeText(activityContext, R.string.toast_login_success, Toast.LENGTH_LONG).show();
                    if (!wasAuthenticated) {
                        setResult(LoginActivity.RESULT);
                        finish();
                    }
                } else {
                    mUsernameEditText.setError(activityContext.getString(R.string.error_invalid));
                    mPasswordEditText.setError(activityContext.getString(R.string.error_invalid));
                }
            }
        };
        checkUserCredentialsTask.execute();
    }
}
