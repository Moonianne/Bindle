package com.android.group.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.R;
import com.android.group.model.Venue;
import com.android.group.model.yelp.Business;
import com.android.group.view.OnFragmentInteractionCompleteListener;
import com.android.group.viewmodel.NetworkViewModel;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddLocationViewHolder extends RecyclerView.ViewHolder {

    private ImageView venueImageView;
    private TextView venueNameTextView;
    private TextView venueAddressTextView;
    private ArrayList<Business> yelpBusinesses = new ArrayList<>();
    private TextView selectButton;
    private NetworkViewModel viewModel;
    private OnFragmentInteractionCompleteListener listener;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private TextView moreInfoButton;
    private Venue venue;
    private String fullAddress;

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
        onFragmentInteractionListener = (OnFragmentInteractionListener) itemView.getContext();
    }

    private void findViews(@NonNull View itemView) {
        venueImageView = itemView.findViewById(R.id.venue_image);
        venueNameTextView = itemView.findViewById(R.id.venue_name);
        venueAddressTextView = itemView.findViewById(R.id.venue_address);
        selectButton = itemView.findViewById(R.id.select_text_view);
        moreInfoButton = itemView.findViewById(R.id.more_info_text_view);
    }

    void onBind(final Venue venue) {
        this.venue = venue;
        getVenueAddress(venue);
        Picasso.get().load(R.drawable.ic_pin_drop_black_24dp).into(venueImageView);
        venueNameTextView.setText(venue.getName());
        venueAddressTextView.setText(venue.getLocation().getAddress());
        selectButton.setOnClickListener(v -> {
            listener.closeFragment();
            viewModel.setUserSelectedVenue(venue);
        });
        moreInfoButton.setOnClickListener(v -> {
            viewModel.makeYelpNetworkCall(venue.getName());
            viewModel.setInfoSelectedListener(businesses -> {
                yelpBusinesses.addAll(businesses);
                InflateDialogImageBox(yelpBusinesses);
            });
        });
    }

    private void InflateDialogImageBox(List<Business> businesses) {
        View view = LayoutInflater.from(itemView.getContext())
          .inflate(R.layout.venue_info_dialog, null);
        Picasso.get().load(yelpBusinesses.get(0).getImage_url()).resize(800, 350)
          .into(view.<ImageView>findViewById(R.id.imageView_venue_picture));
        view.<TextView>findViewById(R.id.textView_venue_phone)
          .setText(businesses.get(0).getPhone());
        view.<TextView>findViewById(R.id.textView_venue_rating)
          .setText(String.valueOf(businesses.get(0).getRating()));
        view.<TextView>findViewById(R.id.textView_venue_name)
          .setText(venue.getName());
        view.<TextView>findViewById(R.id.textView_venue_address)
          .setText(venue.getLocation().getFormattedAddress().get(0).toString());
        view.<Button>findViewById(R.id.button_directions)
          .setOnClickListener(v -> onFragmentInteractionListener.openDirections(fullAddress));
        setAlertDialog(view);
    }

    private void setAlertDialog(View view) {
        AlertDialog.Builder alertMessage = new AlertDialog.Builder(itemView.getContext())
          .setView(view)
          .setNeutralButton("CLOSE", (dialog, which) -> {
          });
        alertMessage.show();
    }

    private void getVenueAddress(Venue venue) {
        fullAddress = venue.getLocation().getAddress() + " "
          + venue.getLocation().getCity() + ","
          + venue.getLocation().getState() + " "
          + venue.getLocation().getPostalCode();
    }
}
