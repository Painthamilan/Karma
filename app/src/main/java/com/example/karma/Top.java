package com.example.karma;

public class Top {
    String ItemName,ItemImage,ItemPrice,ItemId;

    public Top() {
    }

    public Top(String itemName, String itemImage, String itemPrice, String itemId) {
        ItemName = itemName;
        ItemImage = itemImage;
        ItemPrice = itemPrice;
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPPrice) {
        ItemPrice = itemPPrice;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }
}
