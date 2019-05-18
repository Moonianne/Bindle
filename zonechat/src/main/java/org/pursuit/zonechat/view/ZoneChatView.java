package org.pursuit.zonechat.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pursuit.zonechat.R;

public final class ZoneChatView extends Fragment {

    public static ZoneChatView newInstance() {
        return new ZoneChatView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zone_chat_view, container, false);
    }

}
