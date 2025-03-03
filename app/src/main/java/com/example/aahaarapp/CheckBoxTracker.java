package com.example.aahaarapp;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxTracker {
    private static CheckBoxTracker single_instance = null;

    List<String> items;

    private CheckBoxTracker()
    {
        items = new ArrayList<>();
    }
    public void setItem(String item) {
        items.add(item);
    }
    public List<String> getItem() {
        return items;
    }
    public static synchronized CheckBoxTracker getInstance()
    {
        if (single_instance == null)
            single_instance = new CheckBoxTracker();

        return single_instance;
    }
}
