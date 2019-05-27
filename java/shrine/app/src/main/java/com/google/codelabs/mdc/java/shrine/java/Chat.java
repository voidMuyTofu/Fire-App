package com.google.codelabs.mdc.java.shrine.java;

public class Chat {
    private String userId;
    private String productName;
    private String imageUrl;

    public Chat() { }

    public Chat(String userId, String productName, String imageUrl) {
        this.userId = userId;
        this.productName = productName;
        this.imageUrl = imageUrl;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId;
    }

    public String getProductName() { return productName;
    }

    public void setProductName(String productName) { this.productName = productName;
    }

    public String getImageUrl() { return imageUrl;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl;
    }
}
