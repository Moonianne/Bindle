package com.android.group.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.android.group.model.bindle.BindleBusiness;
import com.android.group.model.foursquare.Venue;
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

    public void createGroup(String groupName,
                            BindleBusiness bindleBusiness,
                            String groupDescription,
                            String category) {
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
              pushGroup();
              Log.d(TAG, "createGroup: " + currentGroup.toString());
          });
    }

    public void pushGroup() {
        fireRepo.addGroup(currentGroup);
    }
}
