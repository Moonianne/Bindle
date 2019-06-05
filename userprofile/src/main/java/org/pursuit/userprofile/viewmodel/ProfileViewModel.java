package org.pursuit.userprofile.viewmodel;

import android.Manifest;
import android.arch.lifecycle.ViewModel;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import org.pursuit.firebasetools.Repository.FireRepo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ProfileViewModel extends ViewModel {

    private final FireRepo fireRepo = FireRepo.getInstance();

    public String getUsername() {
        Log.d("profileViewModel - ", "email: " + fireRepo.getUser().getEmail());
        return fireRepo.getUser().getEmail();
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
        return cityName + ", " + stateName + ", " + countryName;
    }

    public Intent getPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        return intent;
    }

    public void pushPhoto(@NonNull final Uri uri, SharedPreferences.Editor editor) {
        fireRepo.uploadFile(uri, editor);
    }
}
