package com.example.karma;

public class Cats {
private String CatagoryName,CatagoryImage;
private boolean HasSub;

    public Cats() {
    }

    public Cats(String catagoryName, String catagoryImage, boolean hasSub) {
        CatagoryName = catagoryName;
        CatagoryImage = catagoryImage;
        HasSub = hasSub;
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

    public boolean isHasSub() {
        return HasSub;
    }

    public void setHasSub(boolean hasSub) {
        HasSub = hasSub;
    }
}
