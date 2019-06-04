package org.pursuit.usolo.map.nearbygroups;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.interactionlistener.OnFragmentInteractionListener;

import org.pursuit.firebasetools.model.Group;
import org.pursuit.usolo.R;

import java.util.List;

public class NearbyGroupAdapter extends RecyclerView.Adapter<NearbyGroupViewHolder> {

    private OnFragmentInteractionListener listener;
    private List<Group> groupList;

    public NearbyGroupAdapter(OnFragmentInteractionListener listener, List<Group> groupList) {
        this.listener = listener;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public NearbyGroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nearby_item_layout, viewGroup, false);
        return new NearbyGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyGroupViewHolder nearbyGroupViewHolder, int pos) {
        nearbyGroupViewHolder.onBind(groupList.get(pos), listener);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public void updateList(List<Group> newGroupList) {
        groupList = newGroupList;
        notifyDataSetChanged();
    }
}
