package com.android.authentication;


import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public final class FireAuthRepo {

    private final FirebaseAuth auth;
    private final DatabaseReference database;
    private FirebaseUser user;

    private FireAuthRepo() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference(); //todo throw in view model
    }

    public FirebaseUser getUser() {
        if (user == null) {
            setUser();
        }
        return user;
    }

    public void setUser() {
        this.user = auth.getCurrentUser();
    }

    public boolean isEmailVerified() {
        setUser();
        return user.isEmailVerified();
    }

    public void login(String email,
                      String password,
                      OnLoginResultListener listener) {
        auth.signInWithEmailAndPassword(email, password)
          .addOnCompleteListener(task -> {
              Log.d("jimenez", "login: " + task.getException());
              final FirebaseUser user = auth.getCurrentUser();
              listener.onLoginListener(task.isSuccessful(), user != null ? user.getUid() : null);
          });
    }

    public void registerUser(String email,
                             String password,
                             OnRegistrationResultListener listener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            Log.d("jimenez", "registerUser: " + task.getException());
            listener.onRegistrationResult(task.isSuccessful());
        });
    }

    public void reAuthenticateUser(String email,
                                   String password) {
        final AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
        Objects.requireNonNull(auth.getCurrentUser()).reauthenticate(authCredential);
    }

    public void sendUserData() {
        setUser();
        database.getRoot()
          .child("users")
          .child(Objects.requireNonNull(auth.getUid()))
          .child("email").setValue(user.getEmail());
    }

    public void sendVerificationEmail(OnVerificationEmailSentListener listener) {
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
              .addOnCompleteListener(task -> {
                  Log.d("jimenez", "sendVerificationEmail: " + task.getException());
                  listener.onVerificationEmailSent(task.isSuccessful());
              });
        }
    }

    private static FireAuthRepo instance;

    public static FireAuthRepo getInstance() {
        if (instance == null) {
            instance = new FireAuthRepo();
        }
        return instance;
    }

    public interface OnLoginResultListener {
        boolean onLoginListener(boolean isSuccess, @Nullable String userID);
    }

    public interface OnRegistrationResultListener {
        boolean onRegistrationResult(boolean isSuccess);
    }

    public interface OnVerificationEmailSentListener {
        boolean onVerificationEmailSent(boolean isSuccess);
    }
}
