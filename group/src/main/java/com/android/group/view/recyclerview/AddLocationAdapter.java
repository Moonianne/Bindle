package com.android.group.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.group.R;
import com.android.group.model.Venue;

import java.util.ArrayList;
import java.util.List;

public class AddLocationAdapter extends RecyclerView.Adapter<AddLocationViewHolder> {

    private List<Venue> venues = new ArrayList<>();

    @NonNull
    @Override
    public AddLocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_location_item_view,viewGroup,false);
        return new AddLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddLocationViewHolder addLocationViewHolder, int i) {
        addLocationViewHolder.onBind(venues.get(i));
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    public void setData(List<Venue> venues){
        this.venues = venues;
        notifyDataSetChanged();
    }
}
