package com.android.group.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.group.R;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import org.pursuit.firebasetools.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class BindlerViewHolder extends RecyclerView.ViewHolder {

    CircleImageView profileImage;
    TextView displayName;

    public BindlerViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.bindler_icon);
        displayName = itemView.findViewById(R.id.bindler_displayName);
    }

    public void onBind(OnFragmentInteractionListener listener, User user) {

        displayName.setText(user.getDisplayName());

        Picasso.get().load(user.getUserProfilePhotoURL()).into(profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.inflateProfileFragment(false, user.getUserId());
            }
        });
    }
}
