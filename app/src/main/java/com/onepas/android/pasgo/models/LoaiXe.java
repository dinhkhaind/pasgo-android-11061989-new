package com.onepas.android.pasgo.models;

public class LoaiXe {
	private int id;
	private Integer image;
	private String name;

	public LoaiXe() {
	}

	public LoaiXe(int id, Integer image, String name) {
		this.id = id;
		this.image = image;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getImage() {
		return image;
	}

	public void setImage(Integer image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
