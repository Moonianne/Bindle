package com.android.group.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.group.R;

public class VenueInfoFragment extends Fragment {


    public VenueInfoFragment() {
    }

    public static VenueInfoFragment newInstance(){
        return new VenueInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_venue_info, container, false);
    }
}
