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
    private final MutableLiveData<List<Product>> products = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    public CatalogViewModel() {
        this.repository = new ProductRepository();
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<List<Product>> getProducts() { return products; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void loadCatalog() {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        repository.fetchProducts(new ProductRepository.CatalogCallback() {
            @Override
            public void onSuccess(List<Product> productList) {
                isLoading.setValue(false);
                products.setValue(productList);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                errorMessage.setValue(error);
            }
        });
    }
}