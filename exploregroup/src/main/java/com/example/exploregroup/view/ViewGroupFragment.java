package com.example.exploregroup.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exploregroup.R;
import com.squareup.picasso.Picasso;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;


public class ViewGroupFragment extends Fragment {

    public static final String GROUP_KEY = "VIEW_GROUP_KEY";
    private View rootView;
    private FireRepo fireRepo;

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
        rootView = inflater.inflate(R.layout.fragment_view_group, container, false);
        Bundle bundle = getArguments();
        ImageView viewGroupImageView = rootView.findViewById(R.id.view_group_location_image_view);
        TextView viewGroupNameTextView = rootView.findViewById(R.id.view_group_name_text_view);
        TextView viewGroupDescriptionTextView = rootView.findViewById(R.id.view_group_description_text_view);
        if(bundle != null){
            Group group = (Group) getArguments().getSerializable(GROUP_KEY);
            Picasso.get().load(group.getImage_url()).into(viewGroupImageView);
            viewGroupNameTextView.setText(group.getTitle());
            viewGroupDescriptionTextView.setText(group.getDescription());
        }




        fireRepo = FireRepo.getInstance();
        return rootView;
    }

}
