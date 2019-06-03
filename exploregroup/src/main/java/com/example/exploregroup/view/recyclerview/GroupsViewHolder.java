package com.example.exploregroup.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.interactionlistener.OnFragmentInteractionListener;
import com.example.exploregroup.R;
import com.squareup.picasso.Picasso;

import org.pursuit.firebasetools.model.Group;

final class GroupsViewHolder extends RecyclerView.ViewHolder {

    GroupsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void onBind(@NonNull final Group group) {
        Picasso.get().load(group.getImage_url()).into( itemView.<ImageView>findViewById(R.id.group_location_image_view));
        itemView.<TextView>findViewById(R.id.group_name_text_view).setText(group.getTitle().toUpperCase());
        itemView.<TextView>findViewById(R.id.group_category_text_view).setText(group.getCategory());
        itemView.<TextView>findViewById(R.id.group_location_address_text_view).setText(group.getAddress());
        itemView.findViewById(R.id.view_group_button).setOnClickListener(v ->
                ((OnFragmentInteractionListener) itemView.getContext()).inflateViewGroupFragment(group));
    }
}
