package de.sauroter.miniplan.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.data.AltarService;

public class AltarServiceRecyclerViewAdapter extends RecyclerView.Adapter<AltarServiceRecyclerViewAdapter.ViewHolder> {

    private final List<AltarService> altarServices;

    public AltarServiceRecyclerViewAdapter(final List<AltarService> altarServices) {
        this.altarServices = altarServices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_altar_service, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setAltarService(altarServices.get(position));
        holder.getDetailsView().setText(altarServices.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return altarServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final View parentView;
        @NonNull
        private final TextView detailsView;
        @Nullable
        private AltarService altarService;


        public ViewHolder(final View itemView) {
            super(itemView);
            parentView = itemView;
            detailsView = itemView.findViewById(R.id.list_item_altarservice_details);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + detailsView.getText() + "'";
        }

        public AltarService getAltarService() {
            return altarService;
        }

        public void setAltarService(final AltarService altarService) {
            this.altarService = altarService;
            if (altarService != null && !altarService.isDuty()) {
                detailsView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.primaryLightColor));
            } else {
                detailsView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.primaryDarkColor));
            }
        }

        public View getParentView() {
            return parentView;
        }

        public TextView getDetailsView() {
            return detailsView;
        }
    }
}
