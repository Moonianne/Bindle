package com.android.group.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.group.model.bindle.BindleBusiness;
import com.android.group.model.foursquare.Venue;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.User;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public final class GroupViewModel extends ViewModel {

    private static final String TAG = "GroupViewModel";
    private final FireRepo fireRepo = FireRepo.getInstance();
    private Group currentGroup;

    public final void createGroup(@NonNull final String groupName,
                                  @NonNull final BindleBusiness bindleBusiness,
                                  @NonNull final String groupDescription,
                                  @NonNull final String category,
                                  @NonNull final OnSuccessListener listener) {
        List<User> userList = new LinkedList<>();
        Disposable disposable = fireRepo
          .getUserInfo(fireRepo.getCurrentUser().getUid())
          .subscribe(user -> {
              user.setCurrentGroup(groupName);
              userList.add(user);
              String[] formattedAddress = bindleBusiness.getVenue().getLocation().getFormattedAddress();
              currentGroup = new Group(userList, groupDescription, category,
                new LatLng(bindleBusiness.getVenue().getLocation().getLat(),
                  bindleBusiness.getVenue().getLocation().getLng()), groupName.toLowerCase() + "Chat",
                groupName, 1, formattedAddress[0] + "\n" + formattedAddress[1],
                bindleBusiness.getBusiness().getImage_url(), bindleBusiness.getVenue().getName());
              pushGroup(listener);
              Log.d(TAG, "createGroup: " + currentGroup.toString());
          });
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }

    private void pushGroup(OnSuccessListener listener) {
        fireRepo.addGroup(currentGroup, listener);
    }
}
