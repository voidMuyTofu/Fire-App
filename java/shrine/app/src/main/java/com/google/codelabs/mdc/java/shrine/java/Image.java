package com.google.codelabs.mdc.java.shrine.java;

public class Image {
    private String imageName;
    private String imageUrl;

    public Image(){

    }

    public Image(String imageName, String imageUrl){
        if(imageName.trim().equals("")){
            imageName = "No name";
        }

        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
