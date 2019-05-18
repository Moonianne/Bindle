package org.pursuit.zonechat.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.pursuit.zonechat.model.Message;
import org.pursuit.zonechat.view.MessageAdapter;

public final class ChatViewModel extends ViewModel {
    private static final String ZONE_MESSAGES_CHILD = "zoneMessages";
    private final static String ZONE_CHAT = "zoneChat";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference messageDatabaseReference = firebaseDatabase.getReference().child(ZONE_CHAT).child(ZONE_MESSAGES_CHILD);
    private SnapshotParser<Message> parser;
    private String username = "anonymous";

    public DatabaseReference getMessageDatabaseReference() {
        return messageDatabaseReference;
    }

    public SnapshotParser<Message> getParser() {
        return parser;
    }

    public void parseMessage() {
        // New child entries
        parser = dataSnapshot -> {
            Message message = dataSnapshot.getValue(Message.class);
            if (message != null) {
                message.setId(dataSnapshot.getKey());
            }
            return message;
        };
    }

    public boolean hasText(CharSequence charSequence) {
        return charSequence.toString().trim().length() > 0;
    }

    public void pushMessage(String message) {
        messageDatabaseReference.push()
          .setValue(new Message(
            username, message, null, System.currentTimeMillis()));
    }
}
