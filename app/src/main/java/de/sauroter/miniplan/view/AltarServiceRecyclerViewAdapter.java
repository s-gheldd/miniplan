package de.sauroter.miniplan.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.sauroter.miniplan.data.AltarService;
import de.sauroter.miniplan.miniplan.R;

public class AltarServiceRecyclerViewAdapter extends RecyclerView.Adapter<AltarServiceRecyclerViewAdapter.ViewHolder> {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("EE dd.MM.yyyy HH:mm", Locale.GERMANY);

    private final List<AltarService> altarServices;

    public AltarServiceRecyclerViewAdapter(@NonNull final List<AltarService> altarServices) {
        this.altarServices = altarServices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_altarservice, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AltarService altarService = altarServices.get(position);
        holder.setAltarService(altarService);
        holder.getDateView().setText(TIME_FORMAT.format(altarService.getDate()));
        holder.getPlaceView().setText(altarService.getPlace());
        holder.getDetailsView().setText(altarService.getAdditionalInformation());
    }

    @Override
    public int getItemCount() {
        return altarServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final View parentView;
        @NonNull
        private final TextView placeView;
        @NonNull
        private final TextView detailsView;
        @NonNull
        private final TextView dateView;

        @Nullable
        private AltarService altarService;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            parentView = itemView;
            dateView = itemView.findViewById(R.id.list_item_altarservice_date);
            detailsView = itemView.findViewById(R.id.list_item_altarservice_details);
            placeView = itemView.findViewById(R.id.list_item_altarservice_place);
        }


        public AltarService getAltarService() {
            return altarService;
        }

        private void setAlpha(final float alpha) {
            parentView.setAlpha(alpha);
            dateView.setAlpha(alpha);
            detailsView.setAlpha(alpha);
            placeView.setAlpha(alpha);
        }

        public void setAltarService(@Nullable final AltarService altarService) {
            this.altarService = altarService;
            if (altarService != null && !altarService.isDuty()) {
                parentView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.tile_background_light));
                setAlpha(0.5f);
            } else {
                parentView.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.tile_background_dark));
                setAlpha(1f);
            }
        }

        @NonNull
        public View getParentView() {
            return parentView;
        }

        @NonNull
        public TextView getDetailsView() {
            return detailsView;
        }

        @NonNull
        public TextView getPlaceView() {
            return placeView;
        }

        @NonNull
        public TextView getDateView() {
            return dateView;
        }
    }
}
