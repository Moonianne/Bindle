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

    public String pushGroup(Group group) {
        String key = groupDatabaseReference.push().getKey();
        if (key != null) {
            groupDatabaseReference.child(key).setValue(group);
        }
        return key;
    }

    public DatabaseReference getMessageDatabaseReference() {
        return groupDatabaseReference;
    }
}
