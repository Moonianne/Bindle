package org.pursuit.usolo.map.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.pursuit.usolo.map.model.Zone;

public final class ZoneRepository {

    private static final String PATH = "zone/";
    private static final String ZONE = "pursuit/";
    private static final String ZONECHAT = "ZONECHAT/";
    private static final String TAG = "ZoneChatRepo";

    private DatabaseReference zoneDataBaseReference;

    public ZoneRepository() {
        zoneDataBaseReference = FirebaseDatabase.getInstance()
          .getReference(PATH);
    }

    public void addZone(Zone zone) {

    }

    public void getZone(OnUpdatesEmittedListener listener) {
        zoneDataBaseReference.child("pursuit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.emitUpdate(dataSnapshot.getValue(Zone.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //No-op
            }
        });
    }

    public DatabaseReference getZoneChatReference(){
        return zoneDataBaseReference.child(ZONE).child(ZONECHAT);
    }

    public DatabaseReference getZoneLocationReference(){
        return zoneDataBaseReference.child(ZONE);
    }

    public interface OnUpdatesEmittedListener {
        void emitUpdate(Zone zone);
    }

    public void loginToFirebase(@NonNull final String email,
                                @NonNull final String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
          email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "firebase auth success");
            } else {
                Log.d(TAG, "firebase auth failed");
            }
        });
    }
}
