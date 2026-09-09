package com.example.aplicacion1.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.aplicacion1.R;
import com.example.aplicacion1.data.SessionManager;
import com.example.aplicacion1.model.Product;
import com.example.aplicacion1.viewmodel.ProductDetailViewModel;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvProductTitle, tvProductCategory, tvProductPrice, tvProductDescription;
    private LinearLayout layoutAdminButtons;

    private ProductDetailViewModel viewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();

        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId == -1) {
            handleError();
            return;
        }

        setupObservers();
        viewModel.fetchProductDetail(productId);
    }

    private void initViews() {
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductTitle = findViewById(R.id.tvProductTitle);
        tvProductCategory = findViewById(R.id.tvProductCategory);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        layoutAdminButtons = findViewById(R.id.layoutAdminButtons);
    }

    private void setupObservers() {
        viewModel.getProduct().observe(this, this::renderProductData);

        viewModel.getError().observe(this, isError -> {
            if (Boolean.TRUE.equals(isError)) {
                handleError(); // Escenario 3: Alerta y retorno al catálogo
            }
        });
    }

    private void renderProductData(Product product) {
        // Escenario 1: Datos estándar
        tvProductTitle.setText(product.getTitle());
        tvProductCategory.setText(product.getCategory());
        tvProductPrice.setText(String.format("$%.2f", product.getPrice()));
        tvProductDescription.setText(product.getDescription());

        Glide.with(this)
                .load(product.getImageUrl())
                .into(ivProductImage);

        // Regla de Negocio: Validar sesión local para controles de Admin
        String userRole = sessionManager.getUserRole();
        if ("ADMINISTRADOR".equalsIgnoreCase(userRole)) {
            // Escenario 2: Agregar controles dinámicos (sin instanciar ocultos)
            setupAdminControls();
        }
    }

    private void setupAdminControls() {
        layoutAdminButtons.removeAllViews();

        Button btnEdit = new Button(this);
        btnEdit.setText("Editar");
        LinearLayout.LayoutParams paramsEdit = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        paramsEdit.setMargins(0, 0, 8, 0);
        btnEdit.setLayoutParams(paramsEdit);
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Acción Editar", Toast.LENGTH_SHORT).show();
        });

        Button btnDelete = new Button(this);
        btnDelete.setText("Eliminar");
        LinearLayout.LayoutParams paramsDelete = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        paramsDelete.setMargins(8, 0, 0, 0);
        btnDelete.setLayoutParams(paramsDelete);
        btnDelete.setOnClickListener(v -> {
            Toast.makeText(this, "Acción Eliminar", Toast.LENGTH_SHORT).show();
        });

        layoutAdminButtons.addView(btnEdit);
        layoutAdminButtons.addView(btnDelete);
    }

    private void handleError() {
        Toast.makeText(this, "Producto no disponible", Toast.LENGTH_LONG).show();
        finish(); // Retorna automáticamente a la pantalla del catálogo
    }
}