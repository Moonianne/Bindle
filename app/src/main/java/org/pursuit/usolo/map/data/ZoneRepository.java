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
    private static final String PATH = "zones/";
    private static final String TAG = "ZoneRepository";

    public void loginToFirebase(@NonNull final String email,
                                @NonNull final String password) {
        // Authenticate with Firebase, and request location updates
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
          email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "firebase auth success");
            } else {
                Log.d(TAG, "firebase auth failed");
            }
        });
    }

    public void subscribeToUpdates(OnUpdatesEmittedListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(PATH);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Zone value = dataSnapshot.getValue(Zone.class);
                listener.emitUpdate(value);
                Log.d(TAG, "onDataChange: " + value.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //No-op
            }
        });
    }

    public interface OnUpdatesEmittedListener{
        void emitUpdate(Zone zone);
    }
}
