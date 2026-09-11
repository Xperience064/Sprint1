package com.example.aplicacion1.network;

import com.example.aplicacion1.model.Product;
import com.example.aplicacion1.model.CartDto;
import com.example.aplicacion1.datos.dto.UsuarioDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface ApiService {

    @GET("products")
    Call<List<Product>> getAllProducts();

    @GET("products")
    Call<List<Product>> getProducts();

    @GET("products/categories")
    Call<List<String>> getCategories();

    @GET("products/category/{category}")
    Call<List<Product>> getProductsByCategory(
            @Path("category") String category
    );

    @GET("products/{id}")
    Call<Product> getProductDetail(
            @Path("id") int productId
    );

    @DELETE("products/{id}")
    Call<Product> deleteProduct(@Path("id") int productId);

    @GET("users")
    Call<List<UsuarioDto>> getUsers();

    @GET("carts")
    Call<List<CartDto>> getCarts();
}
