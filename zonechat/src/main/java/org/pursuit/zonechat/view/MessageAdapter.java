package org.pursuit.zonechat.view;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.pursuit.zonechat.R;
import org.pursuit.zonechat.model.Message;

public class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {

    public MessageAdapter(@NonNull FirebaseRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder,
                                    int position, @NonNull Message model) {
        holder.onBind(model);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MessageViewHolder(
          LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.message_item_view, viewGroup, false));
    }
}
