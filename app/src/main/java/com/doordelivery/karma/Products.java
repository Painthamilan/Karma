package com.doordelivery.karma;

public class Products {
    String ProductId;
    String Price;
    String ProductName;
    String ProductImage;
    String Percentage;


    public Products() {
    }

    public Products(String productId, String price, String productName, String productImage, String percentage) {
        ProductId = productId;
        Price = price;
        ProductName = productName;
        ProductImage = productImage;
        Percentage = percentage;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getPercentage() {
        return Percentage;
    }

    public void setPercentage(String percentage) {
        Percentage = percentage;
    }
}
