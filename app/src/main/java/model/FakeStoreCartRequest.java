package model;

import java.util.List;

public class FakeStoreCartRequest {
    private int userId;
    private String date;
    private List<CartProductRequest> products;

    public FakeStoreCartRequest(int userId, String date, List<CartProductRequest> products) {
        this.userId = userId;
        this.date = date;
        this.products = products;
    }

    public static class CartProductRequest {
        private int productId;
        private int quantity;

        public CartProductRequest(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}