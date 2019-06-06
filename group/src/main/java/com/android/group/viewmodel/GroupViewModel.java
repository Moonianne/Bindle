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

public final class GroupViewModel extends ViewModel {

    private static final String TAG = "GroupViewModel";
    private final FireRepo fireRepo = FireRepo.getInstance();
    private Group currentGroup;

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public void createGroup(String groupName,
                            BindleBusiness bindleBusiness,
                            String groupDescription) {
        List<User> userList = new LinkedList<>();
        User user =
          new User("Eric",
            fireRepo.getCurrentUser().getEmail(),
            "I like to code and dance bachata.",
            "Nightlife",
            "pursuit",
            groupName,
            null,
            null,
            fireRepo.getCurrentUser().getUid());
        userList.add(user);
        String[] formattedAddress = bindleBusiness.getVenue().getLocation().getFormattedAddress();
        currentGroup = new Group(userList, groupDescription,
          bindleBusiness.getVenue().getCategories()[0].getPluralName(),
          new LatLng(bindleBusiness.getVenue().getLocation().getLat(),
            bindleBusiness.getVenue().getLocation().getLng()), groupName.toLowerCase() + "Chat",
          groupName.toLowerCase(), 1, formattedAddress[0] + "\n" + formattedAddress[1],
          bindleBusiness.getBusiness().getImage_url(), bindleBusiness.getVenue().getName());
        Log.d(TAG, "createGroup: " + currentGroup.toString());
    }

    public void pushGroup() {
        fireRepo.addGroup(currentGroup);
    }
}
