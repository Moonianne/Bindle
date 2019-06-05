package com.android.group.view.joingroup;


import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.R;
import com.android.group.view.OnFragmentInteractionCompleteListener;
import com.android.group.viewmodel.GroupChatViewModel;
import com.android.interactionlistener.OnFragmentInteractionListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Message;


public class GroupChatView extends Fragment {

    private static final String GROUP_OBJECT = "group_chat";
    public static final String GROUP_PREFS = "GROUP";
    public static final String CURRENT_GROUP_KEY = "current_group";

    private Group group;
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private GroupChatViewModel viewModel;
    private TextView groupTitle, groupLocation, groupCategory;
    private EditText groupMessageEditText;
    private Button groupSendButton, groupLeaveButton, groupBindlersButton, groupDetailsButton;
    private RecyclerView groupChatRecycler;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Message, GroupMessageViewHolder> fireBaseAdapter;
    private SharedPreferences sharedPreferences;
    private OnFragmentInteractionCompleteListener listener;
    private OnFragmentInteractionListener interactionListener;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionCompleteListener) {
            listener = (OnFragmentInteractionCompleteListener) context;
        } else {
            throw new RuntimeException();
        }
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Does not implement OnFragmentInteractionListener");
        }
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
        groupDetailsButton.setOnClickListener(v -> inflateDialogImageBox());
        groupLeaveButton.setOnClickListener(v -> {
            sharedPreferences = getActivity().getSharedPreferences(GROUP_PREFS, Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            listener.closeFragment();
        });

    }

    private void setUpGroupInfo() {
        groupTitle.setText(group.getTitle());
        groupLocation.setText(group.getAddress());
        groupCategory.setText(group.getCategory());
    }

    private void findViews(@NonNull View view) {
        groupChatRecycler = view.findViewById(R.id.groupChat_rv);
        groupTitle = view.findViewById(R.id.groupChat_title);
        groupLocation = view.findViewById(R.id.groupChat_location);
        groupCategory = view.findViewById(R.id.groupChat_category);
        groupSendButton = view.findViewById(R.id.groupChat_sendButton);
        groupLeaveButton = view.findViewById(R.id.leave_group_button);
        groupBindlersButton = view.findViewById(R.id.groupChat_viewMembers_button);
        groupMessageEditText = view.findViewById(R.id.groupChat_messageEditText);
        groupDetailsButton = view.findViewById(R.id.group_chat_view_details_button);
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
                    groupSendButton.setBackgroundResource(R.drawable.ic_send_purple_24dp);
                    groupSendButton.setEnabled(true);
                } else {
                    groupSendButton.setBackgroundResource(R.drawable.ic_send_white_24dp);
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

    private void inflateDialogImageBox() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.group_details_dialog, null);
        setViews(view);
        setAlertDialog(view);
    }

    private void setAlertDialog(View view) {
        Dialog venueDialog = new Dialog(view.getContext());
        venueDialog.setContentView(R.layout.venue_info_dialog);
        venueDialog.setContentView(view);
        venueDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        venueDialog.show();
    }

    private void setViews(View view) {
        Picasso.get().load(group.getImage_url())
                .into(view.<ImageView>findViewById(R.id.group_details_business_image_view));
        view.<TextView>findViewById(R.id.group_details_business_name_text_view)
                .setText(group.getBuiness_name());
        view.<TextView>findViewById(R.id.group_details_business_address_text_view)
                .setText(group.getAddress());
        view.<TextView>findViewById(R.id.group_details_description_text_view)
                .setText(group.getDescription());
        view.<Button>findViewById(R.id.group_details_button_directions)
                .setOnClickListener(v -> interactionListener.openDirections(group.getAddress()));
    }
}
