package com.example.aplicacion1.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aplicacion1.R;
import com.example.aplicacion1.adapter.ProductAdapter;
import com.example.aplicacion1.viewmodel.CatalogViewModel;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout layoutError;
    private TextView tvErrorMessage;
    private Button btnRetry;

    private ProductAdapter adapter;
    private CatalogViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        initViews();
        setupRecyclerView();

        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        observeViewModel();

        btnRetry.setOnClickListener(v -> viewModel.loadCatalog());

        viewModel.loadCatalog();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts);
        progressBar = findViewById(R.id.progressBar);
        layoutError = findViewById(R.id.layoutError);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnRetry = findViewById(R.id.btnRetry);
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) {
                recyclerView.setVisibility(View.GONE);
                layoutError.setVisibility(View.GONE);
            }
        });

        viewModel.getProducts().observe(this, products -> {
            if (products != null && !products.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                layoutError.setVisibility(View.GONE);
                adapter.setProducts(products);
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                recyclerView.setVisibility(View.GONE);
                layoutError.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(error);
            }
        });
    }
}