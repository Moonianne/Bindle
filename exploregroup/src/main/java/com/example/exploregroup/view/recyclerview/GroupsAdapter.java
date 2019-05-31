package com.example.exploregroup.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.exploregroup.R;

import org.pursuit.firebasetools.model.Group;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class GroupsAdapter extends RecyclerView.Adapter<GroupsViewHolder> {

    private List<Group> groupList;

    public GroupsAdapter(@NonNull List<Group> groupList) {
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GroupsViewHolder(LayoutInflater
          .from(viewGroup.getContext()).inflate(R.layout.groups_item_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsViewHolder groupsViewHolder, int i) {
        groupsViewHolder.onBind(groupList.get(i));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void addData(@NonNull final Group group) {
        if (groupList.size() == 0) {
            groupList = new LinkedList<>();
        }
        groupList.add(group);
        notifyDataSetChanged();
    }
}
