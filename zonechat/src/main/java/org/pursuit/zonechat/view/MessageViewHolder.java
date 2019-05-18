package org.pursuit.zonechat.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.pursuit.zonechat.R;
import org.pursuit.zonechat.model.Message;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView messageTextView;
    private TextView messengerNameTextView;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        messageTextView = itemView.findViewById(R.id.messageTextView);
        messengerNameTextView = itemView.findViewById(R.id.nameTextView);
    }

    public void onBind(final Message message){
        messengerNameTextView.setText(message.getName());
        messageTextView.setText(message.getMessage());
    }
}
