package org.pursuit.userprofile.view;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.pursuit.userprofile.R;
import org.pursuit.userprofile.viewmodel.ProfileViewModel;


public class ProfileView extends Fragment {

    private ProfileViewModel profileViewModel;
    private EditText aboutMeEditText, interestsEditText;
    private ImageView editAboutMeButton, editInterestsButton, finishInterestsButton, finishAboutMeButton;
    private TextView displaynameView, aboutMeView, interestsView;
    private Button logOutButton, uploadPhotoButton;

    public ProfileView() {
    }

    public static ProfileView newInstance() {
        return new ProfileView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

        editAboutMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAboutMeVisibility(true);

                finishAboutMeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO send data to firebase
                        setAboutMeVisibility(false);
                    }
                });
            }
        });

        editInterestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInterestsVisibility(true);

                finishInterestsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setInterestsVisibility(false);
                    }
                });
            }
        });

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO intent for photo and store profile photo in firebase
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO firebase needed for the user log out
            }
        });

        displaynameView.setText(profileViewModel.getUsername());
    }

    private void setAboutMeVisibility(boolean visibility) {

        if (visibility) {
            aboutMeView.setVisibility(View.INVISIBLE);
            editAboutMeButton.setVisibility(View.INVISIBLE);
            aboutMeEditText.setVisibility(View.VISIBLE);
            finishAboutMeButton.setVisibility(View.VISIBLE);
        } else {
            aboutMeEditText.setVisibility(View.INVISIBLE);
            finishAboutMeButton.setVisibility(View.INVISIBLE);
            aboutMeView.setVisibility(View.VISIBLE);
            editAboutMeButton.setVisibility(View.VISIBLE);
        }

    }

    private void setInterestsVisibility(boolean visibility) {

        if (visibility) {
            interestsView.setVisibility(View.INVISIBLE);
            editInterestsButton.setVisibility(View.INVISIBLE);
            interestsEditText.setVisibility(View.VISIBLE);
            finishInterestsButton.setVisibility(View.VISIBLE);
        } else {
            interestsEditText.setVisibility(View.INVISIBLE);
            finishInterestsButton.setVisibility(View.INVISIBLE);
            interestsView.setVisibility(View.VISIBLE);
            editInterestsButton.setVisibility(View.VISIBLE);
        }

    }

    private void findViews(@NonNull View view) {
        displaynameView = view.findViewById(R.id.profile_display_name);
        aboutMeView = view.findViewById(R.id.aboutme_textview);
        interestsView = view.findViewById(R.id.interests_textview);
        aboutMeEditText = view.findViewById(R.id.aboutMe_editText);
        interestsEditText = view.findViewById(R.id.interest_editText);
        editAboutMeButton = view.findViewById(R.id.edit_aboutMe_button);
        editInterestsButton = view.findViewById(R.id.edit_interestsButton);
        finishInterestsButton = view.findViewById(R.id.finishEdit_interestsButton);
        finishAboutMeButton = view.findViewById(R.id.finishEdit_aboutMe_button);
        logOutButton = view.findViewById(R.id.logOut_button);
        uploadPhotoButton = view.findViewById(R.id.upload_profile_photoButton);
    }
}
