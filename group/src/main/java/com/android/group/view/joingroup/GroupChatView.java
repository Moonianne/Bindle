package com.android.group.view.joingroup;


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

import com.android.group.R;
import com.android.group.viewmodel.GroupChatViewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Message;


public class GroupChatView extends Fragment {

    private static final String GROUP_OBJECT = "group_chat";

    private Group group;
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private GroupChatViewModel viewModel;
    private TextView groupTitle, groupLocation, groupCategory, groupDescription;
    private EditText groupMessageEditText;
    private Button groupSendButton, groupLeaveButton, groupBindlersButton;
    private RecyclerView groupChatRecycler;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Message, GroupMessageViewHolder> fireBaseAdapter;

    public GroupChatView() {
    }

    public static GroupChatView newInstance(Group group) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GROUP_OBJECT, group);
        GroupChatView fragment = new GroupChatView();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(GroupChatViewModel.class);
        if (getArguments() != null) {
            group = (Group) getArguments().getSerializable(GROUP_OBJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_chat_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setUpGroupInfo();

        layoutManager = new LinearLayoutManager(getContext());
        groupChatRecycler.setLayoutManager(layoutManager);
        viewModel.parseMessage();
        updateMessageList(viewModel.getMessageDatabaseReference(group.getChatName()));
        editTextListener();
        sendMessageOnClick();
        registerAdapter();
        groupChatRecycler.setAdapter(fireBaseAdapter);

    }

    private void setUpGroupInfo() {
        groupTitle.setText(group.getTitle());
        groupLocation.setText(group.getAddress());
        groupCategory.setText(group.getCategory());
        groupDescription.setText(group.getDescription());
    }

    private void findViews(@NonNull View view) {
        groupChatRecycler = view.findViewById(R.id.groupChat_rv);
        groupTitle = view.findViewById(R.id.groupChat_title);
        groupLocation = view.findViewById(R.id.groupChat_location);
        groupCategory = view.findViewById(R.id.groupChat_category);
        groupDescription = view.findViewById(R.id.groupChat_description);
        groupSendButton = view.findViewById(R.id.groupChat_sendButton);
        groupLeaveButton = view.findViewById(R.id.leave_group_button);
        groupBindlersButton = view.findViewById(R.id.groupChat_viewMembers_button);
        groupMessageEditText = view.findViewById(R.id.groupChat_messageEditText);
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
        fireBaseAdapter = new GroupMessageAdapter(new FirebaseRecyclerOptions.Builder<Message>()
          .setQuery(query, viewModel.getParser())
          .build(), viewModel);
        fireBaseAdapter.notifyDataSetChanged();
    }

    private void editTextListener() {
        groupMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (viewModel.hasText(charSequence)) {
                    groupSendButton.setEnabled(true);
                } else {
                    groupSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        groupMessageEditText
          .setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }

    private void sendMessageOnClick() {
        groupSendButton.setOnClickListener(view -> {
            viewModel.pushMessage(groupMessageEditText.getText().toString());
            groupMessageEditText.setText("");
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
                    groupChatRecycler.scrollToPosition(positionStart);
                }
            }
        });
    }
}
