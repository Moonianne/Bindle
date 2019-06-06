package org.pursuit.zonechat.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.motion.MotionLayout;
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
import android.widget.Toast;

import com.android.interactionlistener.OnBackPressedInteraction;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import org.pursuit.firebasetools.model.Message;
import org.pursuit.firebasetools.model.Zone;
import org.pursuit.zonechat.R;
import org.pursuit.zonechat.viewmodel.ChatViewModel;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class ZoneChatFragment extends Fragment implements OnBackPressedInteraction {
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final String ZONE_NAME_KEY = "ZONE_KEY";
    private static final String ZONE_CHAT_KEY = "ZONE_CHAT_KEY";

    private ChatViewModel viewModel;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> fireBaseAdapter;
    private Disposable disposable;

    public static ZoneChatFragment newInstance(@NonNull final Zone zone) {
        ZoneChatFragment fragment = new ZoneChatFragment();
        Bundle args = new Bundle();
        args.putString(ZONE_NAME_KEY, zone.getName());
        args.putString(ZONE_CHAT_KEY, zone.getChatName());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        if (getArguments() != null) {
            viewModel.setCurrentZoneChat(Objects.requireNonNull(getArguments()
              .getString(ZONE_CHAT_KEY)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zone_chat_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            disposable = viewModel
              .getZone(Objects.requireNonNull(getArguments().getString(ZONE_NAME_KEY)))
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(zone -> setZoneTitle(view, zone),
                throwable -> Toast.makeText(getActivity(),
                  "Connection Failed, please try again later.", Toast.LENGTH_SHORT).show());
        }

        RecyclerView chatRecycler = view.findViewById(R.id.chat_rv);
        chatRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatRecycler.setLayoutManager(layoutManager);
        EditText messageEditText = view.findViewById(R.id.messageEditText);
        Button sendButton = view.findViewById(R.id.sendButton);


        viewModel.parseMessage();
        updateMessageList(viewModel.getMessageDatabaseReference());
        editTextListener(messageEditText, sendButton);
        sendMessageOnClick(messageEditText, sendButton);
        registerAdapter(chatRecycler, layoutManager);
        chatRecycler.setAdapter(fireBaseAdapter);
    }

    public void setZoneTitle(@NonNull View view, Zone zone) {
        view.<TextView>findViewById(R.id.zone_title).setText(zone.getName());
        view.<TextView>findViewById(R.id.location).setText(zone.getName());
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

    @Override
    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    private void updateMessageList(Query query) {
        fireBaseAdapter = new MessageAdapter(new FirebaseRecyclerOptions.Builder<Message>()
          .setQuery(query, viewModel.getParser())
          .build(), viewModel);
        fireBaseAdapter.notifyDataSetChanged();
    }

    private void editTextListener(EditText messageEditText, Button sendButton) {
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (viewModel.hasText(charSequence)) {
                    sendButton.setBackgroundResource(R.drawable.ic_send_purple_24dp);
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setBackgroundResource(R.drawable.ic_send_white_24dp);
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

    private void sendMessageOnClick(EditText messageEditText, Button sendButton) {
        sendButton.setOnClickListener(view -> {
            viewModel.pushMessage(messageEditText.getText().toString());
            messageEditText.setText("");
        });
    }

    private void registerAdapter(RecyclerView chatRecycler, LinearLayoutManager layoutManager) {
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

    @Override
    public boolean onBackPressed() {
        MotionLayout motionLayout = getView().findViewById(R.id.start_layout_zone);
        if (motionLayout.getProgress() != 0){
            motionLayout.transitionToStart();
            return true;
        }else {
            return false;
        }
    }
}
