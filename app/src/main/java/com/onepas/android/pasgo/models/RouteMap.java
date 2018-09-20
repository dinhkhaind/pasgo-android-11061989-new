package com.onepas.android.pasgo.models;

import java.util.ArrayList;
import java.util.List;

import com.onepas.android.pasgo.util.mapnavigator.Legs;

public class RouteMap {
	private String distanceText;
	private double distanceValue;
	private String durationText;
	private int durationValue;
	private String startAddress;
	private double startLocationLat;
	private double startLocationLng;
	private String endAddress;
	private double endLocationLat;
	private double endLocationLng;
	private String summary;
	private String jsonString;
	private String departure_timeText;
	private double departure_timeValue;
	private String arrival_timeText;
	private double arrival_timeValue;
	private List<Legs> legs = new ArrayList<Legs>();
	private List<MapStep> mapStepInfos = new ArrayList<MapStep>();
	private int soLuongSuCo;

	public String getDistanceText() {
		return distanceText;
	}

	public void setDistanceText(String distanceText) {
		this.distanceText = distanceText;
	}

	public double getDistanceValue() {
		return distanceValue;
	}

	public void setDistanceValue(double distanceValue) {
		this.distanceValue = distanceValue;
	}

	public String getDurationText() {
		return durationText;
	}

	public void setDurationText(String durationText) {
		this.durationText = durationText;
	}

	public int getDurationValue() {
		return durationValue;
	}

	public void setDurationValue(int durationValue) {
		this.durationValue = durationValue;
	}

	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public double getStartLocationLat() {
		return startLocationLat;
	}

	public void setStartLocationLat(double startLocationLat) {
		this.startLocationLat = startLocationLat;
	}

	public double getStartLocationLng() {
		return startLocationLng;
	}

	public void setStartLocationLng(double startLocationLng) {
		this.startLocationLng = startLocationLng;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public double getEndLocationLat() {
		return endLocationLat;
	}

	public void setEndLocationLat(double endLocationLat) {
		this.endLocationLat = endLocationLat;
	}

	public double getEndLocationLng() {
		return endLocationLng;
	}

	public void setEndLocationLng(double endLocationLng) {
		this.endLocationLng = endLocationLng;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<MapStep> getMapStepInfos() {
		return mapStepInfos;
	}

	public void setMapStepInfos(List<MapStep> mapStepInfos) {
		this.mapStepInfos = mapStepInfos;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getDeparture_timeText() {
		return departure_timeText;
	}

	public void setDeparture_timeText(String departure_timeText) {
		this.departure_timeText = departure_timeText;
	}

	public double getDeparture_timeValue() {
		return departure_timeValue;
	}

	public void setDeparture_timeValue(double departure_timeValue) {
		this.departure_timeValue = departure_timeValue;
	}

	public String getArrival_timeText() {
		return arrival_timeText;
	}

	public void setArrival_timeText(String arrival_timeText) {
		this.arrival_timeText = arrival_timeText;
	}

	public double getArrival_timeValue() {
		return arrival_timeValue;
	}

	public void setArrival_timeValue(double arrival_timeValue) {
		this.arrival_timeValue = arrival_timeValue;
	}

	public int getSoLuongSuCo() {
		return soLuongSuCo;
	}

	public void setSoLuongSuCo(int soLuongSuCo) {
		this.soLuongSuCo = soLuongSuCo;
	}

	public List<Legs> getLegs() {
		return legs;
	}

	public void setLegs(List<Legs> legs) {
		this.legs = legs;
	}
	
}
