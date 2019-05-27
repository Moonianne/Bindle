package com.android.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.interactionlistener.OnFragmentInteractionListener;

import java.util.Objects;

public final class AuthenticationFragment extends Fragment {
    private static final String LOGIN_PREFS = "USER_LOGIN";
    private static final String EMAIL_PREFS = "EMAIL_PREFS";
    private static final String PASS_PREFS = "PW_PREFS";

    private FireAuthRepo fireAuthRepo;
    private SharedPreferences preferences;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    private OnFragmentInteractionListener listener;

    public static AuthenticationFragment getInstance() {
        return new AuthenticationFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
              + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireAuthRepo = FireAuthRepo.getInstance();
        preferences =
          Objects.requireNonNull(getActivity()).getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authentication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextEmail = view.findViewById(R.id.editText_email);
        editTextPassword = view.findViewById(R.id.editText_password);
        progressBar = view.findViewById(R.id.progressBar);
        view.<Button>findViewById(R.id.button_login).setOnClickListener(v -> {
            if (isValidField()) {
                if (fireAuthRepo.getUser() != null) {
                    fireAuthRepo.getUser().reload();
                    fireAuthRepo.reAuthenticateUser(getEmail(), getPassword());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(EMAIL_PREFS, getEmail()).apply();
                    editor.putString(PASS_PREFS, getPassword()).apply();
                    if (fireAuthRepo.isEmailVerified()) {
                        fireAuthRepo.login(getEmail(),
                          getPassword(),
                          (isSuccess, userID) -> {
                              if (isSuccess) {
                                  Log.d("sign in", "successful: " + userID);
                                  Toast.makeText(getContext(),
                                    "Welcome Back!",
                                    Toast.LENGTH_SHORT).show();
                                  progressBar.setVisibility(View.INVISIBLE);
                                  onButtonPressed();
                              } else {
                                  Log.d("sign in ", "failure ");
                                  progressBar.setVisibility(View.INVISIBLE);
                              }
                              return false;
                          });
                        editor.putString(EMAIL_PREFS, getEmail()).apply();
                        editor.putString(PASS_PREFS, getPassword()).apply();
                    } else {
                        Toast.makeText(getContext(),
                          "Please Verify Email to Proceed",
                          Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(),
                      "Wrong Email/Password, Try Again",
                      Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        view.findViewById(R.id.button_signup).setOnClickListener(v -> {
            // TODO: 2019-05-15 Bring user to Questions Fragment, then call registeruser() from there
            if (isValidField()) {
                registerUser();
            }
        });
    }

    public void onButtonPressed() {
        if (listener != null) {
            listener.onLoginSuccess();
        }
    }

    private String getPassword() {
        return editTextPassword.getText().toString().trim();
    }

    private String getEmail() {
        return editTextEmail.getText().toString().trim();
    }


    private boolean isValidField() {
        if (TextUtils.isEmpty(getEmail())) {
            editTextEmail.setError("Email Required");
            editTextEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()) {
            editTextEmail.setError("Please Enter Valid Email Address");
            editTextEmail.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(getPassword())) {
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return false;
        }
        progressBar.setVisibility(View.VISIBLE);
        return true;
    }

    private void registerUser() {
        fireAuthRepo.registerUser(editTextEmail.getText().toString(),
          editTextPassword.getText().toString(),
          isSuccess -> {
              if (isSuccess) {
                  progressBar.setVisibility(View.INVISIBLE);
                  SharedPreferences.Editor editor = preferences.edit();
                  editor.putString(EMAIL_PREFS, getEmail()).apply();
                  editor.putString(PASS_PREFS, getPassword()).apply();
                  Toast.makeText(
                    getContext(),
                    "Registration Successful",
                    Toast.LENGTH_SHORT).show();
                  fireAuthRepo.sendVerificationEmail(isSuccess1 -> {
                      if (isSuccess1) {
                          Toast.makeText(getContext(),
                            "Verification Email Sent",
                            Toast.LENGTH_SHORT).show();
                          return true;
                      } else {
                          Log.d("verification", "not sent: ");
                          return false;
                      }
                  });
                  fireAuthRepo.setUser();
                  fireAuthRepo.sendUserData();
                  return true;
              } else {
                  Toast.makeText(
                    getContext(),
                    "Error, please try again",
                    Toast.LENGTH_SHORT).show();
                  Log.d("createuser", "error: ");
                  progressBar.setVisibility(View.INVISIBLE);
                  return false;
              }
          });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
