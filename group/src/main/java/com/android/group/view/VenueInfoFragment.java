package com.android.group.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.R;
import com.android.group.model.Venue;

import java.util.ArrayList;
import java.util.List;

public class VenueInfoFragment extends Fragment {
    private String venueName;
    private ArrayList<String> venueAddress;
    private ImageView imageViewVenue;
    private TextView textViewVenueName;
    private TextView textViewVenueAddress;

    public VenueInfoFragment() {
    }

    public static VenueInfoFragment newInstance(String name, List<String> formattedAddress){
        VenueInfoFragment venueInfoFragment = new VenueInfoFragment();
        Bundle args = new Bundle();
        args.putString("venueName", name);
        args.putStringArrayList("address", (ArrayList<String>) formattedAddress);
        venueInfoFragment.setArguments(args);
        return venueInfoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            venueName = getArguments().getString("venueName");
            venueAddress = getArguments().getStringArrayList("address");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_venue_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        textViewVenueName.setText(venueName);
        textViewVenueAddress.setText(venueAddress.get(0));
    }

    private void findViews(@NonNull View view) {
        imageViewVenue = view.findViewById(R.id.imageView_venue_picture);
        textViewVenueName = view.findViewById(R.id.textView_venue_name);
        textViewVenueAddress = view.findViewById(R.id.textView_venue_address);
    }
}
