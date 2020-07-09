package com.example.karma;

public class Top {
    String ItemName,ItemImage,ItemPrice,ItemId,Percentage;

    public Top() {
    }

    public Top(String itemName, String itemImage, String itemPrice, String itemId, String percentage) {
        ItemName = itemName;
        ItemImage = itemImage;
        ItemPrice = itemPrice;
        ItemId = itemId;
        Percentage = percentage;
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

    public String getPercentage() {
        return Percentage;
    }

    public void setPercentage(String percentage) {
        Percentage = percentage;
    }
}
