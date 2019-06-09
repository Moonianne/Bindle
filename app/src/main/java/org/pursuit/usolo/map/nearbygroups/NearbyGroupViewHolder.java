package org.pursuit.usolo.map.nearbygroups;

import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.interactionlistener.OnFragmentInteractionListener;

import org.pursuit.firebasetools.model.Group;
import org.pursuit.usolo.R;

final class NearbyGroupViewHolder extends RecyclerView.ViewHolder {
    NearbyGroupViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    final void onBind(@NonNull final Group group,
                      @NonNull final OnFragmentInteractionListener listener) {
        itemView.<TextView>findViewById(R.id.venue_name_nearby).setText(group.getTitle());
        itemView.<TextView>findViewById(R.id.venue_address_nearby).setText(group.getAddress());
        itemView.<Button>findViewById(R.id.view_group_nearby_button).setOnClickListener(v ->
          listener.inflateViewGroupFragment(group));
    }
}
