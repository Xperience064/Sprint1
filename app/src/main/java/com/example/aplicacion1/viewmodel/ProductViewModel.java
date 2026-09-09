package com.example.aplicacion1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aplicacion1.model.Product;
import com.example.aplicacion1.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {

    private final ProductRepository repository;
    private final MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public ProductViewModel() {
        this.repository = new ProductRepository();
    }

    public LiveData<List<Product>> getProductsLiveData() {
        return productsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<String>> getCategories() {
        return repository.getCategories();
    }

    public void loadAllProducts() {
        clearCurrentProducts();
        isLoading.setValue(true);
        repository.getAllProducts().observeForever(products -> {
            productsLiveData.setValue(products);
            isLoading.setValue(false);
        });
    }

    public void filterByCategory(String category) {
        clearCurrentProducts();
        isLoading.setValue(true);
        repository.getProductsByCategory(category).observeForever(products -> {
            productsLiveData.setValue(products);
            isLoading.setValue(false);
        });
    }

    private void clearCurrentProducts() {
        productsLiveData.setValue(new ArrayList<>());
    }
}