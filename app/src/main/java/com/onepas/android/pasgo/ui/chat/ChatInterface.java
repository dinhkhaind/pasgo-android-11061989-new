package com.onepas.android.pasgo.ui.chat;

import org.json.JSONObject;

/**
 * Created by Kai on 5/18/2017.
 */

public interface ChatInterface {
    void eConnect();
    void eConversation(JSONObject obj);
    void eMessage(JSONObject obj);
}
