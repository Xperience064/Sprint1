package com.example.aplicacion1.network;

import com.example.aplicacion1.model.Product;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("products/{id}")
    Call<Product> getProductDetail(@Path("id") int productId);
}