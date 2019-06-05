package org.pursuit.userprofile.view;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.jakewharton.rxbinding3.view.RxView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.pursuit.userprofile.R;
import org.pursuit.userprofile.viewmodel.ProfileViewModel;

import java.io.File;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public final class ProfileView extends Fragment {

    private static final int REQUEST_GET_SINGLE_FILE = 314;
    private static final String PROFILE_PREFS = "PROFILE";

    private static boolean isCurrentUserProfile = true;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private ProfileViewModel profileViewModel;
    private EditText aboutMeEditText, interestsEditText;
    private ImageView editAboutMeButton, editInterestsButton, finishInterestsButton, finishAboutMeButton, profilePhoto;
    private TextView displayNameView, aboutMeView, interestsView, locationView;
    private Button logOutButton, uploadPhotoButton, editDisplayName;

    public static ProfileView newInstance(boolean bool) {
        isCurrentUserProfile = bool;
        return new ProfileView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedPreferences = context.getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        if (isCurrentUserProfile) {
            compositeDisposable.add(profileViewModel.getCurrentUserInfo()
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(user -> {
                  if (user != null) {
                      profileViewModel.setCurrentUser(user);
                      if (user.getInterests() != null) {
                          interestsView.setText(user.getInterests());
                      }
                      if (user.getAboutMe() != null) {
                          aboutMeView.setText(user.getAboutMe());
                      }
                      if (user.getUserProfilePhotoURL() != null) {
                          Picasso.get()
                            .load(user.getUserProfilePhotoURL())
                            .rotate(-90f)
                            .into(profilePhoto);
                      }
                      locationView.setText(profileViewModel.getLocation(getContext()));
                  }
              }));
            displayNameView.setText(profileViewModel.getUsername());
            compositeDisposable.add(RxView.clicks(editAboutMeButton)
              .subscribe(unit -> setAboutMeVisibility(true)));
            compositeDisposable.add(RxView.clicks(finishAboutMeButton)
              .observeOn(Schedulers.io())
              .doOnNext(click ->
                profileViewModel
                  .setUserAboutMe(
                    aboutMeEditText
                      .getText().toString()))
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(unit -> {
                  aboutMeView
                    .setText(aboutMeEditText.getText().toString());
                  setAboutMeVisibility(false);
              }));
            compositeDisposable.add(RxView.clicks(editInterestsButton)
              .subscribe(unit1 -> {
                  setInterestsVisibility(true);
                  compositeDisposable.add(RxView.clicks(finishInterestsButton)
                    .observeOn(Schedulers.io())
                    .doOnNext(click ->
                      profileViewModel
                        .setUserInterests(
                          interestsEditText
                            .getText().toString()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(click -> {
                        interestsView
                          .setText(interestsEditText.getText().toString());
                        setInterestsVisibility(false);
                    }));
              }));
            compositeDisposable.add(RxView.clicks(uploadPhotoButton)
              .subscribe(unit -> startActivityForResult(Intent
                .createChooser(profileViewModel.getPhotoIntent(),
                  "Select Picture"), REQUEST_GET_SINGLE_FILE)));
            compositeDisposable.add(RxView.clicks(editDisplayName).
                    subscribe(unit -> {
                                View dialogView = LayoutInflater.from(getContext())
                                        .inflate(R.layout.edit_display_name_layout, null);
                                EditText editDisplayNameEditText = dialogView.findViewById(R.id.edit_display_name_edit_text);
                                new AlertDialog.Builder(getContext())
                                        .setView(dialogView)
                                        .setTitle("Enter Display Name")
                                        .setPositiveButton("Submit", (dialog, which) -> {
                                            if (editDisplayNameEditText.getText() != null &&
                                                    !editDisplayNameEditText.getText().equals("")) {
                                                profileViewModel.updateDisplayName(editDisplayNameEditText.getText().toString())
                                                .addOnSuccessListener(aVoid -> displayNameView.setText(profileViewModel.getUsername()));
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                    ));

            logOutButton.setOnClickListener(v -> {
                //TODO firebase needed for the user log out
            });
        } else {
            setExtraViewsVisibility();
            displayNameView.setText(profileViewModel.getUsername());
            locationView.setText(profileViewModel.getLocation(getContext()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    Uri selectedImageUri = data.getData();
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    if (selectedImageUri != null) {
                        profileViewModel.pushPhoto(selectedImageUri, sharedPreferences.edit());
                    }
                    profilePhoto.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver()
          .query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void setExtraViewsVisibility() {
        aboutMeEditText.setVisibility(View.INVISIBLE);
        finishAboutMeButton.setVisibility(View.INVISIBLE);
        editAboutMeButton.setVisibility(View.INVISIBLE);
        interestsEditText.setVisibility(View.INVISIBLE);
        finishInterestsButton.setVisibility(View.INVISIBLE);
        editInterestsButton.setVisibility(View.INVISIBLE);
        logOutButton.setVisibility(View.INVISIBLE);
        uploadPhotoButton.setVisibility(View.INVISIBLE);
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
        profilePhoto = view.findViewById(R.id.profilephoto);
        locationView = view.findViewById(R.id.locationView);
        displayNameView = view.findViewById(R.id.profile_display_name);
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
        editDisplayName = view.findViewById(R.id.edit_display_name_button);
    }
}
