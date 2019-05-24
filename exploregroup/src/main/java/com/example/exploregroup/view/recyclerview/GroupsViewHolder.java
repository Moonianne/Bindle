package com.example.exploregroup.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.group.model.firebase.Group;
import com.example.exploregroup.R;

public final class GroupsViewHolder extends RecyclerView.ViewHolder {

    public GroupsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void onBind(final Group group) {
        itemView.<TextView>findViewById(R.id.group_name_text_view).setText(group.getGroupName());
        itemView.<TextView>findViewById(R.id.group_category_text_view).setText(group.getCategory());
        itemView.<TextView>findViewById(R.id.group_description_text_view).setText(group.getDescription());
    }
}
