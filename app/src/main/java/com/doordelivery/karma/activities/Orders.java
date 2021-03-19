package com.doordelivery.karma.activities;

public class Orders {

    String ProductId;
    String ProductName;
    String ProductImage;
    String UserId;
    String Status;
    String PhoneNumber;
    String Address;

    public Orders() {
    }

    public Orders(String productId, String productName, String productImage, String userId, String status, String phoneNumber, String address) {
        ProductId = productId;
        ProductName = productName;
        ProductImage = productImage;
        UserId = userId;
        Status = status;
        PhoneNumber = phoneNumber;
        Address = address;
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

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
