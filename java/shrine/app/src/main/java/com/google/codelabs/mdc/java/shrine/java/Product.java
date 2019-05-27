package com.google.codelabs.mdc.java.shrine.java;

import android.content.res.Resources;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private String name;
    private String description;
    private String price;
    private String size;
    private String[] imagesUrl;
    private String userId;

    public Product() {

    }

    public Product(String name, String description, String price, String size, String[] imagesUrl, String userId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.size = size;
        this.imagesUrl = imagesUrl;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String[] getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String[] imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public static List<Product> initProductEntryList(List<Product> products) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("productentry");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Product product = snapshot.getValue(Product.class);
                    products.add(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return products;
    }
}
