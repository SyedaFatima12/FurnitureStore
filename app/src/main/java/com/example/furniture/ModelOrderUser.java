package com.example.furniture;

public class ModelOrderUser {

    private  String OrderId,OrderTime,OrderStatus,OrderCost,OrderBy,orderTo,ShopName,quantity,Payment_Method,Product_title,Customer_name,Phone_no,Address;


    public ModelOrderUser() {
    }

    public ModelOrderUser(String orderId, String orderTime, String orderStatus, String orderCost, String orderBy, String orderTo, String shopName, String quantity, String payment_Method, String product_title, String customer_name, String phone_no, String address) {
        OrderId = orderId;
        OrderTime = orderTime;
        OrderStatus = orderStatus;
        OrderCost = orderCost;
        OrderBy = orderBy;
        this.orderTo = orderTo;
        ShopName = shopName;
        this.quantity = quantity;
        Payment_Method = payment_Method;
        Product_title = product_title;
        Customer_name = customer_name;
        Phone_no = phone_no;
        Address = address;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderCost() {
        return OrderCost;
    }

    public void setOrderCost(String orderCost) {
        OrderCost = orderCost;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(String orderBy) {
        OrderBy = orderBy;
    }

    public String getOrderTo() {
        return orderTo;
    }

    public void setOrderTo(String orderTo) {
        this.orderTo = orderTo;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPayment_Method() {
        return Payment_Method;
    }

    public void setPayment_Method(String payment_Method) {
        Payment_Method = payment_Method;
    }

    public String getProduct_title() {
        return Product_title;
    }

    public void setProduct_title(String product_title) {
        Product_title = product_title;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }

    public String getPhone_no() {
        return Phone_no;
    }

    public void setPhone_no(String phone_no) {
        Phone_no = phone_no;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

}

