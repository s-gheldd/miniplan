package de.sauroter.miniplan.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.sauroter.miniplan.data.Event;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.model.AltarServiceViewModel;
import de.sauroter.miniplan.view.EventRecyclerViewAdapter;

public class EventListFragment extends Fragment {

    private final ArrayList<Event> mEvents = new ArrayList<>();
    private final EventRecyclerViewAdapter mEventViewAdapter = new EventRecyclerViewAdapter(mEvents);

    @BindView(R.id.event_list)
    RecyclerView mEventRecyclerView;

    @BindView(R.id.event_refresh)
    SwipeRefreshLayout mEventRefresh;
    private AltarServiceViewModel mAltarServiceViewModel;
    private EventListFragment.OnListFragmentInteractionListener mListener;


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_event_list,
                container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get the AltarServiceViewModel
        mAltarServiceViewModel = ViewModelProviders.of(this.getActivity()).get(AltarServiceViewModel.class);
        // add observer
        mAltarServiceViewModel.getEvents().observe(this, es -> {
            if (es != null) {
                setEvents(es);
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Context context = view.getContext();

        this.mEventRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.mEventRecyclerView.setAdapter(mEventViewAdapter);

        this.mEventRefresh.setOnRefreshListener(this::updateEvents);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (EventListFragment.OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void setEvents(@NonNull final List<Event> events) {

        this.mEvents.clear();
        this.mEventViewAdapter.notifyDataSetChanged();
        for (final Event event : events) {
            if (!this.mEvents.contains(event)) {
                this.mEvents.add(event);
                this.mEventViewAdapter.notifyItemInserted(this.mEvents.indexOf(event));
            }
        }

        this.mEventRefresh.setRefreshing(false);
    }

    protected void updateEvents() {
        if (mListener != null) {
            mListener.onListFragmentRefreshRequested();
        }
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentRefreshRequested();
    }
}
