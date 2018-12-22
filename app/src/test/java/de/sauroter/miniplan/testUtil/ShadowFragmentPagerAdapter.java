package de.sauroter.miniplan.testUtil;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import org.mockito.internal.util.reflection.Fields;
import org.mockito.internal.util.reflection.InstanceField;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

import androidx.annotation.NonNull;

import static org.robolectric.shadow.api.Shadow.directlyOn;

/**
 * This only exists as a workaround for a Robolectric bug with Fragments and ViewPagers - it can be deleted
 * and the relevant @Config( shadows = { ShadowViewPager.class }) entries may be removed once Robolectric is fixed
 * https://github.com/robolectric/robolectric/issues/3698#issuecomment-441839491
 */
@Implements(FragmentPagerAdapter.class)
public class ShadowFragmentPagerAdapter {

    @RealObject
    FragmentPagerAdapter realObject;

    @Implementation
    public void finishUpdate(@NonNull ViewGroup container) {
        FragmentManager fragmentManager = getFragmentManagerFromAdapter(realObject);
        if (fragmentManager.getFragments().isEmpty()) {
            directlyOn(realObject, PagerAdapter.class).finishUpdate(container);
        }
    }

    private FragmentManager getFragmentManagerFromAdapter(PagerAdapter adapter) {
        for (InstanceField instanceField : Fields.allDeclaredFieldsOf(adapter).instanceFields()) {
            Object obj = instanceField.read();
            if (obj instanceof FragmentManager) {
                return (FragmentManager) obj;
            }
        }
        return null;
    }

}