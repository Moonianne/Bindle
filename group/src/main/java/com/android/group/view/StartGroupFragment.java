package com.android.group.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.group.R;
import com.android.group.model.Venue;
import com.android.group.viewmodel.NetworkViewModel;


public class StartGroupFragment extends Fragment implements NetworkViewModel.OnVenueSelectedListener {

    private static final String TAG = "StartGroupFragment";
    private View rootView;
    private NetworkViewModel viewModel;
    private OnFragmentInteractionListener listener;
    private ViewGroup addLocation;
    private TextView addLocationTextView;

    public StartGroupFragment() {
    }

    public interface OnFragmentInteractionListener{
        void inflateAddLocationFragment();

    }
    public static StartGroupFragment getInstance(){
        return new StartGroupFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException("Context does not implement interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_start_group, container, false);
        findViews();
        initViewModel();
        setOnVenueSelectedListener();
        setAddLocationGroupListener();
        return rootView;
    }

    private void findViews() {
        addLocation = rootView.findViewById(R.id.add_location_container);
        addLocationTextView = rootView.findViewById(R.id.add_location_text_view);
    }

    private void initViewModel() {
        viewModel = NetworkViewModel.getSingleInstance();
    }

    private void setOnVenueSelectedListener() {
        viewModel.setVenueSelectedListener(this);
    }

    private void setAddLocationGroupListener() {
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.inflateAddLocationFragment();
            }
        });
    }

    @Override
    public void venueSelected(Venue venue) {
        addLocationTextView.setText(venue.getLocation().getAddress());
    }
}
