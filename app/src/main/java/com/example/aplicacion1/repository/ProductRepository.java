package com.example.aplicacion1.repository;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aplicacion1.model.Product;
import com.example.aplicacion1.network.ApiService;
import com.example.aplicacion1.network.RetrofitClient;


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

        this.apiService = RetrofitClient.getApiService();
    }

    public LiveData<List<Product>> getAllProducts() {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        apiService.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<String>> getCategories() {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        apiService.getCategories().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Product>> getProductsByCategory(String category) {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        apiService.getProductsByCategory(category).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);

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

            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;

            public void onFailure(Call<List<Product>> call, Throwable t) {
                callback.onError("Error de conexión a la red.");
            }
        });

    }
}