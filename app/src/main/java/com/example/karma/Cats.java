package com.example.karma;

public class Cats {
private String CatagoryName,CatagoryImage;

    public Cats() {
    }

    public Cats(String catagoryName, String catagoryImage) {
        CatagoryName = catagoryName;
        CatagoryImage = catagoryImage;
    }

    public String getCatagoryName() {
        return CatagoryName;
    }

    public void setCatagoryName(String catagoryName) {
        CatagoryName = catagoryName;
    }

    public String getCatagoryImage() {
        return CatagoryImage;
    }

    public void setCatagoryImage(String catagoryImage) {
        CatagoryImage = catagoryImage;
    }
}
