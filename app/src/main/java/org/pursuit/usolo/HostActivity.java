package org.pursuit.usolo;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.pursuit.usolo.map.MapFragment;
import org.pursuit.usolo.map.PermissionRequestListener;
import org.pursuit.usolo.map.model.Zone;

public final class HostActivity extends AppCompatActivity implements PermissionRequestListener {
    private static final String TAG = "HostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        loginToFirebase();
        requestUserLocationPermission();
        final String path = getString(R.string.firebase_path) + "2/";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        Zone zone = new Zone("Pursuit", new LatLng(40.7430877d, -73.9419287d), 0);
        ref.setValue(zone);

        inflateFragment(MapFragment.newInstance());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: " + grantResults.toString());
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
     public boolean requestUserLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
          ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        return (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
          ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void inflateFragment(Fragment fragment) {
        getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.main_container, fragment)
          .commit();
    }

    private void loginToFirebase() {
        // Authenticate with Firebase, and request location updates
        String email = getString(R.string.firebase_email);
        String password = getString(R.string.firebase_password);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
          email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "firebase auth success");
            } else {
                Log.d(TAG, "firebase auth failed");
            }
        });
    }
}
