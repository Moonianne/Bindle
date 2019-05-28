package com.example.exploregroup.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.exploregroup.R;

import org.pursuit.firebasetools.model.Group;

public final class GroupsViewHolder extends RecyclerView.ViewHolder {

    public GroupsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void onBind(final Group group) {
        itemView.<TextView>findViewById(R.id.group_name_text_view).setText(group.getChatName());
        itemView.<TextView>findViewById(R.id.group_category_text_view).setText(group.getCategory());
        itemView.<TextView>findViewById(R.id.group_description_text_view).setText(group.getDescription());
    }
}
