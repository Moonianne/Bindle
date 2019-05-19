package com.android.authentication;


import android.content.Context;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public final class AuthenticationFragment extends Fragment{
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private DatabaseReference database;
//    private OnInteractionListener onInteractionListener;
    // TODO: 2019-05-19 implement interaction listener

    public static AuthenticationFragment getInstance() {
        return new AuthenticationFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnInteractionListener) {
//            onInteractionListener = (OnInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//              + " must implement OnInteractionListener");
//        }
        // TODO: 2019-05-19 implement interaction listener
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        database = FirebaseDatabase.getInstance().getReference(); //todo throw in view model
        auth = FirebaseAuth.getInstance();
        view.findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidField()) {
                    final FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        user.reload();
                        reAuthenticateUser(getEmail(), getPassword());
                        if (user.isEmailVerified()) {
                            signIn();
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
            }
        });
        view.findViewById(R.id.button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019-05-15 Bring user to Questions Fragment, then call registeruser() from there
                if (isValidField()) {
                    registerUser();
                }
            }
        });
    }

    public void onButtonPressed() {
//        if (onInteractionListener != null) {
//            onInteractionListener.onLoginSelected();
//        }
        // TODO: 2019-05-19 implement interaction listener
    }

    private String getPassword() {
        return editTextPassword.getText().toString().trim();
    }

    private String getEmail() {
        return editTextEmail.getText().toString().trim();
    }

    private void signIn() {
        auth.signInWithEmailAndPassword(getEmail(), getPassword())
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()) {
                      //sign in successful update UI with user's information
                      final FirebaseUser user = auth.getCurrentUser();
                      String uid = null;
                      if (user != null) {
                          uid = user.getUid();
                      }
                      Log.d("sign in", "successful: " + uid);
                      Toast.makeText(getContext(),
                        "Welcome Back!",
                        Toast.LENGTH_SHORT).show();
                      progressBar.setVisibility(View.INVISIBLE);
                      onButtonPressed();

                  } else {
                      Log.d("sign in ", "failure " + task.getException());
                      progressBar.setVisibility(View.INVISIBLE);
                  }
              }
          });
    }

    private void sendVerificationEmail() {
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()) {
                          Toast.makeText(getContext(),
                            "Verification Email Sent",
                            Toast.LENGTH_SHORT).show();
                      } else {
                          Log.d("verification", "not sent: " + task.getException());
                      }
                  }
              });
        }
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
        auth.createUserWithEmailAndPassword(getEmail(), getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(
                      getContext(),
                      "Registration Successful",
                      Toast.LENGTH_SHORT).show();
                    sendVerificationEmail();
                    sendUserData();
                } else {
                    Toast.makeText(
                      getContext(),
                      "Error, please try again",
                      Toast.LENGTH_SHORT).show();
                    Log.d("createuser", "error: " + task.getException());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void sendUserData() {
        database.getRoot()
          .child("users")
          .child(Objects.requireNonNull(auth.getUid()))
          .child("email").setValue(getEmail());
    }

    public void reAuthenticateUser(String email, String password) {
        final AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
        Objects.requireNonNull(auth.getCurrentUser()).reauthenticate(authCredential);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        onInteractionListener = null;
        // TODO: 2019-05-19 implement on interaction listener
    }
}

