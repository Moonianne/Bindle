package org.pursuit.zonechat.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Message;
import org.pursuit.firebasetools.model.User;
import org.pursuit.firebasetools.model.Zone;

import java.text.DateFormat;
import java.util.Date;

import io.reactivex.Maybe;

public final class ChatViewModel extends ViewModel {
    private final FireRepo fireRepo = FireRepo.getInstance();

    private SnapshotParser<Message> parser;
    //TODO: Use FireBaseAuth to give viewmodel UserName.
    private String currentZoneChat;
    private Zone currentZone = null;
    private String username = "anonymous";
    private User user;

    public boolean hasText(CharSequence charSequence) {
        return charSequence.toString().trim().length() > 0;
    }

    public Query getMessageDatabaseReference() {
        //TODO: Setup Passing Reference to the chat room
        return fireRepo.getZoneMessageDatabaseReference(currentZoneChat);
    }

    public void pushMessage(String message) {
        fireRepo.pushZoneChatMessage(currentZoneChat, new Message(System.currentTimeMillis(), username, message, ""));
    }

    public SnapshotParser<Message> getParser() {
        return parser;
    }

    public void parseMessage() {
        parser = dataSnapshot -> {
            Message message = dataSnapshot.getValue(Message.class);
            if (message != null) {
                message.setiD(dataSnapshot.getKey());
            }
            return message;
        };
    }

    @NonNull
    public String convertToReadableTime(@NonNull final Message message) {
        DateFormat formatter = DateFormat.getDateTimeInstance();
        return formatter.format(new Date(message.getTimeStamp()));
    }

    @NonNull
    public Maybe<Zone> getZone(@NonNull final String zoneKey) {
        return fireRepo
          .getZone(zoneKey)
          .doOnSuccess(zone -> {
              Log.d("jimenez", "getZone: " + zone.getChatName());
              currentZone = zone;
          });
    }

    public void setCurrentZoneChat(@NonNull final String chat) {
        this.currentZoneChat = chat;
    }
}
