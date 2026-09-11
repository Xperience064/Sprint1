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
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public ProductViewModel() {
        this.repository = new ProductRepository();
    }

    public LiveData<List<Product>> getProductsLiveData() {
        return productsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getError() { return error; }

    public LiveData<List<String>> getCategories() {
        return repository.getCategories();
    }

    public void loadAllProducts() {
        error.setValue(null);
        clearCurrentProducts();
        isLoading.setValue(true);
        repository.getAllProducts().observeForever(products -> {
            if (products == null) error.setValue("No se pudieron cargar los productos");
            else productsLiveData.setValue(products);
            isLoading.setValue(false);
        });
    }

    public void filterByCategory(String category) {
        error.setValue(null);
        clearCurrentProducts();
        isLoading.setValue(true);
        repository.getProductsByCategory(category).observeForever(products -> {
            if (products == null) error.setValue("No se pudieron filtrar los productos");
            else productsLiveData.setValue(products);
            isLoading.setValue(false);
        });
    }

    private void clearCurrentProducts() {
        productsLiveData.setValue(new ArrayList<>());
    }
}
