package com.example.aplicacion1.model;

import java.util.List;

public class CartDto {
    private int id;
    private int userId;
    private String date;
    private List<CartProduct> products;

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getDate() { return date; }
    public List<CartProduct> getProducts() { return products; }

    public static class CartProduct {
        private int productId;
        private int quantity;
        public int getProductId() { return productId; }
        public int getQuantity() { return quantity; }
    }
}
