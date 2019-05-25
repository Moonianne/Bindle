package org.pursuit.usolo.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.group.view.AddLocationFragment;
import com.android.group.view.OnFragmentInteractionCompleteListener;
import com.android.group.view.StartGroupFragment;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.geometry.LatLng;


import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Zone;
import org.pursuit.usolo.R;
import org.pursuit.usolo.map.MapFragment;
import org.pursuit.usolo.map.ViewModel.ZoneViewModel;
import org.pursuit.zonechat.view.ZoneChatView;

import java.util.LinkedList;
import java.util.List;


public final class HostActivity extends AppCompatActivity
  implements OnFragmentInteractionListener, StartGroupFragment.OnFragmentInteractionListener,
  OnFragmentInteractionCompleteListener {

    private static final String TAG = "HostActivity";
    private ZoneViewModel viewModel;
    public static boolean granted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        FireRepo fireRepo = FireRepo.getInstance();
        fireRepo.addZone(new Zone(new LatLng(40.7430877d, -73.9419287d), "pursuitChat", "pursuit", 0));
        fireRepo.addZoneChat("pursuitChat");
        List<FirebaseUser> userList = new LinkedList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userList.add(user);
        fireRepo.addGroup(new Group(userList, "Access Code", "Night Life", new LatLng(40.7430877d, -73.9419287d), "pursuitChat", "Let's Learn to Code", 1));
        fireRepo.addGroupChat("pursuitChat");
        viewModel = ViewModelProviders.of(this).get(ZoneViewModel.class);
        viewModel.loginToFireBase();
//        viewModel.addUserToZoneCount("pursuit");
        requestUserLocationPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            granted = true;
            inflateFragment(MapFragment.newInstance());
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void inflateAddLocationFragment() {
        inflateFragment(AddLocationFragment.newInstance(), true);
    }

    @Override
    public void inflateStartGroupFragment() {
        inflateFragment(StartGroupFragment.newInstance(), true);
    }

    @Override
    public void inflateZoneChatFragment() {
        inflateFragment(ZoneChatView.newInstance(), true);
    }

    @Override
    public void closeFragment() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void requestUserLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
          ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            granted = true;
            inflateFragment(MapFragment.newInstance());
        }
    }

    public void inflateFragment(Fragment fragment) {
        inflateFragment(fragment, false);
    }

    public void inflateFragment(Fragment fragment, boolean addToBack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.main_container, fragment);
        if (addToBack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
