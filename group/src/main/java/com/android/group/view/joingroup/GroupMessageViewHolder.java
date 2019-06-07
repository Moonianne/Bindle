package com.android.group.view.joingroup;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.group.R;
import com.android.group.viewmodel.GroupChatViewModel;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import org.pursuit.firebasetools.model.Message;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageViewHolder extends RecyclerView.ViewHolder {
    public GroupMessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    final void onBind(@NonNull final Message message,
                      @NonNull final GroupChatViewModel viewModel,
                      OnFragmentInteractionListener listener) {

        CircleImageView profilePhoto;
        profilePhoto = itemView.findViewById(R.id.group_photImage);

        itemView.<TextView>findViewById(R.id.group_nameTextView).setText(message.getUserName());
        itemView.<TextView>findViewById(R.id.group_messageTextView).setText(message.getMessageText());
        itemView.<TextView>findViewById(R.id.group_timeTV).setText(viewModel.convertToReadableTime(message));

        if ( message.getPhotoUrl() == null || message.getPhotoUrl().equals("")) {
            profilePhoto.setImageResource(R.drawable.ic_account_circle_purp_24dp);
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
