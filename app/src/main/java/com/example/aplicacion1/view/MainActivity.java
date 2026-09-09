package com.example.aplicacion1.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacion1.R;
import com.example.aplicacion1.ui.adapter.ProductAdapter;
import com.example.aplicacion1.ui.viewmodel.ProductViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProductViewModel viewModel;
    private ProductAdapter adapter;
    private ProgressBar progressBar;
    private ChipGroup chipGroupCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupViewModel();
        loadCategories();

        viewModel.loadAllProducts();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        viewModel.getProductsLiveData().observe(this, products -> adapter.setProductList(products));

        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadCategories() {
        viewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                setupChips(categories);
            }
        });
    }

    private void setupChips(List<String> categories) {
        chipGroupCategories.removeAllViews();

        Chip allChip = new Chip(this);
        allChip.setText("Ver todos");
        allChip.setCheckable(true);
        allChip.setChecked(true);
        chipGroupCategories.addView(allChip);

        for (String category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setCheckable(true);
            chipGroupCategories.addView(chip);
        }

        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            Chip selectedChip = group.findViewById(checkedId);
            if (selectedChip == null || selectedChip.getText().toString().equals("Ver todos")) {
                viewModel.loadAllProducts();
            } else {
                viewModel.filterByCategory(selectedChip.getText().toString());
            }
        });
    }
}