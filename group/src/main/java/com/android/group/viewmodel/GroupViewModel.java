package com.android.group.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

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
                            Venue userSelectedVenue,
                            String groupDescription) {
        List<User> userList = new LinkedList<>();
        User user = new User("Eric", "ericdiazjr21@gmail.com", "I like to code and dance bachata.", "Nightlife", "pursuit", null, null, null);
        userList.add(user);
        currentGroup = new Group(userList, groupDescription,
                userSelectedVenue.getCategories()[0].getPluralName(),
                new LatLng(userSelectedVenue.getLocation().getLat(),
                        userSelectedVenue.getLocation().getLng()), groupName.toLowerCase() + "Chat",
                groupName.toLowerCase(), 1);
        Log.d(TAG, "createGroup: " + currentGroup.toString());
    }

    public void pushGroup() {
        fireRepo.addGroup(currentGroup);
    }
}
