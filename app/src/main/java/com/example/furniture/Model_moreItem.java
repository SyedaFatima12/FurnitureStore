package com.example.furniture;

public class Model_moreItem {
    public Model_moreItem() {
    }

    private String Product_img;
    private String price,uuid,Product_Id,Product_Description,Product_title,Product_Category;

    public Model_moreItem(String product_img, String price, String uuid, String product_Id, String product_Description, String product_title, String product_Category) {
        Product_img = product_img;
        this.price = price;
        this.uuid = uuid;
        Product_Id = product_Id;
        Product_Description = product_Description;
        Product_title = product_title;
        Product_Category = product_Category;
    }

    public String getProduct_img() {
        return Product_img;
    }

    public void setProduct_img(String product_img) {
        Product_img = product_img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(String product_Id) {
        Product_Id = product_Id;
    }

    public String getProduct_Description() {
        return Product_Description;
    }

    public void setProduct_Description(String product_Description) {
        Product_Description = product_Description;
    }

    public String getProduct_title() {
        return Product_title;
    }

    public void setProduct_title(String product_title) {
        Product_title = product_title;
    }

    public String getProduct_Category() {
        return Product_Category;
    }

    public void setProduct_Category(String product_Category) {
        Product_Category = product_Category;
    }
}
