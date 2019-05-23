package org.pursuit.zonechat.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;

import org.pursuit.zonechat.data.repository.ZoneChatRepo;
import org.pursuit.zonechat.model.Message;

import java.text.DateFormat;
import java.util.Date;

public final class ChatViewModel extends ViewModel {
    private static final String ZONE_CHAT = "zone";
    private static final String ZONE_MESSAGES_CHILD = "pursuit";

    private final ZoneChatRepo zoneChatRepo =
      new ZoneChatRepo(ZONE_CHAT, ZONE_MESSAGES_CHILD);

    private SnapshotParser<Message> parser;
    private String username = "anonymous";

    public boolean hasText(CharSequence charSequence) {
        return charSequence.toString().trim().length() > 0;
    }

    public DatabaseReference getMessageDatabaseReference() {
        return zoneChatRepo.getMessageDatabaseReference();
    }

    public void pushMessage(String message) {
        zoneChatRepo.pushMessage()
          .setValue(new Message(
            username, message, null, System.currentTimeMillis()));
    }

    public SnapshotParser<Message> getParser() {
        return parser;
    }

    public void parseMessage() {
        parser = dataSnapshot -> {
            Message message = dataSnapshot.getValue(Message.class);
            if (message != null) {
                message.setId(dataSnapshot.getKey());
            }
            return message;
        };
    }

    @NonNull
    public String convertToReadableTime(@NonNull final Message message) {
        DateFormat formatter = DateFormat.getDateTimeInstance();
        return formatter.format(new Date(message.getTimeStamp()));
    }
}
