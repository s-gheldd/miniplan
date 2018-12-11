package de.sauroter.miniplan.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.sauroter.miniplan.activity.DetailActivity;
import de.sauroter.miniplan.activity.SettingsActivity;
import de.sauroter.miniplan.alarm.AlarmReceiver;
import de.sauroter.miniplan.data.AltarService;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.model.AltarServiceViewModel;
import de.sauroter.miniplan.view.AltarServiceRecyclerViewAdapter;
import timber.log.Timber;

public class AltarServiceListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, Observer<List<AltarService>> {

    private final ArrayList<AltarService> mAltarServices = new ArrayList<>();
    private final AltarServiceRecyclerViewAdapter mAltarServiceViewAdapter = new AltarServiceRecyclerViewAdapter(mAltarServices);

    @BindView(R.id.altar_service_list)
    RecyclerView mAltarServiceRecyclerView;

    @BindView(R.id.altar_service_refresh)
    SwipeRefreshLayout mAltarServiceRefresh;

    private OnListFragmentInteractionListener mListener;
    private AltarServiceViewModel mAltarServiceViewModel;

    private boolean mShowAll = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_altar_service_list, container, false);

        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get the AltarServiceViewModel
        mAltarServiceViewModel = ViewModelProviders.of(this.getActivity()).get(AltarServiceViewModel.class);
        // add observer
        mAltarServiceViewModel.getAltarServices().observe(this, this);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Context context = view.getContext();

        this.mAltarServiceRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.mAltarServiceRecyclerView.setAdapter(mAltarServiceViewAdapter);

        this.mAltarServiceRefresh.setOnRefreshListener(this::updateAltarServices);
        this.mAltarServiceViewAdapter.setItemClickListener(this::onItemClicked);
    }

    private void onItemClicked(final View view) {
        final AltarServiceRecyclerViewAdapter.ViewHolder tag = (AltarServiceRecyclerViewAdapter.ViewHolder) view.getTag();

        final AltarService altarService = tag.getAltarService();
        final Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.DETAIL_ALTAR_SERVICE, altarService);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getArguments() != null) {
            getArguments().remove(AlarmReceiver.SCROLL_TO_DATE);
        }
    }

    // every Conext for this Fragment must implement OnListFragmentInteractionListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onChanged(@Nullable final List<AltarService> altarServices) {
        if (altarServices != null) {
            setAltarServices(altarServices);
        }
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (SettingsActivity.PREF_SHOW_ALL_DUTY.equals(key)) {
            final List<AltarService> altarServices = mAltarServiceViewModel.getAltarServices().getValue();
            if (altarServices != null) {
                this.setAltarServices(altarServices);
            }
        }
    }

    public void setAltarServices(@NonNull final List<AltarService> altarServices) {
        this.updateFromPreferences();

        this.mAltarServices.clear();
        this.mAltarServiceViewAdapter.notifyDataSetChanged();
        for (final AltarService altarService : altarServices) {
            if (altarService.isDuty() || this.mShowAll) {
                if (!this.mAltarServices.contains(altarService)) {
                    this.mAltarServices.add(altarService);
                    this.mAltarServiceViewAdapter.notifyItemInserted(this.mAltarServices.indexOf(altarService));
                }
            }
        }

        if (mAltarServices != null && mAltarServices.size() > 0)
            for (int i = mAltarServices.size() - 1; i >= 0; i--) {
                if (!mAltarServices.get(i).isDuty() && !mShowAll) {
                    mAltarServices.remove(i);
                    mAltarServiceViewAdapter.notifyItemRemoved(i);
                }
            }

        this.mAltarServiceRefresh.setRefreshing(false);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            final Date date = (Date) arguments.getSerializable(AlarmReceiver.SCROLL_TO_DATE);
            if (date != null) {
                scrollToDate(date);
            }
        }
    }

    protected void updateAltarServices() {
        if (mListener != null) {
            mListener.onListFragmentRefreshRequested();
        }
    }

    private void updateFromPreferences() {
        final Context context = getContext();

        if (context != null) {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(context);
            mShowAll = prefs.getBoolean(SettingsActivity.PREF_SHOW_ALL_DUTY, false);
        }
    }

    private void scrollToDate(final Date date) {
        final int i = Collections.binarySearch(mAltarServices, AltarService.dateOnlyService(date), AltarService.DATE_COMPARATOR);
        Timber.d("Search got index %d", i);
        mAltarServiceRecyclerView.post(() -> mAltarServiceRecyclerView.scrollToPosition(i));
    }
}
