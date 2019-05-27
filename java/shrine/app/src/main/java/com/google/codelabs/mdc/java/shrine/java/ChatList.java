package com.google.codelabs.mdc.java.shrine.java;

public class ChatList {
    private String id;
    private String productName;
    private String imageUrl;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ChatList(String id, String productName, String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.imageUrl = imageUrl;
    }

    public ChatList() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
