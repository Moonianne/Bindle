package com.android.group.view;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public final class StartGroupFragment extends Fragment
  implements NetworkViewModel.OnVenueSelectedListener {

    private NetworkViewModel networkViewModel;
    private GroupViewModel groupViewModel;
    private OnFragmentInteractionListener interactionListener;
    private OnFragmentInteractionCompleteListener interactionCompleteListener;
    private TextView locationTextView;
    private Venue userSelectedVenue;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkViewModel = NetworkViewModel.getSingleInstance();
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        networkViewModel.setVenueSelectedListener(this);
        return inflater.inflate(R.layout.fragment_start_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.<ViewGroup>findViewById(R.id.add_location_container)
          .setOnClickListener(v -> interactionListener.inflateAddLocationFragment());
        locationTextView = view.findViewById(R.id.add_location_text_view);
        setStartButtonOnClickListener(view);
    }

    @Override
    public void venueSelected(Venue venue) {
        userSelectedVenue = venue;
        locationTextView.setText(userSelectedVenue.getLocation().getAddress());
    }

    private void setStartButtonOnClickListener(View view) {
        view.<Button>findViewById(R.id.start_group_button).setOnClickListener(v -> {
            final String groupName =
              view.<EditText>findViewById(R.id.group_name_edit_text)
                .getText().toString();
            final String groupDescription =
              view.<EditText>findViewById(R.id.group_description_edit_text)
                .getText().toString();
            if (!groupName.equals("")) {
                if (!groupDescription.equals("")) {
                    if (userSelectedVenue != null) {
                        groupViewModel.createGroup(groupName, userSelectedVenue, groupDescription);
                        interactionCompleteListener.closeFragment();
                    } else {
                        makeToast("Select a Location");
                    }
                } else {
                    makeToast("Provide a Group Description");
                }
            } else {
                makeToast("Provide a Group Name");
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
