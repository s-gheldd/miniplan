package de.sauroter.miniplan.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.sauroter.miniplan.data.Event;
import de.sauroter.miniplan.miniplan.R;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("EE dd.MM.yyyy HH:mm", Locale.GERMANY);

    private final List<Event> events;

    public EventRecyclerViewAdapter(final List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Event event = events.get(position);
        holder.setEvent(event);
        holder.getDateView().setText(TIME_FORMAT.format(event.getDate()));
        holder.getInfoView().setText(event.getInfo());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final View parentView;
        @NonNull
        private final TextView infoView;
        @NonNull
        private final TextView dateView;
        @Nullable
        private Event event;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.parentView = itemView;
            this.infoView = itemView.findViewById(R.id.list_item_event_info);
            this.dateView = itemView.findViewById(R.id.list_item_event_date);
        }

        @NonNull
        public View getParentView() {
            return parentView;
        }

        @NonNull
        public TextView getInfoView() {
            return infoView;
        }

        @NonNull
        public TextView getDateView() {
            return dateView;
        }

        public void setEvent(final Event event) {
            if (event != null) {
                this.event = event;
            }
        }
    }
}
