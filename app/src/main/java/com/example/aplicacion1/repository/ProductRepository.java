package com.example.aplicacion1.repository;

import com.example.aplicacion1.model.Product;
import com.example.aplicacion1.network.ApiClient;
import com.example.aplicacion1.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private final ApiService apiService;

    public ProductRepository() {
        this.apiService = ApiClient.getApiService();
    }

    public interface CatalogCallback {
        void onSuccess(List<Product> products);
        void onError(String errorMessage);
    }

    public void fetchProducts(CatalogCallback callback) {
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error al obtener productos. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                callback.onError("Error de conexión a la red.");
            }
        });
    }
}