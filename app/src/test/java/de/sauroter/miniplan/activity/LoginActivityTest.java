package de.sauroter.miniplan.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.Scheduler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

    @Test
    public void test_login_performs_request() {
        final Scheduler backgroundThreadScheduler = Robolectric.getBackgroundThreadScheduler();
        backgroundThreadScheduler.pause();
        assertFalse(backgroundThreadScheduler.areAnyRunnable());
        final LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);

        loginActivity.mPasswordEditText.setText("test");
        loginActivity.mUsernameEditText.setText("test");
        loginActivity.mConnectButton.performClick();

        assertTrue(backgroundThreadScheduler.areAnyRunnable());
    }

    @Test
    public void test_missing_parameters_no_request() {
        final Scheduler backgroundThreadScheduler = Robolectric.getBackgroundThreadScheduler();
        backgroundThreadScheduler.pause();
        assertFalse(backgroundThreadScheduler.areAnyRunnable());
        final LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);

        loginActivity.mPasswordEditText.setText("test");
        loginActivity.mConnectButton.performClick();

        assertFalse(backgroundThreadScheduler.areAnyRunnable());
    }
}