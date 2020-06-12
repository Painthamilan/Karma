package com.example.karma;

public class Orders {

    String ProductId;
    String ProductName;
    String ProductImage;
    String UserId;
    String Status;

    public Orders() {
    }

    public Orders(String productId, String productName, String productImage, String userId, String status) {
        ProductId = productId;
        ProductName = productName;
        ProductImage = productImage;
        UserId = userId;
        Status = status;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
