package org.pursuit.usolo.map;


import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.model.bindle.BindleBusiness;
import com.android.group.network.constants.CategoryConstants;
import com.android.interactionlistener.OnBackPressedInteraction;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;
import org.pursuit.firebasetools.model.Group;
import org.pursuit.usolo.R;
import org.pursuit.usolo.map.ViewModel.ZoneViewModel;
import org.pursuit.usolo.map.categories.CategoryAdapter;
import org.pursuit.usolo.map.nearbygroups.NearbyGroupAdapter;
import org.pursuit.usolo.model.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;

public final class MapFragment extends Fragment implements OnBackPressedInteraction {

    private static final String TAG = "MapFragment";
    private static final String MAPBOX_ACCESS_TOKEN =
      "pk.eyJ1IjoibmFvbXlwIiwiYSI6ImNqdnBvMWhwczJhdzA0OWw2Z2R1bW9naGoifQ.h-ujnDnmD5LbLhyegylCNA";
    private static final String MAPBOX_STYLE_URL =
      "mapbox://styles/naomyp/cjvpowkpn0yd01co7844p4m6w";
    private static final String MARKER_IMAGE = "custom-marker";
    private static final String CURRENT_FILTER = "FILTER";
    public static final String GROUP_PREFS = "GROUP";
    public static final String CURRENT_GROUP_KEY = "current_group";

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Disposable groupMarkerDisposable;
    private List<SymbolLayer> symbolLayers = new LinkedList<>();
    private ZoneViewModel zoneViewModel;
    private ImageView bottomSheetArrow;
    private OnFragmentInteractionListener listener;
    private MapView mapView;
    private boolean isFabOpen;
    private FloatingActionButton fab, fab1, fab2, fabProfile, fabRecenterUser;
    private BottomSheetBehavior bottomSheetBehavior;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private TextView currentActivityHeader, nearbyActivityHeader, groupTitle, groupLocation;
    private CardView currentActivityCard;
    private Group currentGroup;
    private MapboxMap mapboxMap;
    private Dialog zoneDialog;
    private SharedPreferences sharedPreferences;
    private SymbolManager symbolManager;
    private String currentGroupSharedPref;
    private LocationComponent locationComponent;
    private RecyclerView recyclerViewNearby, recyclerViewCategories;
    private NearbyGroupAdapter nearbyGroupAdapter;

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
        sharedPreferences = getActivity().getSharedPreferences(GROUP_PREFS, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CURRENT_GROUP_KEY)) {
            currentGroupSharedPref = sharedPreferences.getString(CURRENT_GROUP_KEY, "");
            Log.d(TAG, "onCreate: " + currentGroupSharedPref);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        zoneDialog = new Dialog(Objects.requireNonNull(MapFragment.this.getContext()));
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setUpCategoryRV();
        setCurrentActivityView();
        nearbyGroupAdapter = new NearbyGroupAdapter(listener, Collections.emptyList());
        setGroups();
        getGroup();
        fabRecenterUser = view.findViewById(R.id.fab_recenter);
        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fabStartGroup);
        fab2 = view.findViewById(R.id.fabFindGroup);
        fabProfile = view.findViewById(R.id.fab_profile);
        fab.setOnClickListener(v -> onFabClick(v));
        fab1.setOnClickListener(v -> onFabClick(v));
        fab2.setOnClickListener(v -> onFabClick(v));
        fabProfile.setOnClickListener(v -> onFabClick(v));
        assignAnimations();
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        fabRecenterUser.show();
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(130);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    fab.show();
                    fab.setClickable(true);
                    fabRecenterUser.show();
                    fabRecenterUser.setClickable(true);
                } else {
                    fab.hide();
                    fab.startAnimation(rotateBackward);
                    fab.setClickable(false);
                    fabRecenterUser.hide();
                    fabRecenterUser.setClickable(false);
                    if (isFabOpen) {
                        disableFabs();
                        isFabOpen = false;
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                fab.hide();
                fabRecenterUser.hide();
                fabRecenterUser.setClickable(false);
                if (isFabOpen) {
                    disableFabs();
                    isFabOpen = false;
                }
            }
        });
        mapView = view.findViewById(R.id.mapView);
        mapView.getMapAsync(mapboxMap ->
          this.mapboxMap = mapboxMap);
    }

    private void setUpCategoryRV() {
        List<Category> categories = new ArrayList<>();
        List<String> categoryNames = Arrays.asList(getResources().getStringArray(R.array.categoryNames));
        int[] iconId = {R.drawable.icon_nighltlife_purple_grad,
          R.drawable.icon_sightseeing_purple_grad,
          R.drawable.icon_eatdrink_purple_grad};
        int count = 0;
        for (String title : categoryNames) {
            categories.add(new Category(title, iconId[count]));
            count++;
        }
        CategoryAdapter categoryAdapter =
          new CategoryAdapter(categories, sharedPreferences.edit(),
            category -> {
                mapView.getMapAsync(mapboxMap1 -> {
                    Style style = mapboxMap1.getStyle();
                    for (SymbolLayer symbolLayer :
                      symbolLayers) {
                        style.removeLayer(symbolLayer);
                    }
                    symbolLayers.clear();
                    for (String title :
                      zoneViewModel.getGroupNames()) {
                        style.removeSource(title);
                    }
                    if (zoneViewModel.clearGroupNames()) {
                        groupMarkerDisposable.dispose();
                        groupMarkerDisposable = zoneViewModel.getAllGroups().subscribe(group ->
                            showGroup(group, mapboxMap1.getStyle(), category),
                          throwable ->
                            Log.d(TAG, "onCategorySelected: " + throwable.getMessage()));
                    }
                });
            });
        recyclerViewCategories.setAdapter(categoryAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager);
    }

    private void findViews(@NonNull View view) {
        mapView = view.findViewById(R.id.mapView);
        recyclerViewCategories = view.findViewById(R.id.category_recycler);
        recyclerViewNearby = view.findViewById(R.id.nearby_recycler);
        nearbyActivityHeader = view.findViewById(R.id.happening_header);
        nearbyActivityHeader.setVisibility(View.INVISIBLE);
        currentActivityCard = view.findViewById(R.id.current_activity_card);
        currentActivityHeader = view.findViewById(R.id.current_activity_textHeader);
        groupTitle = view.findViewById(R.id.textView_activity_name);
        groupLocation = view.findViewById(R.id.textView_activity_address);
        view.<Button>findViewById(R.id.view_your_group_button).setOnClickListener(v -> {
            if (currentGroupSharedPref != null)
                listener.inflateGroupChatFragment(currentGroup);
        });
        view.<Button>findViewById(R.id.leave_group_button).setOnClickListener(v -> {
            disposables.add(zoneViewModel.removeUserFromGroup(currentGroupSharedPref));
            sharedPreferences.edit().clear().apply();
            currentGroupSharedPref = null;
            setCurrentActivityView();
        });
        fabProfile = view.findViewById(R.id.fab_profile);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(130);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mapView.getMapAsync(mapboxMap -> {
            MapFragment.this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(new Style.Builder().fromUrl(MAPBOX_STYLE_URL), style -> {
                MapFragment.this.enableLocationComponent(style);
                fabRecenterUser.setOnClickListener(v -> {
                    CameraPosition position = MapFragment.this.getCameraPosition();
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000);
                });
                setZoneStyle(style);
                groupMarkerDisposable = zoneViewModel.getAllGroups()
                  .subscribe(group -> showGroup(group, style,
                    Objects.requireNonNull(sharedPreferences
                      .getString(CURRENT_FILTER, "Nightlife"))),
                    throwable -> Log.d(TAG, "findViews: " + throwable.getMessage()));
                disposables.add(zoneViewModel.getAllZones(Objects.requireNonNull(MapFragment.this.getContext()))
                  .map(zone -> zoneViewModel.getMapFeature(zone))
                  .subscribe(mapFeature -> {
                      MapFragment.this.showZone(mapFeature.location);
                      String sourceId = mapFeature.name + "_source";
                      style.addSource(new GeoJsonSource(sourceId, zoneViewModel.getGeometry(mapFeature)));
                      style.addLayer(new FillLayer(mapFeature.name, sourceId).withProperties(
                        fillColor(Color.parseColor(MapFragment.this.getString(R.string.zone_colour)))));
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
                    for (String title : zoneViewModel.getGroupNames()) {
                        if (mapboxMap.queryRenderedFeatures(rectF, title).size() > 0) {
                            showGroupDialog(title);
                            return true;
                        }
                    }
                    return false;
                });
            });
        });
    }

    private void onFabClick(View v) {
        animateFAB();
        Log.d(TAG, "setOnClick: " + v.getId() + " " + R.id.fabStartGroup);
        switch (v.getId()) {
            case R.id.fabStartGroup:
                fabRecenterUser.hide();
                String currentGroupName = sharedPreferences.getString(CURRENT_GROUP_KEY, "");
                if (currentGroupName.equals("")) {
                    listener.inflateStartGroupFragment();
                } else {
                    Snackbar snackbar = Snackbar.make(getView(), "Already in Group: \n" + currentGroupName, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Go to Group", v1 -> listener.inflateGroupChatFragment(currentGroup)).show();
                }
                break;
            case R.id.fabFindGroup:
                fabRecenterUser.hide();
                listener.inflateExploreGroupsFragment();
                break;
            case R.id.fab_profile:
                fabRecenterUser.hide();
                listener.inflateProfileFragment(true, "currentUserID");
                break;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: " + disposables.getClass());
        disposables.dispose();
        groupMarkerDisposable.dispose();
        super.onDestroy();
    }

    private void getGroup() {
        if (currentGroupSharedPref != null) {
            disposables.add(zoneViewModel
              .getGroup(currentGroupSharedPref)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::setCurrentActivityView,
                throwable -> Log.d("naomy: ", throwable.toString())));
        }
    }

    private void setGroups() {
        disposables.add(zoneViewModel
          .getGroups()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::setNearbyActivityView));
    }

    private void setNearbyActivityView(List<Group> groupList) {
        nearbyActivityHeader.setVisibility(View.VISIBLE);
        setUpRV(groupList);

    }

    private void setUpRV(List<Group> groupList) {
        nearbyGroupAdapter.updateList(groupList);
        recyclerViewNearby.setAdapter(nearbyGroupAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNearby.setLayoutManager(layoutManager);
    }

    private void setCurrentActivityView(Group group) {
        currentGroup = group;
        currentActivityHeader.setText(R.string.your_current_activity);
        currentActivityCard.setVisibility(View.VISIBLE);
        groupTitle.setText(currentGroup.getTitle());
        groupLocation.setText(currentGroup.getAddress());
    }

    private void setCurrentActivityView() {
        currentActivityHeader.setText(R.string.current_activity_none);
        currentActivityCard.setVisibility(View.INVISIBLE);
    }

    private void assignAnimations() {
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_backward);
    }

    @NotNull
    private CameraPosition getCameraPosition() {
        return new CameraPosition.Builder()
          .target(new LatLng(Objects.requireNonNull(locationComponent.getLastKnownLocation())))
          .zoom(13)
          .tilt(30)
          .build();
    }

    private void setZoneStyle(Style style) {
        style.addImage(MapFragment.MARKER_IMAGE,
          BitmapFactory.decodeResource(MapFragment.this.getResources(),
            R.drawable.smallbindle));
        symbolManager = new SymbolManager(mapView, mapboxMap, style, null,
          new GeoJsonOptions().withTolerance(0.4f));
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setTextAllowOverlap(true);
    }

    private void showGroup(@NonNull final Group group,
                           @NonNull final Style style,
                           @NonNull final String category) {
        Bitmap image = null;
        switch (category) {
            case CategoryConstants.NIGHTLIFE:
                image = BitmapFactory.decodeResource(getResources(), R.drawable.nightlife_icon_32);
                break;
            case CategoryConstants.SIGHTSEEING:
                image = BitmapFactory.decodeResource(getResources(), R.drawable.sightseeing_icon_32);
                break;
            case CategoryConstants.EAT_AND_DRINK:
                image = BitmapFactory.decodeResource(getResources(), R.drawable.eatdrink_icon_32);
                break;
        }
        if (image != null && group.getCategory().equals(category)) {
            setIcon(group, style, image);
        }
    }

    private void setIcon(@NonNull final Group group,
                         @NonNull final Style style,
                         @NonNull final Bitmap image) {
        style.addImage(group.getTitle(), image);
        GeoJsonSource geoJsonSource = new GeoJsonSource(group.getTitle(), Feature.fromGeometry(
          Point.fromLngLat(group.getLocation().getLongitude(), group.getLocation().getLatitude())));
        style.addSource(geoJsonSource);
        SymbolLayer symbolLayer = new SymbolLayer(group.getTitle(), group.getTitle());
        symbolLayer.withProperties(PropertyFactory.iconImage(group.getTitle()));
        symbolLayers.add(symbolLayer);
        style.addLayer(symbolLayer);
    }

    private void showGroupDialog(String imageName) {
        zoneDialog.setContentView(R.layout.zone_dialog_layout);
        zoneDialog.<TextView>findViewById(R.id.zone_dialog_name)
          .setText(imageName);
        zoneDialog.<Button>findViewById(R.id.view_zone_button).setOnClickListener(v ->
          disposables.addAll(zoneViewModel.getGroup(imageName).observeOn(AndroidSchedulers.mainThread())
            .subscribe(group -> {
                listener.inflateViewGroupFragment(group);
                zoneDialog.dismiss();
            })));
        Objects.requireNonNull(
          zoneDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        zoneDialog.show();
    }

    private void showZone(@NonNull final LatLng location) {
        SymbolOptions symbolOptions = new SymbolOptions();
        symbolManager.create(symbolOptions
          .withLatLng(location)
          .withIconImage(MARKER_IMAGE)
          .withIconSize(.5f));
    }

    private void showZoneDialog(String iD) {
        zoneDialog.setContentView(R.layout.zone_dialog_layout);
        zoneDialog.<TextView>findViewById(R.id.zone_dialog_name).setText(iD);
        zoneDialog.<Button>findViewById(R.id.view_zone_button).setOnClickListener(v -> {
            disposables.add(zoneViewModel.getZone(iD)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(zone -> {
                  listener.inflateZoneChatFragment(zone);
                  zoneDialog.dismiss();
              }));
        });
        Objects.requireNonNull(
          zoneDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        zoneDialog.show();
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        locationComponent = mapboxMap.getLocationComponent();
        locationComponent.activateLocationComponent(
          LocationComponentActivationOptions
            .builder(Objects.requireNonNull(getContext()), loadedMapStyle).build());
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
        setCurrentActivityView();
        if (sharedPreferences.contains(CURRENT_GROUP_KEY)) {
            currentGroupSharedPref = sharedPreferences.getString(CURRENT_GROUP_KEY, "");
            Log.d(TAG, "onResume: " + currentGroupSharedPref);
            getGroup();
        } else {
            currentGroupSharedPref = null;
        }
        setGroups();
        if (zoneViewModel.getRecentGroupList().size() > 0)
            nearbyGroupAdapter.updateList(zoneViewModel.getRecentGroupList());
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

    private void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fabProfile.startAnimation(fabClose);
            fabRecenterUser.show();
            fabRecenterUser.setClickable(true);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fabProfile.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fabProfile.startAnimation(fabOpen);
            fabRecenterUser.hide();
            fabRecenterUser.setClickable(false);
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
        fabRecenterUser.show();
        fabRecenterUser.setClickable(true);
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
