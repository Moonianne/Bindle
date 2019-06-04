package org.pursuit.usolo.map.nearbygroups;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.interactionlistener.OnFragmentInteractionListener;

import org.pursuit.firebasetools.model.Group;
import org.pursuit.usolo.R;

public class NearbyGroupViewHolder extends RecyclerView.ViewHolder {

    private Button viewGroup;
    private TextView groupTitle, groupAddress;

    public NearbyGroupViewHolder(@NonNull View itemView) {
        super(itemView);

        viewGroup = itemView.findViewById(R.id.view_group_nearby_button);
        groupTitle = itemView.findViewById(R.id.venue_name_nearby);
        groupAddress = itemView.findViewById(R.id.venue_address_nearby);
    }

    public void onBind(Group group, final OnFragmentInteractionListener listener) {

        groupTitle.setText(group.getTitle());
        groupAddress.setText(group.getAddress());
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.inflateViewGroupFragment(group);
            }
        });
    }
}
