package com.onepas.android.pasgo.models;

/**
 * Created by Khai on 12/27/2014.
 */
public class DanhSachQuyDinh {
    private int id;
    private String name;
    private Integer image;
    public DanhSachQuyDinh(int id, String name, Integer image)
    {
        this.id=id;
        this.name=name;
        this.image=image;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

}
