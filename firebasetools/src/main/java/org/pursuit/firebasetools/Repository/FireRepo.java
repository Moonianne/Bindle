package org.pursuit.firebasetools.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Message;
import org.pursuit.firebasetools.model.User;
import org.pursuit.firebasetools.model.Zone;
import org.pursuit.sqldelight.db.model.BindleDatabase;
import org.pursuit.usolo.ZoneModelQueries;

import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public final class FireRepo {
    private static final String TAG = "FireRepo.tst";
    private static final String ZONES_PATH = "zones";
    private static final String GROUPS_PATH = "groups";
    private static final String ZONECHATS_PATH = "zoneChats";
    private static final String GROUPCHATS_PATH = "groupChats";

    private final DatabaseReference zoneDataBaseReference;
    private final DatabaseReference zoneChatDataBaseReference;
    private final DatabaseReference groupDataBaseReference;
    private final DatabaseReference groupChatDataBaseReference;
    private final FirebaseStorage firebaseStorage;
    private DatabaseReference userDatabaseReference;

    private FireRepo() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        zoneDataBaseReference = FirebaseDatabase.getInstance().getReference(ZONES_PATH);
        zoneChatDataBaseReference = FirebaseDatabase.getInstance().getReference(ZONECHATS_PATH);
        groupDataBaseReference = FirebaseDatabase.getInstance().getReference(GROUPS_PATH);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        groupDataBaseReference.keepSynced(true);
        groupChatDataBaseReference = FirebaseDatabase.getInstance().getReference(GROUPCHATS_PATH);
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public void addZone(@NonNull final Zone zone) {
        zoneDataBaseReference.child(zone.getName()).setValue(zone);
    }

    public Maybe<Zone> getZone(@NonNull final String zoneKey) {
        return RxFirebaseDatabase
          .observeSingleValueEvent(zoneDataBaseReference.child(zoneKey), Zone.class)
          .subscribeOn(Schedulers.io());
    }

    public Flowable<Zone> getAllZones(@NonNull final Context context) {
        ZoneModelQueries queries = BindleDatabase.getInstance(context).getZoneModelQueries();
        return RxFirebaseDatabase
          .observeChildEvent(zoneDataBaseReference, Zone.class)
          .subscribeOn(Schedulers.io())
          .filter(event -> event.getEventType().equals(RxFirebaseChildEvent.EventType.ADDED))
          .map(RxFirebaseChildEvent::getValue)
          .doOnNext(zone -> queries.insertItem(zone.getLocation(),
            zone.getChatName(), zone.getName(), zone.getUserCount()));
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

    public String pushZoneChatMessage(@NonNull final String chatName,
                                      @NonNull final Message message) {
        //TODO: During authentication user should set a display name so we can pass that for usename.
        message.setUserName(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        DatabaseReference reference = zoneChatDataBaseReference.child(chatName).push();
        String key = reference.getKey();
        message.setiD(key);
        reference.setValue(message);
        return key;
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

    public Maybe<Group> getGroup(@NonNull final String key) {
        return RxFirebaseDatabase
          .observeSingleValueEvent(groupDataBaseReference.child(key), Group.class)
          .subscribeOn(Schedulers.io());
    }

    public Flowable<Group> getGroups() {
        return RxFirebaseDatabase.observeChildEvent(groupDataBaseReference, Group.class)
          .subscribeOn(Schedulers.io())
          .filter(event -> event.getEventType().equals(RxFirebaseChildEvent.EventType.ADDED))
          .map(RxFirebaseChildEvent::getValue);
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

    public String pushGroupChatMessage(@NonNull final String chatName,
                                       @NonNull final Message message) {
        //TODO: During authentication user should set a display name so we can pass that for usename.
        message.setUserName(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        DatabaseReference reference = groupChatDataBaseReference.child(chatName).push();
        String key = reference.getKey();
        message.setiD(key);
        reference.setValue(message);
        return key;
    }

    public void addGroupChat(@NonNull final String chatName) {
        groupChatDataBaseReference.child(chatName).push().setValue(new Message(
          System.currentTimeMillis(),
          "Bindle",
          "This is the start of your chat room."
        ));
    }

    public final FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Maybe<User> getUserInfo(@NonNull final String key) {
        Log.d(TAG, "getUserInfo: " + key);
        return RxFirebaseDatabase
          .observeSingleValueEvent(
            userDatabaseReference.child(key), User.class)
          .subscribeOn(Schedulers.io());
    }

    public void pushUser(@NonNull final User user) {
        userDatabaseReference
          .child(getCurrentUser().getUid())
          .setValue(user);
    }

    public final Query getZoneMessageDatabaseReference(@NonNull final String chatName) {
        return zoneChatDataBaseReference.child(chatName);
    }

    public final Query getGroupMessageDatabaseReference(@NonNull final String chatName) {
        return groupChatDataBaseReference.child(chatName);
    }

    public void uploadFile(@NonNull final Uri uri,
                           @NonNull final SharedPreferences.Editor editor,
                           @NonNull final OnUriReturnListener listener) {
        StorageReference uploadRef = firebaseStorage.getReference()
          .child(FirebaseAuth.getInstance().getUid() + "/" + uri.getLastPathSegment());
        UploadTask uploadTask = uploadRef.putFile(uri);
        uploadTask
          .addOnFailureListener(exception ->
            Log.d(TAG, "onFailure: " + exception.getMessage()))
          .addOnSuccessListener(taskSnapshot ->
            Log.d(TAG, "onSuccess: " + taskSnapshot.getBytesTransferred()));
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return uploadRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                if (downloadUri != null) {
                    editor.putString("PROFILE_PHOTO_URL", downloadUri.toString());
                    editor.apply();
                    listener.onUriReturn(downloadUri);
                }
            }
        });
    }

    public Task<Void> updateDisplayName(String displayName){
        return FirebaseAuth.getInstance().getCurrentUser()
                .updateProfile(new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build());
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

    public interface OnUriReturnListener {
        void onUriReturn(@NonNull final Uri uri);
    }

}
