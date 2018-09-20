package com.onepas.android.pasgo.ui.chat;

import android.os.Bundle;

import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;

public class ChatActivity extends BaseAppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Pasgo.getInstance().isOnChatActivity = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Pasgo.getInstance().isOnChatActivity = false;
    }

    @Override
    public void onStartMoveScreen() {

    }

    @Override
    public void onUpdateMapAfterUserInterection() {

    }
}
