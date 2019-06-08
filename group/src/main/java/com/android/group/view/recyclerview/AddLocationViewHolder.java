package com.android.group.view.recyclerview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.group.R;
import com.android.group.model.bindle.BindleBusiness;
import com.android.group.view.OnFragmentInteractionCompleteListener;
import com.android.group.viewmodel.NetworkViewModel;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.squareup.picasso.Picasso;


class AddLocationViewHolder extends RecyclerView.ViewHolder {

    private NetworkViewModel viewModel;
    private OnFragmentInteractionCompleteListener listener;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private String fullAddress;

    public AddLocationViewHolder(@NonNull View itemView) {
        super(itemView);
        initViewModel();
        initListener(itemView);
    }

    private void initViewModel() {
        viewModel = NetworkViewModel.getSingleInstance();
    }

    private void initListener(@NonNull View itemView) {
        listener = (OnFragmentInteractionCompleteListener) itemView.getContext();
        onFragmentInteractionListener = (OnFragmentInteractionListener) itemView.getContext();
    }

    void onBind(final BindleBusiness bindleBusiness) {
        getVenueAddress(bindleBusiness);
        if (bindleBusiness.getBusiness() != null) {
            Picasso.get().load(bindleBusiness.getBusiness().getImage_url()).into(itemView.<ImageView>findViewById(R.id.venue_image));
        }
        itemView.<TextView>findViewById(R.id.venue_name).setText(bindleBusiness.getVenue().getName());
        itemView.<TextView>findViewById(R.id.venue_address).setText(String.format("%s\n%s",
                bindleBusiness.getVenue().getLocation().getFormattedAddress()[0],
                bindleBusiness.getVenue().getLocation().getFormattedAddress()[1]));
        itemView.<TextView>findViewById(R.id.select_text_view).setOnClickListener(v -> {
            listener.closeFragment();
            viewModel.setUserSelectedVenue(bindleBusiness);
        });
        itemView.<TextView>findViewById(R.id.more_info_text_view).setOnClickListener(v -> InflateDialogImageBox(bindleBusiness));
    }

    private void InflateDialogImageBox(BindleBusiness bindleBusiness) {
        if (bindleBusiness.getBusiness() != null) {
            View view = LayoutInflater.from(itemView.getContext())
                    .inflate(R.layout.venue_info_dialog, null);
            setViews(bindleBusiness, view);
            setAlertDialog(view);
        }
    }

    private void setAlertDialog(View view) {
        Dialog venueDialog = new Dialog(itemView.getContext());
        venueDialog.setContentView(R.layout.venue_info_dialog);
        venueDialog.setContentView(view);
        venueDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        venueDialog.show();
    }

    private void setViews(BindleBusiness bindleBusiness, View view) {
        Picasso.get().load(bindleBusiness.getBusiness().getImage_url())
                .into(view.<ImageView>findViewById(R.id.imageView_venue_picture));
        view.<TextView>findViewById(R.id.textView_venue_phone)
                .setText(bindleBusiness.getBusiness().getPhone());
        view.<TextView>findViewById(R.id.textView_venue_rating)
                .setText(String.valueOf(bindleBusiness.getBusiness().getRating()));
        view.<TextView>findViewById(R.id.textView_venue_name)
                .setText(bindleBusiness.getVenue().getName());
        view.<TextView>findViewById(R.id.textView_venue_address)
                .setText(fullAddress);
        view.<Button>findViewById(R.id.button_directions)
                .setOnClickListener(v -> onFragmentInteractionListener.openDirections(fullAddress));
    }

    private void getVenueAddress(BindleBusiness bindleBusiness) {
        fullAddress = bindleBusiness.getVenue().getLocation().getAddress() + " "
                + bindleBusiness.getVenue().getLocation().getCity() + ","
                + bindleBusiness.getVenue().getLocation().getState() + " "
                + bindleBusiness.getVenue().getLocation().getPostalCode();
    }
}
