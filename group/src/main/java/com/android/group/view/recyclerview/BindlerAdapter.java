package com.android.group.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.group.R;
import com.android.interactionlistener.OnFragmentInteractionListener;

import org.pursuit.firebasetools.model.User;

import java.util.List;

public class BindlerAdapter extends RecyclerView.Adapter<BindlerViewHolder> {

    OnFragmentInteractionListener listener;
    List<User> userList;

    public BindlerAdapter(OnFragmentInteractionListener listener, List<User> userList) {
        this.listener = listener;
        this.userList = userList;
    }

    @NonNull
    @Override
    public BindlerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.icon_bindler_item, viewGroup, false);
        return new BindlerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BindlerViewHolder bindlerViewHolder, int pos) {
        bindlerViewHolder.onBind(listener, userList.get(pos));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setDataChange(List<User> userList) {
        if (userList != null) {
            notifyDataSetChanged();
            this.userList = userList;
        }
    }
}
