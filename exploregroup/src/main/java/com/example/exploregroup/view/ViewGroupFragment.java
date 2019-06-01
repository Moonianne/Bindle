package com.example.exploregroup.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exploregroup.R;


public class ViewGroupFragment extends Fragment {

    private View rootView;

    public ViewGroupFragment() {
    }

    public static ViewGroupFragment newInstance(){
        return new ViewGroupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_group, container, false);
        return rootView;
    }

}
