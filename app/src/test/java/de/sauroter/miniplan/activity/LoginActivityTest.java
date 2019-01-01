package de.sauroter.miniplan.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import de.sauroter.miniplan.testUtil.ShadowFragmentPagerAdapter;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowFragmentPagerAdapter.class})
public class LoginActivityTest {

    @Test
    public void test_login_performs_request() {

    }
}