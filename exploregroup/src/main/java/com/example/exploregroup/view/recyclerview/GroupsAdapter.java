package com.example.exploregroup.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.group.model.firebase.Group;
import com.example.exploregroup.R;

import java.util.ArrayList;
import java.util.List;

public final class GroupsAdapter extends RecyclerView.Adapter<GroupsViewHolder> {

    private List<Group> groupList = new ArrayList<>();

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GroupsViewHolder(LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.groups_item_view,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsViewHolder groupsViewHolder, int i) {
        groupsViewHolder.onBind(groupList.get(i));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void setData(final List<Group> groupList){
        this.groupList = groupList;
        notifyDataSetChanged();
    }
}
