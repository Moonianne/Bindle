package com.android.interactionlistener;


import java.util.List;

public interface OnFragmentInteractionListener {

    void inflateStartGroupFragment();
    void inflateZoneChatFragment();
    void inflateVenueInfoFragment(String name, List formattedAddress);

}
