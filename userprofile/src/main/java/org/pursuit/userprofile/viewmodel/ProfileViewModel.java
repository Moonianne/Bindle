package org.pursuit.userprofile.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.pursuit.firebasetools.Repository.FireRepo;

public class ProfileViewModel extends ViewModel {

    private FireRepo fireRepo = FireRepo.getInstance();


    public String getUsername (){
        Log.d("profileViewModel - ", "email: " + fireRepo.getUser().getEmail());
        return fireRepo.getUser().getEmail();
    }



}
