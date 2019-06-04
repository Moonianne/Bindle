package com.example.exploregroup.view;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.interactionlistener.OnFragmentInteractionListener;
import com.example.exploregroup.R;
import com.squareup.picasso.Picasso;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;


public class ViewGroupFragment extends Fragment {

    public static final String GROUP_KEY = "VIEW_GROUP_KEY";
    public static final String GROUP_PREFS = "GROUP";
    public static final String CURRENT_GROUP_KEY = "current_group";
    private OnFragmentInteractionListener listener;
    private Group group;
    private SharedPreferences sharedPrefs;

    public static ViewGroupFragment newInstance(Group group) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GROUP_KEY, group);
        ViewGroupFragment viewGroupFragment = new ViewGroupFragment();
        viewGroupFragment.setArguments(bundle);
        return viewGroupFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getActivity().getSharedPreferences(GROUP_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_group, container, false);
        setViews(rootView);
        initJoinGroupButtonListener(rootView);
        return rootView;
    }

    private void setViews(View rootView) {
        if (getArguments() != null) {
            group = (Group) getArguments().getSerializable(GROUP_KEY);
            Picasso.get().load(group.getImage_url()).into(rootView.<ImageView>findViewById(R.id.view_group_location_image_view));
            rootView.<TextView>findViewById(R.id.view_group_name_text_view).setText(group.getTitle());
            rootView.<TextView>findViewById(R.id.view_group_address).setText(group.getAddress());
            rootView.<TextView>findViewById(R.id.view_group_description_text_view).setText(group.getDescription());
        }
    }

    private void initJoinGroupButtonListener(View rootView) {
        rootView.findViewById(R.id.join_group_button).setOnClickListener(v -> {
            if (!sharedPrefs.contains(CURRENT_GROUP_KEY)) {
                sharedPrefs.edit().putString(CURRENT_GROUP_KEY, group.getTitle()).apply();
                listener.inflateGroupChatFragment(group);
            } else {
                Toast.makeText(getContext(), "Already in Group", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
