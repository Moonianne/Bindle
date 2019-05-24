package com.android.group.view.recyclerview;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.R;
import com.android.group.model.Venue;
import com.android.group.view.OnFragmentInteractionCompleteListener;
import com.android.group.viewmodel.NetworkViewModel;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.squareup.picasso.Picasso;

public class AddLocationViewHolder extends RecyclerView.ViewHolder {

    private ImageView venueImageView;
    private TextView venueNameTextView;
    private TextView venueAddressTextView;
    private TextView selectButton;
    private NetworkViewModel viewModel;
    private OnFragmentInteractionCompleteListener listener;
    private OnFragmentInteractionListener onFragmentInteractionListener;
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
                InflateDialogImageBox();
            }
        });
    }

    public void InflateDialogImageBox() {
        LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_venue_info, null);
        ImageView imageViewVenue = view.findViewById(R.id.imageView_venue_picture);
        TextView textViewVenueName = view.findViewById(R.id.textView_venue_name);
        TextView textViewVenueAddress = view.findViewById(R.id.textView_venue_address);
        textViewVenueName.setText(venue.getName());
        textViewVenueAddress.setText(venue.getLocation().getFormattedAddress().get(0).toString());
        setAlertDialog(view);
        Picasso.get().load(R.drawable.common_full_open_on_phone).into(imageViewVenue);
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
