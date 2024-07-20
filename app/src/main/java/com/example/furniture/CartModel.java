package com.example.furniture;

import android.os.Parcelable;

import java.io.Serializable;

public class CartModel implements Serializable {
    private String Price, img, Product_Category, Product_title, Product_Description, Product_Id, uuid,quantity,id,eachPrice;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEachPrice() {
        return eachPrice;
    }

    public void setEachPrice(String eachPrice) {
        this.eachPrice = eachPrice;
    }

    CartModel() {
    }

    public CartModel(String price, String img, String product_Category, String product_title, String product_Description, String product_Id, String uuid, String quantity, String id, String eachPrice) {
        Price = price;
        this.img = img;
        Product_Category = product_Category;
        Product_title = product_title;
        Product_Description = product_Description;
        Product_Id = product_Id;
        this.uuid = uuid;
        this.quantity = quantity;
        this.id = id;
        this.eachPrice = eachPrice;
    }



    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProduct_Category() {
        return Product_Category;
    }

    public void setProduct_Category(String product_Category) {
        Product_Category = product_Category;
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

    public String getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(String product_Id) {
        Product_Id = product_Id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
