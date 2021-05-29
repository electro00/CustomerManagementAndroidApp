package com.example.customermanagement;

public class CustomerItem {
    private String name;
    private String itemName;
    private String price;
    private final int imageResource;

    public CustomerItem(String name, String itemName, String price, int imageResource) {
        this.name = name;
        this.itemName = itemName;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }
}
