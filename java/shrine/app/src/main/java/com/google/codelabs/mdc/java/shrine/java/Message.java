package com.google.codelabs.mdc.java.shrine.java;

public class Message {
    private String sender;
    private String reciever;
    private String message;
    private String productname;
    private boolean issen;

    public Message() {

    }

    public Message(String sender, String reciever, String message, String productName, boolean issen) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.productname = productName;
        this.issen = issen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProductName() {
        return productname;
    }

    public void setProductName(String productName) {
        this.productname = productName;
    }

    public boolean isIssen() { return issen;
    }

    public void setIssen(boolean issen) { this.issen = issen;
    }
}
