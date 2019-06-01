package com.android.group.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.Query;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Message;

import java.text.DateFormat;
import java.util.Date;

public class GroupChatViewModel extends ViewModel {

    private final FireRepo fireRepo = FireRepo.getInstance();
    private String dBRef;

    private SnapshotParser<Message> parser;
    //TODO: Use FireBaseAuth to give viewmodel UserName.
    private String username = "anonymous";

    public boolean hasText(CharSequence charSequence) {
        return charSequence.toString().trim().length() > 0;
    }

    public Query getMessageDatabaseReference(String ref) {
        //Passing Reference to the chat room
        this.dBRef = ref;
        return fireRepo.getGroupMessageDatabaseReference(dBRef);
    }

    public void pushMessage(String message) {
        fireRepo.pushGroupChatMessage(dBRef,new Message(System.currentTimeMillis(), username, message));
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

}
