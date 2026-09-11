package controller;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import model.CartItem;
import model.FakeStoreCartRequest;
import model.FakeStoreCartResponse;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class CartManager {
    private static CartManager instance;
    private final List<CartItem> cartItems = new ArrayList<>();
    private final Gson gson = new Gson();
    private SharedPreferences preferences;
    private int userId = -1;
    private int remoteCartId = 1;

    private CartManager() {}
    public static synchronized CartManager getInstance() { if (instance == null) instance = new CartManager(); return instance; }

    public synchronized void initialize(Context context, int sessionUserId) {
        userId = sessionUserId;
        preferences = context.getApplicationContext().getSharedPreferences("carrito_local", Context.MODE_PRIVATE);
        remoteCartId = preferences.getInt("remote_cart_id", 1);
        List<CartItem> saved = gson.fromJson(preferences.getString("items_" + userId, "[]"), new TypeToken<List<CartItem>>(){}.getType());
        cartItems.clear(); if (saved != null) cartItems.addAll(saved);
    }

    public synchronized void addItem(CartItem newItem, Runnable onSuccess, Runnable onError) {
        if (userId < 1 || newItem == null || newItem.getQuantity() <= 0) { if (onError != null) onError.run(); return; }
        String backup = gson.toJson(cartItems);
        CartItem existing = null;
        for (CartItem item : cartItems) if (item.getProductId() == newItem.getProductId()) { existing = item; break; }
        if (existing == null) cartItems.add(newItem); else existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
        RetrofitClient.getApiService().addToCartApi(requestFor(newItem.getProductId(), newItem.getQuantity())).enqueue(new Callback<FakeStoreCartResponse>() {
            @Override public void onResponse(Call<FakeStoreCartResponse> call, Response<FakeStoreCartResponse> response) {
                if (response.isSuccessful()) { if (response.body()!=null && response.body().getId()>0) remoteCartId=response.body().getId(); persist(); if(onSuccess!=null)onSuccess.run(); }
                else { restore(backup); if(onError!=null)onError.run(); }
            }
            @Override public void onFailure(Call<FakeStoreCartResponse> call, Throwable t) { restore(backup); if(onError!=null)onError.run(); }
        });
    }

    public synchronized void updateQuantity(int productId, int newQuantity, Runnable onUpdated) {
        for (int i=0;i<cartItems.size();i++) if(cartItems.get(i).getProductId()==productId) {
            if(newQuantity<=0){cartItems.remove(i);deleteRemote();} else {cartItems.get(i).setQuantity(newQuantity);updateRemote(productId,newQuantity);} break;
        }
        persist(); if(onUpdated!=null)onUpdated.run();
    }
    private FakeStoreCartRequest requestFor(int productId,int quantity){String date=new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date());return new FakeStoreCartRequest(userId,date,Collections.singletonList(new FakeStoreCartRequest.CartProductRequest(productId,quantity)));}
    private void updateRemote(int productId,int quantity){RetrofitClient.getApiService().updateCartApi(remoteCartId,requestFor(productId,quantity)).enqueue(noOp());}
    private void deleteRemote(){RetrofitClient.getApiService().deleteCartApi(remoteCartId).enqueue(noOp());}
    private Callback<FakeStoreCartResponse> noOp(){return new Callback<FakeStoreCartResponse>(){@Override public void onResponse(Call<FakeStoreCartResponse> c,Response<FakeStoreCartResponse> r){}@Override public void onFailure(Call<FakeStoreCartResponse> c,Throwable t){}};}
    private void persist(){if(preferences!=null&&userId>0)preferences.edit().putString("items_"+userId,gson.toJson(cartItems)).putInt("remote_cart_id",remoteCartId).apply();}
    private void restore(String json){List<CartItem> saved=gson.fromJson(json,new TypeToken<List<CartItem>>(){}.getType());cartItems.clear();if(saved!=null)cartItems.addAll(saved);persist();}
    public List<CartItem> getCartItems(){return cartItems;}
    public void clear(){cartItems.clear();if(preferences!=null)preferences.edit().clear().apply();}
    public double getTotalPrice(){double total=0;for(CartItem item:cartItems)total+=item.getSubtotal();return Math.round(total*100.0)/100.0;}
}
