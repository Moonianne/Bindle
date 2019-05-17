package org.pursuit.usolo.map;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import org.pursuit.usolo.R;
import org.pursuit.usolo.map.data.ZoneRepository;
import org.pursuit.usolo.map.model.Zone;
import org.pursuit.usolo.map.utils.GeoFenceCreator;

import java.util.List;

public final class MapFragment extends Fragment
  implements ZoneRepository.OnUpdatesEmittedListener, PermissionsListener {

    private static final String MAPBOX_ACCESS_TOKEN =
      "pk.eyJ1IjoibmFvbXlwIiwiYSI6ImNqdnBvMWhwczJhdzA0OWw2Z2R1bW9naGoifQ.h-ujnDnmD5LbLhyegylCNA";
    private static final String MAPBOX_STYLE_URL =
      "mapbox://styles/naomyp/cjvpowkpn0yd01co7844p4m6w";

    private MapView mapView;
    private PermissionsManager permissionsManager;
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
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(130);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mapView = view.findViewById(R.id.mapView);
        mapView.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;
            // TODO: Map is set up and the style has loaded. Now you can add data or make other map adjustments
            mapboxMap.setStyle(new Style.Builder().fromUrl(MAPBOX_STYLE_URL),
              this::enableLocationComponent);
        });
    }

    private void makeGeoFence(LatLng latLng) {
        new GeoFenceCreator(getContext(), latLng).createGeoFence();
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

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
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), "Get Permissions", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(this::enableLocationComponent);
        } else {
            Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_LONG).show();
            //finish();
        }

    }

    @Override
    public void emitUpdate(Zone zone) {
        makeGeoFence(zone.location);
    }
}
