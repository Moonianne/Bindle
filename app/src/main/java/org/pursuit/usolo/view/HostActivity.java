package org.pursuit.usolo.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.authentication.view.AuthenticationFragment;
import com.android.group.view.AddLocationFragment;
import com.android.group.view.OnFragmentInteractionCompleteListener;
import com.android.group.view.StartGroupFragment;
import com.android.group.view.joingroup.GroupChatView;
import com.android.interactionlistener.OnBackPressedInteraction;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.example.exploregroup.view.ExploreGroupsFragment;
import com.example.exploregroup.view.ViewGroupFragment;

import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Zone;
import org.pursuit.userprofile.view.ProfileView;
import org.pursuit.usolo.R;
import org.pursuit.usolo.map.MapFragment;
import org.pursuit.usolo.map.ViewModel.ZoneViewModel;
import org.pursuit.usolo.map.utils.GeoFenceCreator;
import org.pursuit.usolo.viewmodel.HostViewModel;
import org.pursuit.zonechat.view.ZoneChatFragment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public final class HostActivity extends AppCompatActivity
  implements OnFragmentInteractionListener, StartGroupFragment.OnFragmentInteractionListener,
  OnFragmentInteractionCompleteListener {

    private static final String LOGIN_PREFS = "USER_LOGIN";
    private static final String EMAIL_PREFS = "EMAIL_PREFS";
    private static final String PASS_PREFS = "PW_PREFS";

    private static final String TAG = "HostActivity";
    public static boolean granted;
    private SharedPreferences preferences;
    private ZoneViewModel viewModel;
    private HostViewModel hostViewModel;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        hostViewModel = ViewModelProviders.of(this).get(HostViewModel.class);
        viewModel = ViewModelProviders.of(this).get(ZoneViewModel.class);
        preferences = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        if (preferences.contains(EMAIL_PREFS) && preferences.contains(PASS_PREFS)) {
            viewModel.loginToFireBase(preferences.getString(EMAIL_PREFS, "metalraidernt@gmail.com"),
              preferences.getString(PASS_PREFS, "password123"));
            requestUserLocationPermission();
        } else {
            inflateAuthenticationFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            granted = true;
            placeGeofences();
            Log.d(TAG, "onRequestPermissionsResult: " + "thisran");
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
    public void inflateZoneChatFragment(@NonNull final Zone zone) {
        inflateFragment(ZoneChatFragment.newInstance(zone), true);
    }

    @Override
    public void inflateProfileFragment(boolean bool, String userID) {
        inflateFragment(ProfileView.newInstance(bool, userID), true);
    }

    @Override
    public void inflateExploreGroupsFragment() {
        inflateFragment(ExploreGroupsFragment.newInstance(), true);
    }

    @Override
    public void inflateViewGroupFragment(Group group) {
        inflateFragment(ViewGroupFragment.newInstance(group), true);
    }

    @Override
    public void inflateGroupChatFragment(Group group) {
        inflateFragment(GroupChatView.newInstance(group), true);
    }

    @Override
    public void openDirections(String venueAddress) {
        Uri parseAddress = Uri.parse("google.navigation:q=" + venueAddress + "&mode=transit");
        Intent navIntent = new Intent(android.content.Intent.ACTION_VIEW, parseAddress);
        navIntent.setPackage("com.google.android.apps.maps");
        startActivity(navIntent);
    }

    @Override
    public void onLoginSuccess() {
        requestUserLocationPermission();
    }

    @Override
    public void inflateAuthenticationFragment() {
        inflateFragment(AuthenticationFragment.getInstance(), false);
    }

    @Override
    public void logoutUser() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
        viewModel.logoutFireBase();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void goToGroupChatFragment(Group group) {
        getSupportFragmentManager().popBackStackImmediate();
        inflateGroupChatFragment(group);
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
            placeGeofences();
            inflateFragment(MapFragment.newInstance());
        }
    }

    public void inflateFragment(Fragment fragment) {
        inflateFragment(fragment, false);
    }

    public void inflateFragment(Fragment fragment, boolean addToBack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
          .beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
          .replace(R.id.main_container, fragment);
        if (addToBack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    private void placeGeofences() {
        disposable.add(hostViewModel.getAllGroups().observeOn(AndroidSchedulers.mainThread())
          .subscribe(group -> {
              Log.d(TAG, "placeGeofences: " + group.getTitle());
              new GeoFenceCreator(HostActivity.this, group.getLocation()).createGeoFence();
          }));
        disposable.add(hostViewModel.getAllZones(this).observeOn(AndroidSchedulers.mainThread())
          .subscribe(zone -> {
              Log.d(TAG, "placeGeofences: " + zone.getName());
              new GeoFenceCreator(HostActivity.this, zone.getLocation()).createGeoFence();
          }));
    }

    @Override
    public void onBackPressed() {
        List fragmentList = getSupportFragmentManager().getFragments();
        boolean handled = false;
        for (Object f : fragmentList) {
            if (f instanceof OnBackPressedInteraction) {
                handled = ((OnBackPressedInteraction) f).onBackPressed();

                if (handled) {
                    break;
                }
            }
        }
        if (!handled) {
            super.onBackPressed();
        }
    }
}
