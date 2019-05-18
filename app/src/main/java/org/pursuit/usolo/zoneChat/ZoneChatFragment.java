package org.pursuit.usolo.zoneChat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pursuit.usolo.R;

public class ZoneChatFragment extends Fragment {



    public ZoneChatFragment() {
        // Required empty public constructor
    }

    public static ZoneChatFragment newInstance() {
        return new ZoneChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zone_chat, container, false);
    }

}
