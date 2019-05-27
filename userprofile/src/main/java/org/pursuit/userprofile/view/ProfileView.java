package org.pursuit.userprofile.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.pursuit.userprofile.R;
import org.pursuit.userprofile.viewmodel.ProfileViewModel;


public class ProfileView extends Fragment {

    private ProfileViewModel profileViewModel;
    private EditText messageEditText;
    private Button sendButton;

    public ProfileView() {
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileView newInstance() {
        ProfileView fragment = new ProfileView();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_view, container, false);
    }

}
