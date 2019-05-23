package com.android.group.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.R;
import com.android.group.model.Venue;
import com.android.group.view.OnFragmentInteractionCompleteListener;
import com.android.group.viewmodel.NetworkViewModel;
import com.squareup.picasso.Picasso;

public class AddLocationViewHolder extends RecyclerView.ViewHolder {

    private ImageView venueImageView;
    private TextView venueNameTextView;
    private TextView venueAddressTextView;
    private TextView selectButton;
    private NetworkViewModel viewModel;
    private OnFragmentInteractionCompleteListener listener;
    private TextView moreInfoButton;

    public AddLocationViewHolder(@NonNull View itemView) {
        super(itemView);
        initViewModel();
        initListener(itemView);
        findViews(itemView);
    }

    private void initViewModel() {
        viewModel = NetworkViewModel.getSingleInstance();
    }

    private void initListener(@NonNull View itemView) {
        listener = (OnFragmentInteractionCompleteListener) itemView.getContext();
    }


    private void findViews(@NonNull View itemView) {
        venueImageView = itemView.findViewById(R.id.venue_image);
        venueNameTextView = itemView.findViewById(R.id.venue_name);
        venueAddressTextView = itemView.findViewById(R.id.venue_address);
        selectButton = itemView.findViewById(R.id.select_text_view);
        moreInfoButton = itemView.findViewById(R.id.more_info_text_view);
    }

    void onBind(final Venue venue) {
        Picasso.get().load(R.drawable.ic_pin_drop_black_24dp).into(venueImageView);
        venueNameTextView.setText(venue.getName());
        venueAddressTextView.setText(venue.getLocation().getAddress());
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.closeFragment();
                viewModel.setUserSelectedVenue(venue);
            }
        });
        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentInteractionListener.inflateVenueInfoFragment();
            }
        });
    }
}
