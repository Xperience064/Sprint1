package controller;

import model.CartItem;
import model.FakeStoreCartRequest;
import network.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> cartItems = new ArrayList<>();

    private CartManager() {}

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // US09: Agregar o sumar cantidad + llamada POST a FakeStoreAPI
    public void addItem(CartItem newItem, Runnable onSuccess, Runnable onError) {
        boolean exists = false;
        for (CartItem item : cartItems) {
            if (item.getProductId() == newItem.getProductId()) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                exists = true;
                break;
            }
        }
        if (!exists) {
            cartItems.add(newItem);
        }

        FakeStoreCartRequest request = new FakeStoreCartRequest(
                1, "2026-09-05",
                Collections.singletonList(new FakeStoreCartRequest.CartProductRequest(newItem.getProductId(), newItem.getQuantity()))
        );

        RetrofitClient.getApiService().addToCartApi(request).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful() && onSuccess != null) {
                    onSuccess.run();
                } else if (onError != null) {
                    onError.run();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (onError != null) {
                    onError.run();
                }
            }
        });
    }

    // US10: Modificar cantidad o eliminar + llamada PUT/DELETE a FakeStoreAPI
    public void updateQuantity(int productId, int newQuantity, Runnable onUpdated) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProductId() == productId) {
                if (newQuantity <= 0) {
                    cartItems.remove(i);
                    sendDeleteApiRequest(productId);
                } else {
                    cartItems.get(i).setQuantity(newQuantity);
                    sendPutApiRequest(productId, newQuantity);
                }
                break;
            }
        }
        if (onUpdated != null) {
            onUpdated.run();
        }
    }

    private void sendPutApiRequest(int productId, int quantity) {
        FakeStoreCartRequest request = new FakeStoreCartRequest(
                1, "2026-09-05",
                Collections.singletonList(new FakeStoreCartRequest.CartProductRequest(productId, quantity))
        );
        RetrofitClient.getApiService().updateCartApi(productId, request).enqueue(new Callback<Object>() {
            @Override public void onResponse(Call<Object> c, Response<Object> r) {}
            @Override public void onFailure(Call<Object> c, Throwable t) {}
        });
    }

    private void sendDeleteApiRequest(int productId) {
        RetrofitClient.getApiService().deleteCartApi(productId).enqueue(new Callback<Object>() {
            @Override public void onResponse(Call<Object> c, Response<Object> r) {}
            @Override public void onFailure(Call<Object> c, Throwable t) {}
        });
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // US10: Recálculo de total redondeado a 2 decimales
    public double getTotalPrice() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }
}