package de.sauroter.miniplan.activity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

    private boolean loginSuccessful = false;

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

    void login() {

        final AsyncTask<Uri, Integer, CheckUserCredentialsTask.Response> execute = new CheckUserCredentialsTask(_progress_bar).execute((Uri) null);

        loginSuccessful = true;
        final String username = mUsernameEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();


        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor preferenceEdit = preferences.edit();
        preferenceEdit.putBoolean(tagLoginSuccessfulCompleted, loginSuccessful);
        preferenceEdit.putString(tagUsername,username);
        preferenceEdit.putString(tagPassword,password);
        preferenceEdit.apply();
    }
}
