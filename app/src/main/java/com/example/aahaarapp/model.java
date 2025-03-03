package com.example.aahaarapp;

import java.sql.Timestamp;
import java.util.Date;

public class model {
    String items, userid;
    Date timestamp;
    public model() {

    }

    public model(String items, Date timestamp, String userid) {
        this.items = items;
        this.timestamp = timestamp;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
