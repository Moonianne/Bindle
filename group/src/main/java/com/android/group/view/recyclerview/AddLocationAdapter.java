package com.android.group.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.group.R;
import com.android.group.model.bindle.BindleBusiness;

import java.util.ArrayList;
import java.util.List;

public class AddLocationAdapter extends RecyclerView.Adapter<AddLocationViewHolder> {

    private List<BindleBusiness> bindleBusinesses = new ArrayList<>();

    @NonNull
    @Override
    public AddLocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_location_item_view,viewGroup,false);
        return new AddLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddLocationViewHolder addLocationViewHolder, int i) {
        addLocationViewHolder.onBind(bindleBusinesses.get(i));
    }

    @Override
    public int getItemCount() {
        return bindleBusinesses.size();
    }

    public void setData(List<BindleBusiness> bindleBusinesses){
        this.bindleBusinesses = bindleBusinesses;
        notifyDataSetChanged();
    }
}
