package com.google.codelabs.mdc.java.shrine.java;

public class Message {
    private String sender;
    private String reciever;
    private String message;
    private String productname;

    public Message() {

    }

    public Message(String sender, String reciever, String message, String productName) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.productname = productName;
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
}
