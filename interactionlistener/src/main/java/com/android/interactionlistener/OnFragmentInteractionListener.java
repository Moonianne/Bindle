package com.android.interactionlistener;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import org.pursuit.firebasetools.model.Group;

public interface OnFragmentInteractionListener {

    void inflateStartGroupFragment();

    void inflateZoneChatFragment(@NonNull final String zoneName);

    void inflateProfileFragment(boolean bool);

    void onLoginSuccess();

    void openDirections(String VenueAddress);

    void inflateExploreGroupsFragment();

    void inflateViewGroupFragment(Group group);

    void inflateGroupChatFragment(Group group);
}
