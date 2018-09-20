package com.onepas.android.pasgo.models;

import android.text.Html;
import android.text.Spanned;

import com.onepas.android.pasgo.utils.StringUtils;

public class MapStepChild {
	private String distanceText;
	private String distanceValue;
	private String durationText;
	private String durationValue;
	private String html_instructions;
	private String startLocationLat;
	private String startLocationLng;
	private String endLocationLat;
	private String endLocationLng;
	private String travel_mode;
	private String maneuver;
	private String summary;
	private String direction = "";
	

	public String getDistanceText() {
		return distanceText;
	}

	public void setDistanceText(String distanceText) {
		this.distanceText = distanceText;
	}

	public String getDistanceValue() {
		return distanceValue;
	}

	public void setDistanceValue(String distanceValue) {
		this.distanceValue = distanceValue;
	}

	public String getDurationText() {
		return durationText;
	}

	public void setDurationText(String durationText) {
		this.durationText = durationText;
	}

	public String getDurationValue() {
		return durationValue;
	}

	public void setDurationValue(String durationValue) {
		this.durationValue = durationValue;
	}

	public String getStartLocationLat() {
		return startLocationLat;
	}

	public void setStartLocationLat(String startLocationLat) {
		this.startLocationLat = startLocationLat;
	}

	public String getStartLocationLng() {
		return startLocationLng;
	}

	public void setStartLocationLng(String startLocationLng) {
		this.startLocationLng = startLocationLng;
	}

	public String getHtml_instructions() {
		return html_instructions;
	}

	public void setHtml_instructions(String html_instructions) {
		Spanned marked_up = StringUtils.fromHtml(html_instructions);
		this.html_instructions = marked_up.toString();
	}

	public String getTravel_mode() {
		return travel_mode;
	}

	public void setTravel_mode(String travel_mode) {
		this.travel_mode = travel_mode;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getEndLocationLat() {
		return endLocationLat;
	}

	public void setEndLocationLat(String endLocationLat) {
		this.endLocationLat = endLocationLat;
	}

	public String getEndLocationLng() {
		return endLocationLng;
	}

	public void setEndLocationLng(String endLocationLng) {
		this.endLocationLng = endLocationLng;
	}

	public String getManeuver() {
		return maneuver;
	}

	public void setManeuver(String maneuver) {
		this.maneuver = maneuver;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	
	
}
