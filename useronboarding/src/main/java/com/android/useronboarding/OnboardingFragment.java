package com.android.useronboarding;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class OnboardingFragment extends Fragment {


    public OnboardingFragment() {
    }

    public static OnboardingFragment getInstance() {
        return new OnboardingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.<EditText>findViewById(R.id.editText_display_name);
        view.<EditText>findViewById(R.id.editText_about_me);
    }
}
