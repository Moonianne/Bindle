package com.android.group.view.recyclerview;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.R;
import com.android.group.model.Venue;
import com.android.group.model.yelp.Business;
import com.android.group.view.OnFragmentInteractionCompleteListener;
import com.android.group.viewmodel.NetworkViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddLocationViewHolder extends RecyclerView.ViewHolder {

    private ImageView venueImageView;
    private TextView venueNameTextView;
    private TextView venueAddressTextView;
    private TextView venueRating;
    private TextView venuePhoneNumber;
    private TextView venuePrice;
    private ArrayList<Business> yelpBusinesses;
    private ImageView imageViewVenue;
    private TextView selectButton;
    private NetworkViewModel viewModel;
    private OnFragmentInteractionCompleteListener listener;
    private NetworkViewModel.OnInfoSelectedListener onInfoSelectedListener;
    private TextView moreInfoButton;
    private Venue venue;

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
        imageViewVenue = itemView.findViewById(R.id.imageView_venue_picture);
        venuePhoneNumber = itemView.findViewById(R.id.textView_venue_phone);
        venueRating = itemView.findViewById(R.id.textView_venue_rating);
        venuePrice = itemView.findViewById(R.id.textView_venue_price);
    }

    void onBind(final Venue venue) {
        this.venue = venue;
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
        moreInfoButton.setOnClickListener(v -> {
            viewModel.makeYelpNetworkCall(venue.getName());
            viewModel.setInfoSelectedListener(new NetworkViewModel.OnInfoSelectedListener() {
                @Override
                public void yelpDataLoaded(List<Business> businesses) {
                    yelpBusinesses = new ArrayList<>(businesses);
                    Log.d("joe'slist", "yelpDataLoaded: " + businesses.get(0).getImage_url());
                    InflateDialogImageBox(yelpBusinesses);
//                    Picasso.get().load(yelpBusinesses.get(0).getImage_url()).into(imageViewVenue);
                }
            });
        });
    }


    public void InflateDialogImageBox(List<Business> businesses) {
        LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
        View view = layoutInflater.inflate(R.layout.venue_info_dialog, null);
//        Picasso.get().load(businesses.get(0).getImage_url()).into(imageViewVenue);
//        venuePhoneNumber.setText(businesses.get(0).getPhone());
//        venueRating.setText(String.valueOf(businesses.get(0).getRating()));
//        venuePrice.setText(businesses.get(0).getPrice());
        TextView textViewVenueName = view.findViewById(R.id.textView_venue_name);
        TextView textViewVenueAddress = view.findViewById(R.id.textView_venue_address);
        textViewVenueName.setText(venue.getName());
        textViewVenueAddress.setText(venue.getLocation().getFormattedAddress().get(0).toString());
        setAlertDialog(view);
    }

    private void setAlertDialog(View view) {
        AlertDialog.Builder alertMessage = new AlertDialog.Builder(itemView.getContext())
          .setView(view)
          .setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
          });
        alertMessage.show();
    }
}
