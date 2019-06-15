package com.android.group.view;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.airbnb.lottie.LottieAnimationView;
import com.android.group.R;
import com.android.group.model.bindle.BindleBusiness;
import com.android.group.network.constants.CategoryConstants;
import com.android.group.view.recyclerview.AddLocationAdapter;
import com.android.group.view.utils.SearchViewListFilter;
import com.android.group.viewmodel.NetworkViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public final class AddLocationFragment extends Fragment {

    private static final String TAG = "AddLocationFragment";
    private View rootView;
    private NetworkViewModel viewModel;
    private AddLocationAdapter adapter;
    private LottieAnimationView lottieAnimationView;
    private List<BindleBusiness> bindleBusinessesList = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private String categorySelected;
    private String networkCategory;

    public AddLocationFragment() {
    }

    public static AddLocationFragment newInstance() {
        return new AddLocationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_location, container, false);
        LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
          ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        findViews();
        getViewModel();
        initSearchView(location);
        initCategorySpinner();
        initRecyclerView();
        return rootView;
    }

    private void findViews() {
        lottieAnimationView = rootView.findViewById(R.id.bindle_color_load);
    }

    private void getViewModel() {
        viewModel = NetworkViewModel.getSingleInstance();
    }

    private void initSearchView(Location location) {
        SearchView searchView = rootView.findViewById(R.id.add_location_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                Log.d(TAG, "onQueryTextSubmit: " + location.getLatitude());
                bindleBusinessesList.clear();
                disposable.add(viewModel
                  .makeBindleBusinessNetworkCall(networkCategory, search, location.getLatitude() + "," + location.getLongitude())
                  .subscribe(bindleBusiness -> {
                      bindleBusinessesList.add(bindleBusiness);
                      adapter.addData(bindleBusiness);
                      lottieAnimationView.setVisibility(View.GONE);
                  }));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                adapter.setData(SearchViewListFilter.getFilteredList(search, bindleBusinessesList));
                return false;
            }
        });
    }

    private void initCategorySpinner() {
        Spinner categorySpinner = rootView.findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(ArrayAdapter.createFromResource(
          getContext(), R.array.foursquare_category_array, android.R.layout.simple_dropdown_item_1line));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lottieAnimationView.setVisibility(View.VISIBLE);
                bindleBusinessesList.clear();
                categorySelected = CategoryConstants.FIREBASE_CATEGORY_VERSION[position];
                networkCategory = CategoryConstants.NETWORK_CATEGORY_VERSION[position];
                viewModel.setCategorySelected(categorySelected);
                disposable.add(viewModel.makeBindleBusinessNetworkCall(networkCategory)
                  .doOnSubscribe(unit -> adapter.clear())
                  .subscribe(bindleBusiness -> {
                      bindleBusinessesList.add(bindleBusiness);
                      adapter.addData(bindleBusiness);
                      lottieAnimationView.setVisibility(View.GONE);
                  }, throwable -> Log.d(TAG, "accept: " + throwable)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = rootView.findViewById(R.id.add_location_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AddLocationAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
