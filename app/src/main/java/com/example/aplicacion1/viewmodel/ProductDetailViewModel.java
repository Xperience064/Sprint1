package com.example.aplicacion1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aplicacion1.model.Product;
import com.example.aplicacion1.network.ApiService;
import com.example.aplicacion1.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailViewModel extends ViewModel {

    private final MutableLiveData<Product> productLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> errorLiveData = new MutableLiveData<>();
    private final ApiService apiService;

    public ProductDetailViewModel() {
        this.apiService = RetrofitClient.getApiService();
    }

    public LiveData<Product> getProduct() {
        return productLiveData;
    }

    public LiveData<Boolean> getError() {
        return errorLiveData;
    }

    public void fetchProductDetail(int productId) {
        apiService.getProductDetail(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue(true); // Escenario 3: Error / No existe
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                errorLiveData.setValue(true); // Escenario 3: Error de red
            }
        });
    }
}