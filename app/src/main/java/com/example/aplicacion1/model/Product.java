package com.example.aplicacion1.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    private int id;
    private String title;
    private double price;
    private String description;
    private String category;

    @SerializedName("image")
    private String image;

    public Product() {
    }

    public Product(
            int id,
            String title,
            double price,
            String description,
            String category,
            String image
    ) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
    }

    public Product(
            int id,
            String title,
            double price,
            String image
    ) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getImageUrl() {
        return image;
    }
}