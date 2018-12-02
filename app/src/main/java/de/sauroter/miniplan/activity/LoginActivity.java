package de.sauroter.miniplan.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.task.CheckUserCredentialsTask;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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
    protected void onCreate(Bundle savedInstanceState) {
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
    public void onClick(View v) {
        login();
        Toast.makeText(this, "Button", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("StaticFieldLeak")
    void login() {
        final String username = mUsernameEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();


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

                if (success) {
                    final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    final SharedPreferences.Editor preferenceEdit = preferences.edit();
                    preferenceEdit.putBoolean(tagLoginSuccessfulCompleted, success);
                    preferenceEdit.putString(tagUsername, username);
                    preferenceEdit.putString(tagPassword, password);
                    preferenceEdit.apply();
                }
            }
        };
        checkUserCredentialsTask.execute();
    }
}
