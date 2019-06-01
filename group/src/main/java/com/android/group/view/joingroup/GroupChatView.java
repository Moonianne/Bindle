package com.android.group.view.joingroup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.group.R;

import org.pursuit.firebasetools.model.Group;


public class GroupChatView extends Fragment {

    private static final String GROUP_TITLE = "group_title";

    private String groupTitle;

    public GroupChatView() {
    }

    public static GroupChatView newInstance(Group group) {
        GroupChatView fragment = new GroupChatView();
        Bundle args = new Bundle();
        args.putString(GROUP_TITLE, group.getTitle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupTitle = getArguments().getString(GROUP_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_chat_view, container, false);
    }

}
