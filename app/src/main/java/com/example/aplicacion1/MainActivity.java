package com.example.aplicacion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ProgressBar;

import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacion1.adapter.ProductAdapter;
import com.example.aplicacion1.viewmodel.ProductViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import com.example.aplicacion1.funcionalidad.autenticacion.controlador.ControladorCierreSesion;
import com.example.aplicacion1.funcionalidad.autenticacion.vista.LoginActivity;
import com.example.aplicacion1.funcionalidad.crearproducto.vista.CrearProductoActivity;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.nucleo.carrito.LimpiadorCarrito;
import com.example.aplicacion1.nucleo.carrito.LimpiadorCarritoLocal;
import com.example.aplicacion1.nucleo.sesion.GestorSesion;
import com.example.aplicacion1.nucleo.sesion.GestorSesionLocal;

import com.example.aplicacion1.view.ProductDetailActivity;

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


        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Método para navegar a la pantalla de detalles de un producto (US05)
        // Llama a este método al hacer clic sobre un producto de tu catálogo
        // openProductDetail(1);
    }

    /**
     * Abre la actividad de detalle de producto enviando el ID del producto seleccionado.
     * @param productId ID del producto a consultar en la API.
     */
    public void openProductDetail(int productId) {
        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", productId);
        startActivity(intent);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        Button btnCerrarSesion =
                findViewById(R.id.btnCerrarSesion);

        Button btnAgregarProducto =
                findViewById(R.id.btnAgregarProducto);

        GestorSesion gestorSesion =
                new GestorSesionLocal(this);

        LimpiadorCarrito limpiadorCarrito =
                new LimpiadorCarritoLocal(this);

        ControladorCierreSesion controladorCierreSesion =
                new ControladorCierreSesion(
                        gestorSesion,
                        limpiadorCarrito
                );

        btnCerrarSesion.setOnClickListener(v -> {

            controladorCierreSesion.cerrarSesion(() -> {

                Intent intent = new Intent(
                        MainActivity.this,
                        LoginActivity.class
                );

                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                );

                startActivity(intent);
            });

        });

        Rol rol = gestorSesion.obtenerRol();

        if (rol == Rol.ADMINISTRADOR) {

            btnAgregarProducto.setVisibility(View.VISIBLE);

            btnAgregarProducto.setOnClickListener(v -> {

                Intent intent = new Intent(
                        MainActivity.this,
                        CrearProductoActivity.class
                );

                startActivity(intent);
            });

        } else {

            btnAgregarProducto.setVisibility(View.GONE);
        }

    }
}