package model;

public class CartItem {
    private int productId;
    private String title;
    private double price;
    private int quantity;
    private String image;

    public CartItem(int productId, String title, double price, int quantity) {
        this(productId, title, price, quantity, null);
    }

    public CartItem(int productId, String title, double price, int quantity, String image) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public int getProductId() { return productId; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImage() { return image; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getSubtotal() { return price * quantity; }
}
