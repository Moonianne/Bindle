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

import com.mapbox.android.telemetry.MapboxTelemetry;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.pursuit.usolo.R;
import org.pursuit.usolo.map.utils.GeoFenceCreator;

public final class MapFragment extends Fragment {
    private MapView mapView;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Mapbox.getInstance(context, "pk.eyJ1IjoibWltZWRyb2lkIiwiYSI6ImNqdnByOWN2bTB1bXQzem8xOWRjdG41b2EifQ.UwtQ-KOGSh0K0dUBJbZT6Q");
        //TODO: Key Extraction
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View bottomSheet = view.findViewById( R.id.bottom_sheet );
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(130);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mapView = view.findViewById(R.id.mapView);
        mapView.getMapAsync(mapboxMap ->
                mapboxMap.setStyle(new Style.Builder().fromUrl("mapbox://styles/naomyp/cjvpowkpn0yd01co7844p4m6w"), style -> {
                    // TODO: Map is set up and the style has loaded. Now you can add data or make other map adjustments
                }));

    }

    private void makeGeoFence() {
        GeoFenceCreator geoFenceCreator = new GeoFenceCreator(getContext(),new LatLng(40,122));
        geoFenceCreator.initGeoFenceClient();
        geoFenceCreator.buildGeoFence();
        geoFenceCreator.addGeoFenceToClient();
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
}
