package org.pursuit.zonechat.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.pursuit.zonechat.R;
import org.pursuit.zonechat.model.Message;

final class MessageViewHolder extends RecyclerView.ViewHolder {

    MessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    final void onBind(@NonNull final Message message) {
        itemView.<TextView>findViewById(R.id.nameTextView).setText(message.getName());
        itemView.<TextView>findViewById(R.id.messageTextView).setText(message.getMessage());
        itemView.<TextView>findViewById(R.id.timeTV).setText(String.valueOf(message.getTimeStamp()));
    }
}
