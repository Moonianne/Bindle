package com.android.group.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.android.group.model.Venue;
import com.android.group.model.firebase.Group;

public class GroupViewModel extends ViewModel {

    private static final String TAG = "GroupViewModel";
    private Group currentGroup;

    public GroupViewModel() {}

    public void createGroup(String groupName, Venue userSelectedVenue, String groupDescription) {
        currentGroup = new Group.Builder(groupName,
                userSelectedVenue.getCategories()[0].getPluralName()
                , groupDescription)
                .setiD("")
                .setLat(userSelectedVenue.getLocation().getLat())
                .setLng(userSelectedVenue.getLocation().getLng())
                .setUserList(null)
                .setUsers(0)
                .build();
        Log.d(TAG, "createGroup: " + currentGroup.toString());
    }
}
