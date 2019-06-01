package com.android.interactionlistener;

public interface OnFragmentInteractionListener {

    void inflateStartGroupFragment();

    void inflateZoneChatFragment();

    void inflateProfileFragment(boolean bool);

    void onLoginSuccess();

    void openDirections(String VenueAddress);

    void inflateExploreGroupsFragment();

    void inflateViewGroupFragment();
}
