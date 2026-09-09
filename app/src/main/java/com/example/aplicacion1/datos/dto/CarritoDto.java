package com.example.aplicacion1.datos.dto;

import java.util.List;

public class CarritoDto {
    private int id;
    private int userId;
    private String date;
    private List<ItemCarrito> products; // El arreglo interno de productos

    // Clase anidada para desglosar la cantidad y el ID del artículo
    public static class ItemCarrito {
        private int productId;
        private int quantity;

        public int getProductId() { return productId; }
        public int getQuantity() { return quantity; }
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getDate() { return date; }
    public List<ItemCarrito> getProducts() { return products; }
}
