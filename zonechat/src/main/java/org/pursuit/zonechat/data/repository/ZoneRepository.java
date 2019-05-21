package org.pursuit.zonechat.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ZoneRepository {
    private DatabaseReference messageDatabaseReference;


    public ZoneRepository(String zoneChat, String zoneMessagesChild) {
        messageDatabaseReference = FirebaseDatabase.getInstance()
          .getReference().child(zoneChat).child(zoneMessagesChild);
    }

    public DatabaseReference getMessageDatabaseReference() {
        return messageDatabaseReference;
    }

    public DatabaseReference pushMessage() {
        return messageDatabaseReference.push();
    }
}
