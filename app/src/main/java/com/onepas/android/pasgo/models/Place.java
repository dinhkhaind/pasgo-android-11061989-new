package com.onepas.android.pasgo.models;

import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Place {
	private String id;
	private String icon;
	private String name;
	private String vicinity;
	private Double latitude;
	private Double longitude;
	private Double khoangCach;
	private boolean checkStar;
    private int distanceWithCenter=0;
	ImageView imageView;

	public Place() {
		super();
	}

	public Double getKhoangCach() {
		return khoangCach;
	}

	public void setKhoangCach(Double khoangCach) {
		this.khoangCach = khoangCach;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public boolean isCheckStar() {
		return checkStar;
	}

	public void setCheckStar(boolean checkStar) {
		this.checkStar = checkStar;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

    public int getDistanceWithCenter() {
        return distanceWithCenter;
    }

    public void setDistanceWithCenter(int distanceWithCenter) {
        this.distanceWithCenter = distanceWithCenter;
    }

    public static Place jsonToPontoReferencia(JSONObject pontoReferencia,
			boolean b, int neaBySearch) {
		Place result = new Place();
		if (b) {
			// diem don
			try {

				JSONObject geometry = (JSONObject) pontoReferencia
						.get("geometry");
				JSONObject location = (JSONObject) geometry.get("location");
				result.setLatitude((Double) location.get("lat"));
				result.setLongitude((Double) location.get("lng"));
				result.setIcon(pontoReferencia.getString("icon"));
				result.setName(pontoReferencia.getString("name"));
				if (neaBySearch ==1) {
					result.setVicinity(pontoReferencia.getString("vicinity"));					
				}else {
					result.setVicinity(pontoReferencia.getString("formatted_address"));					
				}
				result.setId(pontoReferencia.getString("id"));
				result.setCheckStar(false);
			} catch (JSONException ex) {
				Logger.getLogger(Place.class.getName()).log(Level.SEVERE, null,
						ex);
			}

		} else {
			// diem den
			try {
				JSONObject geometry = (JSONObject) pontoReferencia
						.get("geometry");
				JSONObject location = (JSONObject) geometry.get("location");
				result.setLatitude((Double) location.get("lat"));
				result.setLongitude((Double) location.get("lng"));
				result.setIcon(pontoReferencia.getString("icon"));
				result.setName(pontoReferencia.getString("name"));
				result.setVicinity(pontoReferencia
						.getString("formatted_address"));
				result.setId(pontoReferencia.getString("id"));
				result.setCheckStar(false);
			} catch (JSONException ex) {
				Logger.getLogger(Place.class.getName()).log(Level.SEVERE, null,
						ex);
			}

		}
		return result;
	}

	@Override
	public String toString() {
		return "Place{" + "id=" + id + ", icon=" + icon + ", name=" + name
				+ ", latitude=" + latitude + ", longitude=" + longitude + '}';
	}

}