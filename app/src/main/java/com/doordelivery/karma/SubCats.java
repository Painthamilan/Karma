package com.doordelivery.karma;

public class SubCats {
    private String SubCatagoryName,SubCatagoryImage;

    public SubCats() {
    }

    public SubCats(String subCatagoryName, String subCatagoryImage) {
        SubCatagoryName = subCatagoryName;
        SubCatagoryImage = subCatagoryImage;
    }

    public String getSubCatagoryName() {
        return SubCatagoryName;
    }

    public void setSubCatagoryName(String subCatagoryName) {
        SubCatagoryName = subCatagoryName;
    }

    public String getSubCatagoryImage() {
        return SubCatagoryImage;
    }

    public void setSubCatagoryImage(String subCatagoryImage) {
        SubCatagoryImage = subCatagoryImage;
    }
}
