package org.pursuit.userprofile.viewmodel;

import android.Manifest;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.google.android.gms.tasks.Task;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;


public class ProfileViewModel extends ViewModel {

    private final FireRepo fireRepo = FireRepo.getInstance();
    private User user;

    public ProfileViewModel() {
        user = new User();
        user.setUserId(fireRepo.getCurrentUser().getUid());
    }

    public String getUsername() {
        return fireRepo.getCurrentUser().getDisplayName();
    }

    public String getLocation(Context context) {
        Location location;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
          ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(locationManager.getAllProviders().get(0));
        } else {
            return "unknown";
        }
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
              location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getSubLocality();
        String stateName = addresses.get(0).getAdminArea();
        String countryName = addresses.get(0).getCountryCode();
        String currentLocation = cityName + ", " + stateName + ", " + countryName;
        setUserLocation(currentLocation);
        return currentLocation;
    }

    public Intent getPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        return intent;
    }

    public Maybe<User> getCurrentUserInfo() {
        return fireRepo
          .getUserInfo(fireRepo.getCurrentUser().getUid());
    }

    private void pushUser() {
        fireRepo.pushUser(user);
    }

    public void pushPhoto(@NonNull final Uri uri,
                          @NonNull final SharedPreferences.Editor editor) {
        Completable.fromAction(() ->
          fireRepo.uploadFile(uri, editor,
            uri1 -> setUserPhoto(uri1.toString()))
        ).subscribeOn(Schedulers.io())
          .subscribe();
    }

    private void setUserPhoto(@NonNull final String url) {
        user.setUserProfilePhotoURL(url);
        pushUser();
    }

    public void setUserAboutMe(@NonNull final String text) {
        user.setAboutMe(text);
        pushUser();
    }

    public void setUserInterests(@NonNull final String text) {
        user.setInterests(text);
        pushUser();
    }

    private void setUserLocation(@NonNull final String text) {
        user.setCurrentLocation(text);
        pushUser();
    }

    public void setCurrentUser(@NonNull final User user) {
        this.user = user;
    }

    public Task<Void> updateDisplayName(String displayName){
        return fireRepo.updateDisplayName(displayName);
    }
}
