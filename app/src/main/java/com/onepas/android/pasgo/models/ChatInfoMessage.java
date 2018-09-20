package com.onepas.android.pasgo.models;

public class ChatInfoMessage {
	private int kindOf;
	private InfoChat info;
	private PTLocationInfo location;

	public ChatInfoMessage() {
	}

	public int getKindOf() {
		return kindOf;
	}

	public void setKindOf(int kindOf) {
		this.kindOf = kindOf;
	}

	public InfoChat getInfo() {
		return info;
	}

	public void setInfo(InfoChat info) {
		this.info = info;
	}

	public String toString() {
		return "ChatInfoMessage [kindOf=" + kindOf + ", info=" + info
				+ ", location=" + location + "]";
	}

	public PTLocationInfo getLocation() {
		return location;
	}

	public void setLocation(PTLocationInfo location) {
		this.location = location;
	}
}