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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import org.pursuit.firebasetools.model.Message;
import org.pursuit.zonechat.R;
import org.pursuit.zonechat.viewmodel.ChatViewModel;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class ZoneChatFragment extends Fragment {

    private static final String ZONE_CHAT_KEY = "ZONE_CHAT_KEY";
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;


    private FirebaseRecyclerAdapter<Message, MessageViewHolder> fireBaseAdapter;
    private ChatViewModel viewModel;
    private Disposable disposable;

    public static ZoneChatFragment newInstance(@NonNull final String zoneKey) {
        ZoneChatFragment zoneChatFragment = new ZoneChatFragment();
        Bundle args = new Bundle();
        args.putString(ZONE_CHAT_KEY, zoneKey);
        zoneChatFragment.setArguments(args);
        return zoneChatFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zone_chat_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            disposable = viewModel
              .setCurrentZoneKey(Objects.requireNonNull(bundle.getString(ZONE_CHAT_KEY)))
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(zone -> {
                  view.<TextView>findViewById(R.id.zone_title).setText(zone.getName());
                  view.<TextView>findViewById(R.id.location).setText(zone.getName());
              });
        }
        RecyclerView chatRecycler = view.findViewById(R.id.chat_rv);
        chatRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatRecycler.setLayoutManager(layoutManager);
        setupMessageBox(view.findViewById(R.id.messageEditText),
          view.findViewById(R.id.sendButton));
        viewModel.parseMessage();
        updateMessageList(viewModel.getMessageDatabaseReference());
        setupFireAdapter(chatRecycler, layoutManager);
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

    public void setupMessageBox(EditText messageEditText, Button sendButton) {
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

        messageEditText.setFilters(new InputFilter[]{new InputFilter
          .LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        sendButton.setOnClickListener(v -> {
            viewModel.pushMessage(messageEditText.getText().toString());
            messageEditText.setText("");
        });
    }

    public void setupFireAdapter(RecyclerView chatRecycler, LinearLayoutManager layoutManager) {
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
