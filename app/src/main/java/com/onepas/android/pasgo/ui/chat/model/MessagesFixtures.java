package com.onepas.android.pasgo.ui.chat.model;

import com.onepas.android.pasgo.ui.chat.model.Message;
import com.onepas.android.pasgo.ui.chat.model.User;

import java.util.Date;

/*
 * Created by troy379 on 12.12.16.
 */
public final class MessagesFixtures {// extends FixturesData
    private MessagesFixtures() {
        throw new AssertionError();
    }

    public static Message getImageMessage(boolean isKhach, String name, String displayName, String displayColor) {
        Message message = new Message("", getUser(isKhach, name), null,new Date(), displayName, displayColor);
        message.setImage(new Message.Image("https://habrastorage.org/getpro/habr/post_images/e4b/067/b17/e4b067b17a3e414083f7420351db272b.jpg"));
        return message;
    }

    public static Message getTextMessage(String text, boolean isKhach, String name, Date date, String displayName, String displayColor) {
        return new Message("New Id", getUser(isKhach, name), text,date, displayName, displayColor);
    }
    public static Message getMessageXemThemLichSu(Date date,int isXemThemLichSu) {
        return new Message("PasGoId",getUser(false, ""), "xem thêm lịch sử", date,isXemThemLichSu);
    }

    public static User getUser(boolean isKhach, String name) {
        return new User(
                isKhach ? "0" : "1",name,
                "http://i.imgur.com/pv1tBmT.png",
                true);
    }
}
