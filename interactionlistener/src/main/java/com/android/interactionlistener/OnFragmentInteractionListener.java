package com.android.interactionlistener;

import org.pursuit.firebasetools.model.Group;

public interface OnFragmentInteractionListener {

    void inflateStartGroupFragment();

    void inflateZoneChatFragment();

    void inflateProfileFragment(boolean bool);

    void onLoginSuccess();

    void openDirections(String VenueAddress);

    void inflateExploreGroupsFragment();

    void inflateViewGroupFragment(Group group);

    void inflateGroupChatFragment(Group group);
}
