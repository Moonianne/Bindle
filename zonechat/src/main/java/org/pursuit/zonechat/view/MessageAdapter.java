package org.pursuit.zonechat.view;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.pursuit.firebasetools.model.Message;
import org.pursuit.zonechat.R;
import org.pursuit.zonechat.viewmodel.ChatViewModel;

public final class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {
    private final ChatViewModel chatViewModel;

    MessageAdapter(@NonNull FirebaseRecyclerOptions<Message> options,
                   @NonNull final ChatViewModel chatViewModel) {
        super(options);
        this.chatViewModel = chatViewModel;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder,
                                    int position, @NonNull Message model) {
        holder.onBind(model, chatViewModel);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MessageViewHolder(
          LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.message_item_view, viewGroup, false));
    }
}
