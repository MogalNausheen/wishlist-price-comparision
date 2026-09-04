package com.team.wishlist.entity;

public class ExternalProduct {

    private String name;
    private String price;
    private String store;
    private String imageUrl;
    private String productUrl;
    private boolean refurbished;

    public ExternalProduct() {
    }

    public ExternalProduct(
            String name,
            String price,
            String store,
            String imageUrl,
            String productUrl,
            boolean refurbished) {

        this.name = name;
        this.price = price;
        this.store = store;
        this.imageUrl = imageUrl;
        this.productUrl = productUrl;
        this.refurbished = refurbished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public boolean isRefurbished() {
        return refurbished;
    }

    public void setRefurbished(boolean refurbished) {
        this.refurbished = refurbished;
    }
}