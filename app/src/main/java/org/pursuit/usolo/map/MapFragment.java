package org.pursuit.usolo.map;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;

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

    private static final String ID_ICON_DEFAULT = "icon-default";
    private static final String MARKER_IMAGE = "custom-marker";

    private AlertDialog zoneSelectionDialog;
    private MapView mapView;
    private boolean isFabOpen;
    private FloatingActionButton fab, fab1, fab2;
    private BottomSheetBehavior bottomSheetBehavior;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private MapboxMap mapboxMap;
    private SymbolManager symbolManager;
    private Symbol symbol;
    private CircleManager circleManager;

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

    @Override
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
          this.mapboxMap = mapboxMap);
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
            MapFragment.this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(new Style.Builder().fromUrl(MAPBOX_STYLE_URL), new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);

                    LatLng pursuit = new LatLng(40.7430877, -73.9419287);
                    addZoneMarker(style, pursuit);

                }
            });
        });
    }

    private void addZoneMarker(Style style, LatLng zone) {

        style.addImage(MARKER_IMAGE, BitmapFactory.decodeResource(
          MapFragment.this.getResources(), R.drawable.asset_icon));

        // create symbol manager
        GeoJsonOptions geoJsonOptions = new GeoJsonOptions().withTolerance(0.4f);
        symbolManager = new SymbolManager(mapView, mapboxMap, style, null, geoJsonOptions);
        symbolManager.addClickListener(symbol -> Toast.makeText(getContext(),
          String.format("Symbol clicked %s", symbol.getId()), Toast.LENGTH_SHORT).show());
        symbolManager.addLongClickListener(symbol ->
          Toast.makeText(getContext(),
            String.format("Symbol long clicked %s", symbol.getId()), Toast.LENGTH_SHORT).show());

        // set non data driven properties
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setTextAllowOverlap(true);

        // create a symbol
        SymbolOptions symbolOptions = new SymbolOptions()
          .withLatLng(zone) //replace with zone LatLng
          .withIconImage(MARKER_IMAGE)
          .withIconSize(.5f);
        symbol = symbolManager.create(symbolOptions);

//        // create circle manager
//        circleManager = new CircleManager(mapView, mapboxMap, style);
//        circleManager.addClickListener(circle -> Toast.makeText(getContext(), String.format("Circle clicked %s", circle.getId()), Toast.LENGTH_SHORT).show());
//        circleManager.addLongClickListener(circle -> Toast.makeText(getContext(), String.format("Circle long clicked %s", circle.getId()), Toast.LENGTH_SHORT).show());
//
//        // create a fixed circle
//        CircleOptions circleOptions = new CircleOptions()
//          .withLatLng(zone)
//          .withCircleColor(ColorUtils.colorToRgbaString(Color.LTGRAY))
//          .withCircleRadius(20f);
//        circleManager.create(circleOptions);

    }

    private void makeGeoFence(LatLng latLng) {
        new GeoFenceCreator(getContext(), latLng).createGeoFence();
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        final LocationComponent locationComponent = mapboxMap.getLocationComponent();

        locationComponent.activateLocationComponent(
          LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.NORMAL);
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
