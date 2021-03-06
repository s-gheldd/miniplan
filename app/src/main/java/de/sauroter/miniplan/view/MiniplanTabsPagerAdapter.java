package de.sauroter.miniplan.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import de.sauroter.miniplan.fragment.AltarServiceListFragment;
import de.sauroter.miniplan.fragment.EventListFragment;
import de.sauroter.miniplan.miniplan.R;

public class MiniplanTabsPagerAdapter extends FragmentPagerAdapter {

    @NonNull
    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    @NonNull
    private final Context context;

    public MiniplanTabsPagerAdapter(@NonNull final FragmentManager fm, @NonNull final Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0:
                return new AltarServiceListFragment();
            case 1:
                return new EventListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_altar_service);
            case 1:
                return context.getString(R.string.tab_events);
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}