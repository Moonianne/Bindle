package com.android.group.view;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.group.R;
import com.android.group.model.Venue;
import com.android.group.viewmodel.GroupViewModel;
import com.android.group.viewmodel.NetworkViewModel;


public class StartGroupFragment extends Fragment implements NetworkViewModel.OnVenueSelectedListener {

    private static final String TAG = "StartGroupFragment";
    private View rootView;
    private NetworkViewModel networkViewModel;
    private GroupViewModel groupViewModel;
    private OnFragmentInteractionListener interactionListener;
    private OnFragmentInteractionCompleteListener interactionCompleteListener;
    private EditText groupNameEditText;
    private ViewGroup addLocation;
    private TextView addLocationTextView;
    private EditText groupDescriptionEditText;
    private Button startGroupButton;
    private Venue userSelectedVenue;

    public StartGroupFragment() {
    }

    public static StartGroupFragment newInstance() {
        return new StartGroupFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Context does not implement OnFragmentInteractionListener");
        }

        if (context instanceof OnFragmentInteractionCompleteListener) {
            interactionCompleteListener = (OnFragmentInteractionCompleteListener) context;
        } else {
            throw new RuntimeException("Context does not implement OnFragmentInteractionCompleteListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_start_group, container, false);
        findViews();
        initViewModels();
        setOnVenueSelectedListener();
        setAddLocationGroupListener();
        setStartButtonOnClickListener();
        return rootView;
    }

    @Override
    public void venueSelected(Venue venue) {
        userSelectedVenue = venue;
        addLocationTextView.setText(userSelectedVenue.getLocation().getAddress());
    }

    private void findViews() {
        groupNameEditText = rootView.findViewById(R.id.group_name_edit_text);
        addLocation = rootView.findViewById(R.id.add_location_container);
        addLocationTextView = rootView.findViewById(R.id.add_location_text_view);
        groupDescriptionEditText = rootView.findViewById(R.id.group_description_edit_text);
        startGroupButton = rootView.findViewById(R.id.start_group_button);
    }

    private void initViewModels() {
        networkViewModel = NetworkViewModel.getSingleInstance();
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
    }

    private void setOnVenueSelectedListener() {
        networkViewModel.setVenueSelectedListener(this);
    }

    private void setAddLocationGroupListener() {
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactionListener.inflateAddLocationFragment();
            }
        });
    }

    private void setStartButtonOnClickListener() {
        startGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = groupNameEditText.getText().toString();
                String groupDescription = groupDescriptionEditText.getText().toString();
                if (!groupName.equals("")) {
                    if (!groupDescription.equals("")) {
                        if(userSelectedVenue != null){
                            groupViewModel.createGroup(groupName,userSelectedVenue,groupDescription);
                            interactionCompleteListener.closeFragment();
                        }else{
                            makeToast("Select a Location");
                        }
                    } else {
                        makeToast("Provide a Group Description");
                    }
                } else {
                    makeToast("Provide a Group Name");
                }
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void inflateAddLocationFragment();

    }
}
