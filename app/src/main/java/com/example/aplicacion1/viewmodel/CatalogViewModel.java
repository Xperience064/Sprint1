package com.example.aplicacion1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aplicacion1.model.Product;
import com.example.aplicacion1.repository.ProductRepository;

import java.util.List;

public class CatalogViewModel extends ViewModel {

    private final ProductRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public CatalogViewModel() {
        repository = new ProductRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<Product>> getProducts() {
        isLoading.setValue(true);
        LiveData<List<Product>> productsLiveData = repository.getAllProducts();
        isLoading.setValue(false);
        return productsLiveData;
    }

    public LiveData<List<String>> getCategories() {
        return repository.getCategories();
    }

    public LiveData<List<Product>> filterByCategory(String category) {
        isLoading.setValue(true);
        LiveData<List<Product>> filteredLiveData = repository.getProductsByCategory(category);
        isLoading.setValue(false);
        return filteredLiveData;
    }

    public void loadCatalog() {
        // Método de compatibilidad para evitar errores si la vista lo llama directamente
        getProducts();
    }
}