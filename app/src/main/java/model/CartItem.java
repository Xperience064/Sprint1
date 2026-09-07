package model;

public class CartItem {
    private int productId;
    private String title;
    private double price;
    private int quantity;

    public CartItem(int productId, String title, double price, int quantity) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getSubtotal() { return price * quantity; }
}