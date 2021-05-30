package com.example.customermanagement;

public class CustomerItem {
    private String id;
    private String name;
    private String itemName;
    private String price;
    private int imageResource;
    private int favouritedCount;

    public CustomerItem() {
    }

    public CustomerItem(String name, String itemName, String price, int imageResource, int favouritedCount) {
        this.name = name;
        this.itemName = itemName;
        this.price = price;
        this.imageResource = imageResource;
        this.favouritedCount = favouritedCount;
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

    public int getFavouritedCount() {
        return favouritedCount;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
