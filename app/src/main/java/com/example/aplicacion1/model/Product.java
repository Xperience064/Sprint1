package com.example.aplicacion1.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    private int id;
    private String title;
    private double price;
    private String description;
    private String category;

    @SerializedName("image")
    private String imageUrl;

    // 1. Un solo constructor vacío
    public Product() {}

    // 2. Un solo constructor completo
    public Product(int id, String title, double price, String description, String category, String imageUrl) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // 3. Getters limpios y sin repetir (Aquí están los tuyos: getId y getTitle)
    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
}