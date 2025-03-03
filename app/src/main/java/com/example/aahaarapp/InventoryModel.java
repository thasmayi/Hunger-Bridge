package com.example.aahaarapp;

public class InventoryModel {
    String item,expiry,description,userid, name;
    Long qty;
    public InventoryModel() {

    }

    public InventoryModel(String description, String item, String userid, Long qty, String expiry, String name) {
        this.item = item;
        this.qty = qty;
        this.description = description;
        this.userid = userid;
        this.expiry = expiry;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String name) {
        this.item = name;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long type) {
        this.qty = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

}
