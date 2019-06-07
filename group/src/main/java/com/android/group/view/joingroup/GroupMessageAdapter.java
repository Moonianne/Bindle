package com.android.group.view.joingroup;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.group.R;
import com.android.group.viewmodel.GroupChatViewModel;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.pursuit.firebasetools.model.Message;

public class GroupMessageAdapter extends FirebaseRecyclerAdapter<Message, GroupMessageViewHolder> {

    GroupChatViewModel viewModel;
    private OnFragmentInteractionListener listener;


    public GroupMessageAdapter(@NonNull FirebaseRecyclerOptions<Message> options,
                               @NonNull final GroupChatViewModel viewModel,
                               OnFragmentInteractionListener listener) {
        super(options);
        this.viewModel = viewModel;
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupMessageViewHolder holder, int position, @NonNull Message model) {
        holder.onBind(model, viewModel, listener);
    }

    @NonNull
    @Override
    public GroupMessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GroupMessageViewHolder(
          LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.group_message_item_view, viewGroup, false));
    }

}
