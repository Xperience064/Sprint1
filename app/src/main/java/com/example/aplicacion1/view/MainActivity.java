package com.example.aplicacion1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacion1.R;
import com.example.aplicacion1.adapter.ProductAdapter;
import com.example.aplicacion1.viewmodel.ProductViewModel;
import com.example.aplicacion1.funcionalidad.autenticacion.controlador.ControladorCierreSesion;
import com.example.aplicacion1.funcionalidad.autenticacion.vista.LoginActivity;
import com.example.aplicacion1.funcionalidad.crearproducto.vista.CrearProductoActivity;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.nucleo.carrito.LimpiadorCarrito;
import com.example.aplicacion1.nucleo.carrito.LimpiadorCarritoLocal;
import com.example.aplicacion1.nucleo.sesion.GestorSesion;
import com.example.aplicacion1.nucleo.sesion.GestorSesionLocal;
import com.example.aplicacion1.view.ProductDetailActivity;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajuste de márgenes para pantallas modernas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupRecyclerView();
        setupViewModel();
        loadCategories();
        setupBotonesSesionYAdmin(); // Método rescatado del segundo archivo

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
            progressBar.setVisibility(isLoading != null && isLoading ? View.VISIBLE : View.GONE);
        });
    }

    private void loadCategories() {
        viewModel.getCategories().observe(this, categories -> {
            if (categories != null) setupChips(categories);
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

        // ¡Esta era la llave que faltaba y rompía todo!
        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            Chip selectedChip = group.findViewById(checkedId);
            if (selectedChip == null || selectedChip.getText().toString().equals("Ver todos")) {
                viewModel.loadAllProducts();
            } else {
                viewModel.filterByCategory(selectedChip.getText().toString());
            }
        });
    }

    private void setupBotonesSesionYAdmin() {
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        Button btnAgregarProducto = findViewById(R.id.btnAgregarProducto);

        GestorSesion gestorSesion = new GestorSesionLocal(this);
        LimpiadorCarrito limpiadorCarrito = new LimpiadorCarritoLocal(this);
        ControladorCierreSesion controladorCierreSesion = new ControladorCierreSesion(gestorSesion, limpiadorCarrito);

        if (btnCerrarSesion != null) {
            btnCerrarSesion.setOnClickListener(v -> {
                controladorCierreSesion.cerrarSesion(() -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
            });
        }

        if (btnAgregarProducto != null) {
            Rol rol = gestorSesion.obtenerRol();
            if (rol == Rol.ADMINISTRADOR) {
                btnAgregarProducto.setVisibility(View.VISIBLE);
                btnAgregarProducto.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, CrearProductoActivity.class);
                    startActivity(intent);
                });
            } else {
                btnAgregarProducto.setVisibility(View.GONE);
            }
        }
    }

    public void openProductDetail(int productId) {
        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", productId);
        startActivity(intent);
    }
}