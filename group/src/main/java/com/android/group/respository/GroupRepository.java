package com.android.group.respository;

import com.android.group.model.firebase.Group;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class GroupRepository {
    private DatabaseReference groupDatabaseReference;

    public GroupRepository(String groupID, String groupChild) {
        groupDatabaseReference = FirebaseDatabase.getInstance()
          .getReference().child(groupID).child(groupChild);
    }

    public void pushGroup(Group group) {
        groupDatabaseReference.push().setValue(group);
    }

    public DatabaseReference getMessageDatabaseReference() {
        return groupDatabaseReference;
    }
}
