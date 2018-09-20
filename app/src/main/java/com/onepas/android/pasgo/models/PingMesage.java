package com.onepas.android.pasgo.models;

public class PingMesage {
	private String channelName;
	private long ping;

	public PingMesage() {
		super();
	}

	public PingMesage(long value, String channelName) {
		this.setChannelName(channelName);
		this.setValue(value);
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public long getValue() {
		return ping;
	}

	public void setValue(long ping) {
		this.ping = ping;
	}

	public String toString() {
		return "PingMesage [channelName=" + channelName + ", ping=" + ping
				+ "]";
	}
}