package com.onepas.android.pasgo.ui.chat.model;

import java.util.ArrayList;

/**
 * Created by Kai on 5/3/2017.
 */

public class MessageAll {
    private int offset;
    private int pagesize;
    private int errorcode;
    private ArrayList<Message> messageItems = new ArrayList<>();

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public ArrayList<Message> getMessageItems() {
        return messageItems;
    }

    public void setMessageItems(ArrayList<Message> messageItems) {
        this.messageItems = messageItems;
    }
}
