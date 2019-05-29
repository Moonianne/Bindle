package org.pursuit.firebasetools.Repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Message;
import org.pursuit.firebasetools.model.User;
import org.pursuit.firebasetools.model.Zone;

import java.util.List;

public final class FireRepo {
    private static final String TAG = "FireRepo.tst";
    private static final String ZONES_PATH = "zones/";
    private static final String GROUPS_PATH = "groups/";
    private static final String ZONECHATS_PATH = "zoneChats/";
    private static final String GROUPCHATS_PATH = "groupChats/";

    private final DatabaseReference zoneDataBaseReference;
    private final DatabaseReference zoneChatDataBaseReference;
    private final DatabaseReference groupDataBaseReference;
    private final DatabaseReference groupChatDataBaseReference;

    private FireRepo() {
        zoneDataBaseReference = FirebaseDatabase.getInstance().getReference(ZONES_PATH);
        zoneChatDataBaseReference = FirebaseDatabase.getInstance().getReference(ZONECHATS_PATH);
        groupDataBaseReference = FirebaseDatabase.getInstance().getReference(GROUPS_PATH);
        groupChatDataBaseReference = FirebaseDatabase.getInstance().getReference(GROUPCHATS_PATH);
    }

    public void addZone(@NonNull final Zone zone) {
        zoneDataBaseReference.child(zone.getName()).setValue(zone);
    }

    public void getZone(@NonNull final OnZoneUpdateEmittedListener listener) {
        zoneDataBaseReference.child("pursuit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Zone zone = dataSnapshot.getValue(Zone.class);
                /// also write to database
                
                listener.onZoneUpdateEmitted(zone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //No-op
            }
        });
    }

    public void addUserToCount(@NonNull final String zoneName) {
        zoneDataBaseReference.child(zoneName).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Zone zone = mutableData.getValue(Zone.class);
                if (zone == null) {
                    return Transaction.success(mutableData);
                }
                zone.setUserCount(zone.getUserCount() + 1);
                mutableData.setValue(zone);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Log.d("TAG", "onComplete: " + databaseError);
            }
        });
    }

    public void removeUserFromCount(@NonNull final String zoneName) {
        zoneDataBaseReference.child(zoneName).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Zone zone = mutableData.getValue(Zone.class);
                if (zone == null) {
                    return Transaction.success(mutableData);
                }
                zone.setUserCount(zone.getUserCount() + 1);
                mutableData.setValue(zone);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Log.d("TAG", "onComplete: " + databaseError);
            }
        });
    }

    public void pushZoneChat(@NonNull final String chatName,
                             @NonNull final Message message) {
        //TODO: During authentication user should set a display name so we can pass that for usename.
        message.setUserName(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        zoneChatDataBaseReference.child(chatName).push().setValue(message);
    }

    public void addZoneChat(@NonNull final String chatName) {
        zoneChatDataBaseReference.child(chatName).push().setValue(new Message(
          System.currentTimeMillis(),
          "Bindle",
          "This is the start of your chat room."
        ));
    }

    public void addGroup(@NonNull final Group group) {
        groupDataBaseReference.child(group.getTitle()).setValue(group);
    }

    public void getGroup(@NonNull final String key,
                         @NonNull final OnGroupUpdateEmittedListener listener) {
        groupDataBaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onGroupUpdateEmitted(dataSnapshot.getValue(Group.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //No-op
            }
        });
    }

    public void getGroups(ChildEventListener listener){
        groupDataBaseReference.addChildEventListener(listener);
    }

    public void addUserToGroup(@NonNull final String groupKey) {
        zoneDataBaseReference.child(groupKey).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Group group = mutableData.getValue(Group.class);
                if (group == null) {
                    return Transaction.success(mutableData);
                }
                group.setUserCount(group.getUserCount() + 1);
                List<User> users = group.getUserList();
                users.add(new User()); //TODO: Add logic for adding self to a group.
                group.setUserList(users);
                mutableData.setValue(group);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Log.d(TAG, "onComplete: " + databaseError);
            }
        });
    }

    public void removeUserFromGroup(@NonNull final String groupKey) {
        zoneDataBaseReference.child(groupKey).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Group group = mutableData.getValue(Group.class);
                if (group == null) {
                    return Transaction.success(mutableData);
                }
                group.setUserCount(group.getUserCount() + 1);
                List<User> users = group.getUserList();
                users.remove(new User()); //TODO: Add logic for removing oneself from a group.
                group.setUserList(users);
                mutableData.setValue(group);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Log.d(TAG, "onComplete: " + databaseError);
            }
        });
    }

    public void pushGroupChat(@NonNull final String chatName,
                              @NonNull final Message message) {
        groupChatDataBaseReference.child(chatName).push().setValue(message);
    }

    public void addGroupChat(@NonNull final String chatName) {
        groupChatDataBaseReference.child(chatName).push().setValue(new Message(
          System.currentTimeMillis(),
          "Bindle",
          "This is the start of your chat room."
        ));
    }

    public final FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public final Query getZoneMessageDatabaseReference(@NonNull final String chatName) {
        return zoneChatDataBaseReference.child(chatName);
    }

    public final Query getGroupMessageDatabaseReference(@NonNull final String chatName) {
        return groupChatDataBaseReference.child(chatName);
    }

    public interface OnZoneUpdateEmittedListener {
        void onZoneUpdateEmitted(Zone zone);
    }

    public interface OnGroupUpdateEmittedListener {
        void onGroupUpdateEmitted(Group group);
    }

    public void loginToFireBase(@NonNull final String email,
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

    private static FireRepo instance;

    public static FireRepo getInstance() {
        if (instance == null) {
            instance = new FireRepo();
        }
        return instance;
    }

}
