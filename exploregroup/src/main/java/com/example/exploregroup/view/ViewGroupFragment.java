package com.example.exploregroup.view;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private OnFragmentInteractionListener listener;
    private FireRepo fireRepo;
    private Group group;

    public ViewGroupFragment() {
    }

    public static ViewGroupFragment newInstance(Group group){
        Bundle bundle = new Bundle();
        bundle.putSerializable(GROUP_KEY, group);
        ViewGroupFragment viewGroupFragment = new ViewGroupFragment();
        viewGroupFragment.setArguments(bundle);
        return viewGroupFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_group, container, false);
        if(getArguments() != null){
            group = (Group) getArguments().getSerializable(GROUP_KEY);
            Picasso.get().load(group.getImage_url()).into(rootView.<ImageView>findViewById(R.id.view_group_location_image_view));
            rootView.<TextView>findViewById(R.id.view_group_name_text_view).setText(group.getTitle());
            rootView.<TextView>findViewById(R.id.view_group_description_text_view).setText(group.getDescription());
        }

        rootView.findViewById(R.id.join_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

                if(sharedPrefs.getString("current_group","").equals("")) {
                    sharedPrefs.edit().putString("current_group", group.getChatName()).apply();
                    listener.inflateGroupChatFragment(group);
                }else{
                    Toast.makeText(getContext(), "Already in Group", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fireRepo = FireRepo.getInstance();
        return rootView;
    }

}
