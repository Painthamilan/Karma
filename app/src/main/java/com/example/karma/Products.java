package com.example.karma;

public class Products {
        String Price;
        String ProductName;
        String ProductImage;

        public Products() {
        }

        public Products(String price, String productName, String productImage) {
            Price = price;
            ProductName = productName;
            ProductImage = productImage;
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

}
