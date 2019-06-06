package org.pursuit.zonechat.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.pursuit.firebasetools.model.Message;
import org.pursuit.zonechat.R;
import org.pursuit.zonechat.viewmodel.ChatViewModel;

final class MessageViewHolder extends RecyclerView.ViewHolder {

    MessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    final void onBind(@NonNull final Message message,
                      @NonNull final ChatViewModel viewModel) {
        itemView.<TextView>findViewById(R.id.nameTextView).setText(message.getUserName());
        itemView.<TextView>findViewById(R.id.messageTextView).setText(message.getMessageText());
        itemView.<TextView>findViewById(R.id.timeTV).setText(viewModel.convertToReadableTime(message));

        Picasso.get()
          .load(user.getUserProfilePhotoURL())
          .rotate(-90f)
          .into(profilePhoto);
    }
}
