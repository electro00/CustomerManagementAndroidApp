package com.example.customermanagement;

public class CustomerItem {
    private String id;
    private String name;
    private String itemName;
    private String paymentMethod;
    private String price;
    private int imageResource;
    private int favouritedCount;

    public CustomerItem() {
    }

    public CustomerItem(String name, String itemName, String paymentMethod, String price, int imageResource, int favouritedCount) {
        this.name = name;
        this.itemName = itemName;
        this.paymentMethod = paymentMethod;
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

    public String getPaymentMethod() {
        return paymentMethod;
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
