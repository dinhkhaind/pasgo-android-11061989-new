package com.onepas.android.pasgo.ui.chat.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

/*
 * Created by troy379 on 04.04.17.
 */
public class Message implements IMessage,
        MessageContentType.Image, /*this is for default image messages implementation*/
        MessageContentType /*and this one is for custom content type (in this case - voice message)*/ {

    private String id;
    private String text;
    private Date createdAt;
    private User user;
    private Image image;


    private String maKhachHang;
    private String roomName;
    private String ngayTao;
    private String msgId;
    private String sender;
    private String receiver;
    private String message;
    private String usertype="";
    private String time;
    private int loaitinnhan;
    private String displayName;
    private String displayColor;
    private int xemThemLichSu;
    public Message(){

    }

    public Message(String id, User user, String text, Date createdAt, String displayName, String displayColor) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
        this.displayName = displayName;
        this.displayColor = displayColor;
    }
    public Message(String id,User user,String text, Date createAt, int xemThemLichSu) {
        this.id = id;
        this.xemThemLichSu = xemThemLichSu;
        this.createdAt = new Date();
        this.user = user;
        this.text = text;
        this.createdAt = createAt;
    }
    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }

    public String getStatus() {
        return "Sent";
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setImage(Image image) {
        this.image = image;
    }


    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getMaKhachHang() {
        return maKhachHang;
    }

    @Override
    public String getRoomName() {
        return roomName;
    }

    @Override
    public String getNgayTao() {
        return ngayTao;
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public String getReceiver() {
        return receiver;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getUsertype() {
        return usertype;
    }

    @Override
    public String getTime() {
        return time;
    }

    @Override
    public int getLoaitinnhan() {
        return loaitinnhan;
    }

    @Override
    public String getMsgId() {
        return msgId;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDisplayColor() {
        return displayColor;
    }

    @Override
    public int getXemThemLichSu() {
        return xemThemLichSu;
    }

    public void setXemThemLichSu(int xemThemLichSu) {
        this.xemThemLichSu = xemThemLichSu;
    }

    // mới
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLoaitinnhan(int loaitinnhan) {
        this.loaitinnhan = loaitinnhan;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDisplayColor(String displayColor) {
        this.displayColor = displayColor;
    }

}
