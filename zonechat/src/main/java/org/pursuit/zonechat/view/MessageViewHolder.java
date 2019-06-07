package org.pursuit.zonechat.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.interactionlistener.OnFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import org.pursuit.firebasetools.model.Message;
import org.pursuit.zonechat.R;
import org.pursuit.zonechat.viewmodel.ChatViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

final class MessageViewHolder extends RecyclerView.ViewHolder {

    MessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    final void onBind(@NonNull final Message message,
                      @NonNull final ChatViewModel viewModel,
                      OnFragmentInteractionListener listener) {

        CircleImageView profilePhoto;
        profilePhoto = itemView.findViewById(R.id.profile_image);
        itemView.<TextView>findViewById(R.id.nameTextView).setText(message.getUserName());
        itemView.<TextView>findViewById(R.id.messageTextView).setText(message.getMessageText());
        itemView.<TextView>findViewById(R.id.timeTV).setText(viewModel.convertToReadableTime(message));

        if ( message.getPhotoUrl() == null || message.getPhotoUrl().equals("")) {
            profilePhoto.setImageResource(R.drawable.ic_account_circle_blue_24dp);
        } else {
            Picasso.get()
              .load(message.getPhotoUrl())
              .into(profilePhoto);
        }

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.inflateProfileFragment(false, message.getUserID());
            }
        });
    }

}
