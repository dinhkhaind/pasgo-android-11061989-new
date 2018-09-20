package com.onepas.android.pasgo.models;

import java.io.Serializable;

public class InfoChat implements Serializable {
	private int id;
	private String chatId;
	private String fullName;
	private String content;
	private String time;
	private static final long serialVersionUID = 1L;

	public InfoChat() {
		super();
	}

	public InfoChat(int id, String chatId, String fullName, String content,
			String time) {
		super();
		this.id = id;
		this.setChatId(chatId);
		this.fullName = fullName;
		this.content = content;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return fullName;
	}

	public void setName(String fullName) {
		this.fullName = fullName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String toString() {
		return "info [fullName=" + fullName + ", content=" + content
				+ ", time=" + time + ", chatId=" + chatId + "]";
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}