package com.android.interactionlistener;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Zone;

public interface OnFragmentInteractionListener {

    void inflateStartGroupFragment();

    void inflateZoneChatFragment(@NonNull final Zone zone);

    void inflateProfileFragment(boolean bool, String userID);

    void onLoginSuccess();

    void inflateAuthenticationFragment();

    void clearSharedPreferences();

    void openDirections(String VenueAddress);

    void inflateExploreGroupsFragment();

    void inflateViewGroupFragment(Group group);

    void inflateGroupChatFragment(Group group);
}
