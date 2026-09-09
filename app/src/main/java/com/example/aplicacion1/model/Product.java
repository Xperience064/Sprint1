package com.example.aplicacion1.model;

public class Product {
    private int id;
    private String title;
    private double price;
    private String image;

    public Product(int id, String title, double price, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
}