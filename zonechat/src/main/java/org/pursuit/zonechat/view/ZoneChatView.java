package org.pursuit.zonechat.view;

import android.arch.lifecycle.ViewModelProviders;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import org.pursuit.zonechat.R;
import org.pursuit.zonechat.model.Message;
import org.pursuit.zonechat.viewmodel.ChatViewModel;

public final class ZoneChatView extends Fragment {

    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ChatViewModel viewModel;
    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView chatRecycler;
    private LinearLayoutManager layoutManager;
    private FirebaseUser fireBaseUser; //TODO: pass user
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> fireBaseAdapter;

    public static ZoneChatView newInstance() {
        return new ZoneChatView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ChatViewModel.class);

        //add code for database messages reference (read and write)

        // Initialize Firebase Auth
        // Firebase instance variables
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        fireBaseUser = firebaseAuth.getCurrentUser();
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

        viewModel.parseMessage();
        updateMessageList(viewModel.getMessageDatabaseReference());
        editTextListener();
        sendMessageOnClick();
        registerAdapter();
        chatRecycler.setAdapter(fireBaseAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        fireBaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        fireBaseAdapter.stopListening();
    }

    private void updateMessageList(Query query) {
        fireBaseAdapter = new MessageAdapter(new FirebaseRecyclerOptions.Builder<Message>()
          .setQuery(query, viewModel.getParser())
          .build(), viewModel);
        fireBaseAdapter.notifyDataSetChanged();
    }

    private void editTextListener() {
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (viewModel.hasText(charSequence)) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        messageEditText
          .setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }

    private void sendMessageOnClick() {
        sendButton.setOnClickListener(view -> {
            viewModel.pushMessage(messageEditText.getText().toString());
            messageEditText.setText("");
        });
    }

    private void registerAdapter() {
        fireBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                  (positionStart >= (fireBaseAdapter.getItemCount() - 1) &&
                    lastVisiblePosition == (positionStart - 1))) {
                    chatRecycler.scrollToPosition(positionStart);
                }
            }
        });
    }
}
