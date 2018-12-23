package de.sauroter.miniplan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.sauroter.miniplan.alarm.AlarmReceiver;
import de.sauroter.miniplan.fragment.OnListFragmentInteractionListener;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.model.AltarServiceViewModel;
import de.sauroter.miniplan.task.RemovePastDatabaseEntriesTask;
import de.sauroter.miniplan.view.MiniplanTabsPagerAdapter;

public class MiniplanActivity extends ManageMiniplanUpdateJobActivity implements OnListFragmentInteractionListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int REQUEST = 2;
    private static final int MENU_PREFERENCES = Menu.FIRST + 1;
    private static final int MENU_LOGIN = MENU_PREFERENCES + 1;
    private static final int MENU_DEBUG = MENU_LOGIN + 1;

    @BindString(R.string.tag_login_successful_completed)
    String tagLoginSuccessfulCompleted;

    @BindString(R.string.tag_miniplan_list_fragment)
    String tagMiniplanListFragment;

    @BindView(R.id.miniplan_reload_fab)
    FloatingActionButton mFab;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private AltarServiceViewModel mAltarServiceViewModel;
    private Toolbar mToolbar;
    private MiniplanTabsPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miniplan);
        ButterKnife.bind(this);

        if (mViewPager != null) {
            mPagerAdapter = new MiniplanTabsPagerAdapter(getSupportFragmentManager(), this);
            mViewPager.setAdapter(mPagerAdapter);
            final TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(mViewPager);
        }

        // get the AltarServiceViewModel
        mAltarServiceViewModel = ViewModelProviders.of(this).get(AltarServiceViewModel.class);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        mFab.setOnClickListener(view -> updateAltarServices());


        manageUpdateJob();
        new RemovePastDatabaseEntriesTask(this).execute(new Date(System.currentTimeMillis() - 2 * DateUtils.HOUR_IN_MILLIS));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MiniplanActivity.REQUEST && resultCode == LoginActivity.RESULT) {
            this.mAltarServiceViewModel.loadMiniplanData();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!preferences.getBoolean(tagLoginSuccessfulCompleted, false)) {

            final Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, MiniplanActivity.REQUEST);
        }

        preferences.registerOnSharedPreferenceChangeListener(this);


        final Intent intent = getIntent();
        Fragment registeredFragment = mPagerAdapter.getRegisteredFragment(0);
        if (registeredFragment == null) {
            registeredFragment = (Fragment) mPagerAdapter.instantiateItem(mViewPager, 0);
        }
        final Bundle arguments = registeredFragment.getArguments();
        if (arguments != null) {
            arguments.putSerializable(AlarmReceiver.SCROLL_TO_DATE, intent.getSerializableExtra(AlarmReceiver.SCROLL_TO_DATE));
        } else {
            registeredFragment.setArguments(intent.getExtras());
        }
    }

    @Override
    public void onListFragmentRefreshRequested() {
        this.updateAltarServices();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_settings);
        menu.add(0, MENU_LOGIN, Menu.NONE, R.string.menu_login);

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.PREF_DEBUG_MODE_ENABLED, false)) {
            menu.add(1, MENU_DEBUG, Menu.NONE, "Debug");

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case MENU_PREFERENCES: {
                final Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                return true;
            }
            case MENU_LOGIN: {
                final Intent intent = new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                return true;
            }
            case MENU_DEBUG: {
                final Intent intent = new Intent(this, DebugActivity.class);
                this.startActivity(intent);
                return true;
            }
        }
        return false;
    }

    private void updateAltarServices() {
        mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
        this.mAltarServiceViewModel.loadMiniplanData();
    }


    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (SettingsActivity.PREF_DEBUG_MODE_ENABLED.equals(key)) {
            this.invalidateOptionsMenu();
        }
    }
}
