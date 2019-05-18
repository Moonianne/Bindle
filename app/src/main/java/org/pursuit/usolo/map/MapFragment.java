package org.pursuit.usolo.map;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import org.pursuit.usolo.HostActivity;
import org.pursuit.usolo.R;
import org.pursuit.usolo.map.data.ZoneRepository;
import org.pursuit.usolo.map.model.Zone;
import org.pursuit.usolo.map.utils.GeoFenceCreator;

public final class MapFragment extends Fragment
  implements ZoneRepository.OnUpdatesEmittedListener, View.OnTouchListener {

    private static final String MAPBOX_ACCESS_TOKEN =
      "pk.eyJ1IjoibmFvbXlwIiwiYSI6ImNqdnBvMWhwczJhdzA0OWw2Z2R1bW9naGoifQ.h-ujnDnmD5LbLhyegylCNA";
    private static final String MAPBOX_STYLE_URL =
      "mapbox://styles/naomyp/cjvpowkpn0yd01co7844p4m6w";

    private MapView mapView;
    private boolean isFabOpen;
    private FloatingActionButton fab, fab1, fab2;
    private BottomSheetBehavior bottomSheetBehavior;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private MapboxMap mapboxMap;


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Mapbox.getInstance(context, MAPBOX_ACCESS_TOKEN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ZoneRepository zoneRepository = new ZoneRepository();
        zoneRepository.loginToFirebase(
          getString(R.string.firebase_email),
          getString(R.string.firebase_password));
        zoneRepository.subscribeToUpdates(this);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        assignAnimations();
        setOnClick(fab);
        setOnClick(fab1);
        setOnClick(fab2);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheet.setOnTouchListener(this);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(130);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mapView = view.findViewById(R.id.mapView);
        mapView.getMapAsync(mapboxMap ->
          mapboxMap.setStyle(new Style.Builder().fromUrl("mapbox://styles/naomyp/cjvpowkpn0yd01co7844p4m6w"), style -> {
              // TODO: Map is set up and the style has loaded. Now you can add data or make other map adjustments
          }));
    }

    private void setOnClick(View view) {
        view.setOnClickListener(v -> animateFAB());
    }

    private void assignAnimations() {
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_backward);
    }

    private void findViews(@NonNull View view) {
        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(130);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mapView = view.findViewById(R.id.mapView);
        mapView.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;
            // TODO: Map is set up and the style has loaded. Now you can add data or make other map adjustments
            mapboxMap.setStyle(new Style.Builder().fromUrl(MAPBOX_STYLE_URL), this::enableLocationComponent);
        });
    }

    private void makeGeoFence(LatLng latLng) {
        new GeoFenceCreator(getContext(), latLng).createGeoFence();
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (HostActivity.granted) {

            // Get an instance of the LocationComponent.
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate the LocationComponent
            locationComponent.activateLocationComponent(
              LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());

            // Enable the LocationComponent so that it's actually visible on the map
            locationComponent.setLocationComponentEnabled(true);

            // Set the LocationComponent's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the LocationComponent's render mode
            locationComponent.setRenderMode(RenderMode.NORMAL);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    public void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            enableFabs();
        }
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING) {
            fab.hide();
            if (isFabOpen) {
                disableFabs();
                isFabOpen = false;
            }
        }
        return false;
        // TODO: 2019-05-17 Figure out how to automatically
        //  show fab once collapsed instead of clicking on View
    }

    private void disableFabs() {
        fab1.startAnimation(fabClose);
        fab2.startAnimation(fabClose);
        fab1.setClickable(false);
        fab2.setClickable(false);
        fab.setClickable(false);
    }

    private void enableFabs() {
        fab.show();
        fab.setClickable(true);
        fab1.setClickable(true);
        fab2.setClickable(true);
    }

    public void emitUpdate(Zone zone) {
        makeGeoFence(zone.location);
    }
}
