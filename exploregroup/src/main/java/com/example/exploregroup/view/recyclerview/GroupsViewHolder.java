package com.example.exploregroup.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.interactionlistener.OnFragmentInteractionListener;
import com.example.exploregroup.R;

import org.pursuit.firebasetools.model.Group;

final class GroupsViewHolder extends RecyclerView.ViewHolder {

    GroupsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void onBind(@NonNull final Group group) {
        itemView.<TextView>findViewById(R.id.group_name_text_view).setText(group.getTitle());
        itemView.<TextView>findViewById(R.id.group_category_text_view).setText(group.getCategory());
        itemView.<TextView>findViewById(R.id.group_description_text_view).setText(group.getDescription());
        itemView.findViewById(R.id.view_group_button).setOnClickListener(v ->
                ((OnFragmentInteractionListener) itemView.getContext()).inflateViewGroupFragment());
    }
}
