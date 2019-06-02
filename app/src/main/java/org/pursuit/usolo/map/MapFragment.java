package org.pursuit.usolo.map;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


import com.android.interactionlistener.OnBackPressedInteraction;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;
import org.pursuit.firebasetools.model.Zone;
import org.pursuit.usolo.R;
import org.pursuit.usolo.map.ViewModel.ZoneViewModel;
import org.pursuit.usolo.map.utils.GeoFenceCreator;
import org.pursuit.usolo.model.MapFeature;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;

public final class MapFragment extends Fragment
  implements View.OnTouchListener, OnBackPressedInteraction {

    private static final String TAG = "MapFragment";
    private static final String MAPBOX_ACCESS_TOKEN =
      "pk.eyJ1IjoibmFvbXlwIiwiYSI6ImNqdnBvMWhwczJhdzA0OWw2Z2R1bW9naGoifQ.h-ujnDnmD5LbLhyegylCNA";
    private static final String MAPBOX_STYLE_URL =
      "mapbox://styles/naomyp/cjvpowkpn0yd01co7844p4m6w";
    private static final String MARKER_IMAGE = "custom-marker";

    private CompositeDisposable disposables = new CompositeDisposable();
    private ZoneViewModel zoneViewModel;
    private OnFragmentInteractionListener listener;
    private MapView mapView;
    private boolean isFabOpen;
    private FloatingActionButton fab, fab1, fab2, fabProfile;
    private BottomSheetBehavior bottomSheetBehavior;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private Button viewForum;
    private MapboxMap mapboxMap;
    private Dialog zoneDialog;
    SharedPreferences sharedPreferences;
    private SymbolManager symbolManager;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Mapbox.getInstance(context, MAPBOX_ACCESS_TOKEN);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zoneViewModel = ViewModelProviders.of(this).get(ZoneViewModel.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        zoneDialog = new Dialog(Objects.requireNonNull(MapFragment.this.getContext()));
        //TODO: Take a look at this
        disposables.add(zoneViewModel.getAllZones(Objects.requireNonNull(getActivity()))
          .map(Zone::getLocation)
          .subscribe(this::makeGeoFence));
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        fabProfile = view.findViewById(R.id.fab_profile);
        fab.setOnClickListener(v -> onFabClick(v));
        fab1.setOnClickListener(v -> onFabClick(v));
        fab2.setOnClickListener(v -> onFabClick(v));
        fabProfile.setOnClickListener(v -> onFabClick(v));

        assignAnimations();


        viewForum.setOnClickListener(v -> {
            String current_group = sharedPreferences.getString("current_group", "");
            if (!current_group.equals("")) {
                zoneViewModel.getGroup(current_group).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Group>() {
                    @Override
                    public void accept(Group group) throws Exception {
                        listener.inflateGroupChatFragment(group);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(".MapFragment: ", throwable.toString());
                    }
                });
            }
        });
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheet.setOnTouchListener(this);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(130);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mapView = view.findViewById(R.id.mapView);
        mapView.getMapAsync(mapboxMap ->
          this.mapboxMap = mapboxMap);
    }

    private void onFabClick(View v) {
        animateFAB();
        Log.d(TAG, "setOnClick: " + v.getId() + " " + R.id.fab1);
        switch (v.getId()) {
            case R.id.fab1:
                listener.inflateStartGroupFragment();
                break;
            case R.id.fab2:
                listener.inflateExploreGroupsFragment();
                break;
            case R.id.fab_profile:
                listener.inflateProfileFragment(true);
                break;
        }
    }

    @Override
    public void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }

    private void assignAnimations() {
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_backward);
    }

    private void findViews(@NonNull View view) {
        viewForum = view.findViewById(R.id.view_your_group_button);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(130);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mapView = view.findViewById(R.id.mapView);
        mapView.getMapAsync(mapboxMap -> {
            MapFragment.this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(new Style.Builder().fromUrl(MAPBOX_STYLE_URL), style -> {
                enableLocationComponent(style);
                setZoneStyle(style);
                disposables.add(zoneViewModel.getAllZones(Objects.requireNonNull(getContext()))
                  .map(zone -> zoneViewModel.getMapFeature(zone))
                  .subscribe(mapFeature -> {
                      showZone(mapFeature.location);
                      String sourceId = mapFeature.name + "_source";
                      style.addSource(new GeoJsonSource(sourceId, zoneViewModel.getGeometry(mapFeature)));
                      style.addLayer(new FillLayer(mapFeature.name, sourceId).withProperties(
                        fillColor(Color.parseColor(getString(R.string.zone_colour)))));
                  }, throwable -> Log.d(TAG, "findViews: " + throwable.getMessage())));
                mapboxMap.addOnMapClickListener(point -> {
                    PointF pointf = mapboxMap.getProjection().toScreenLocation(point);
                    RectF rectF = zoneViewModel.getRectF(pointf);
                    for (String name : zoneViewModel.getZoneNames()) {
                        if (mapboxMap.queryRenderedFeatures(rectF, name).size() > 0) {
                            showZoneDialog(name);
                            return true;
                        }
                    }
                    return false;
                });
            });
        });
    }

    private void setZoneStyle(Style style) {
        style.addImage(MapFragment.MARKER_IMAGE,
          BitmapFactory.decodeResource(MapFragment.this.getResources(),
            R.drawable.smallbindle));
        symbolManager = new SymbolManager(mapView, mapboxMap, style, null,
          new GeoJsonOptions().withTolerance(0.4f));
        symbolManager.addClickListener(symbol -> showZoneDialog(symbol));
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setTextAllowOverlap(true);
    }

    private void showZone(@NonNull final LatLng zone) {
        symbolManager.create(new SymbolOptions()
          .withLatLng(zone)
          .withIconImage(MARKER_IMAGE)
          .withIconSize(.5f));
    }

    private void showZoneDialog(String iD) {
        zoneDialog.setContentView(R.layout.zone_dialog_layout);
        zoneDialog.<TextView>findViewById(R.id.zone_dialog_name).setText(iD);
        zoneDialog.<Button>findViewById(R.id.view_zone_button).setOnClickListener(v -> {
            listener.inflateZoneChatFragment();
            zoneDialog.dismiss();
        });
        Objects.requireNonNull(
          zoneDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        zoneDialog.show();
    }

    private void makeGeoFence(LatLng latLng) {
        new GeoFenceCreator(getContext(), latLng).createGeoFence();
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        final LocationComponent locationComponent = mapboxMap.getLocationComponent();
        locationComponent.activateLocationComponent(
          LocationComponentActivationOptions.builder(Objects.requireNonNull(getContext()), loadedMapStyle).build());
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

    @SuppressLint("ClickableViewAccessibility")
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
            fabProfile.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fabProfile.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fabProfile.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fabProfile.setClickable(true);
            isFabOpen = true;
        }
    }

    private void disableFabs() {
        fab1.startAnimation(fabClose);
        fab2.startAnimation(fabClose);
        fabProfile.startAnimation(fabClose);
        fab1.setClickable(false);
        fab2.setClickable(false);
        fabProfile.setClickable(false);
        fab.setClickable(false);
    }

    private void enableFabs() {
        fab.show();
        fab.setClickable(true);
        fab1.setClickable(true);
        fab2.setClickable(true);
        fabProfile.setClickable(true);
    }

    @Override
    public boolean onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            enableFabs();
            return true;
        } else {

            return false;
        }
    }
}
