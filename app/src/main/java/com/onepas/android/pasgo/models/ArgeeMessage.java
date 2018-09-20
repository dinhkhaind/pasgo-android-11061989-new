package com.onepas.android.pasgo.models;

public class ArgeeMessage {
	private String id;
	private String object;
	private String sendTo;
	private boolean accept;
	private boolean pick;

	public ArgeeMessage() {
		super();
	}

	public String toString() {
		String state = accept ? "1" : "0";
		String pickState = pick ? "1" : "0";
		return "ArgeeMessage [id=" + getId() + ", object=" + object
				+ ", accept=" + state + ", sendTo=" + sendTo + ", pick="
				+ pickState + "]";
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public boolean isPick() {
		return pick;
	}

	public void setPick(boolean isPick) {
		this.pick = isPick;
	}
}