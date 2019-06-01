package com.android.group.view.joingroup;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.group.R;
import com.android.group.viewmodel.GroupChatViewModel;

import org.pursuit.firebasetools.model.Message;

public class GroupMessageViewHolder extends RecyclerView.ViewHolder {
    public GroupMessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    final void onBind(@NonNull final Message message, @NonNull final GroupChatViewModel viewModel) {
        itemView.<TextView>findViewById(R.id.group_nameTextView).setText(message.getUserName());
        itemView.<TextView>findViewById(R.id.group_messageTextView).setText(message.getMessageText());
        itemView.<TextView>findViewById(R.id.group_timeTV).setText(viewModel.convertToReadableTime(message));
    }
}
