package org.pursuit.zonechat.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ZoneChatRepo {
    private DatabaseReference messageDatabaseReference;


    public ZoneChatRepo(String zone, String zoneName) {
        messageDatabaseReference = FirebaseDatabase.getInstance()
          .getReference().child(zone).child(zoneName).child("zoneChat");
    }

    public DatabaseReference getMessageDatabaseReference() {
        return messageDatabaseReference;
    }

    public DatabaseReference pushMessage() {
        return messageDatabaseReference.push();
    }
}
