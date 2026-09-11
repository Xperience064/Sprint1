package com.example.aplicacion1.repository;

import androidx.lifecycle.LiveData;

import com.example.aplicacion1.model.Product;

import java.util.List;

public interface RepositorioCatalogo {

    LiveData<List<Product>> getAllProducts();

    LiveData<List<String>> getCategories();

    LiveData<List<Product>> getProductsByCategory(String category);
}