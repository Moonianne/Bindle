package org.pursuit.zonechat.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.pursuit.zonechat.R;
import org.pursuit.zonechat.model.Message;

public final class ZoneChatView extends Fragment {

    public static final String ZONE_MESSAGES_CHILD = "zoneMessages";
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static String ZONE_CHAT = "zoneChat";
    public static final String ANONYMOUS = "anonymous";

    private String username;
    private EditText messageEditText;
    private Button sendButton;

    RecyclerView chatRecycler;
    LinearLayoutManager layoutManager;

    // Firebase instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference messageDatabaseReference;
    FirebaseRecyclerOptions<Message> options;
    private SnapshotParser<Message> parser;

    public static ZoneChatView newInstance() {
        return new ZoneChatView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = ANONYMOUS;
        firebaseDatabase = FirebaseDatabase.getInstance();
        ZONE_CHAT = "zonechat1";

        //add code for database messages reference (read and write)
        messageDatabaseReference = firebaseDatabase.getReference().child(ZONE_CHAT).child(ZONE_MESSAGES_CHILD);

        // Initialize Firebase Auth
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zone_chat_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatRecycler = view.findViewById(R.id.chat_rv);
        chatRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        chatRecycler.setLayoutManager(layoutManager);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);

        Query messageQuery = messageDatabaseReference;
        updateMessageList(messageQuery);
        editTextListener();
        sendMessageOnClick();
        parseMessage();
        registerAdapter();
        chatRecycler.setAdapter(firebaseAdapter);
    }

    private void registerAdapter() {
        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                  (positionStart >= (friendlyMessageCount - 1) &&
                    lastVisiblePosition == (positionStart - 1))) {
                    chatRecycler.scrollToPosition(positionStart);
                }
            }
        });
    }

    private void parseMessage() {
        // New child entries
        parser = new SnapshotParser<Message>() {
            @Override
            public Message parseSnapshot(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    message.setId(dataSnapshot.getKey());
                }
                return message;
            }
        };
    }

    private void sendMessageOnClick() {
        // Send button sends a message and clears the EditText
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for firebase write data
                Message message = new Message(username, messageEditText.getText().toString(), null, System.currentTimeMillis());
                messageDatabaseReference.push().setValue(message);

                // Clear input box
                messageEditText.setText("");
            }
        });
    }

    private void editTextListener() {
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        messageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }

    private void updateMessageList(Query query) {
        options = new FirebaseRecyclerOptions.Builder<Message>()
          .setQuery(query, parser)
          .build();

        firebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
                holder.onBind(model);
            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false);
                return new MessageViewHolder(view);
            }
        };
        firebaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAdapter.stopListening();
    }
}
