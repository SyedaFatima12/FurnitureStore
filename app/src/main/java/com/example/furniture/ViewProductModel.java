package com.example.furniture;

public class ViewProductModel {
    private String Product_img, Product_title, Product_Description, price;

    public ViewProductModel() {
    }

    public ViewProductModel(String product_img, String product_title, String product_Description, String price) {
        Product_img = product_img;
        Product_title = product_title;
        Product_Description = product_Description;
        this.price = price;
    }

    public String getProduct_img() {
        return Product_img;
    }

    public void setProduct_img(String product_img) {
        Product_img = product_img;
    }

    public String getProduct_title() {
        return Product_title;
    }

    public void setProduct_title(String product_title) {
        Product_title = product_title;
    }

    public String getProduct_Description() {
        return Product_Description;
    }

    public void setProduct_Description(String product_Description) {
        Product_Description = product_Description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
