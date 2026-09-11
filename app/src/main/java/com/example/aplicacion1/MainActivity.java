package com.example.aplicacion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacion1.adapter.ProductAdapter;
import com.example.aplicacion1.funcionalidad.autenticacion.controlador.ControladorCierreSesion;
import com.example.aplicacion1.funcionalidad.autenticacion.vista.LoginActivity;
import com.example.aplicacion1.funcionalidad.crearproducto.vista.CrearProductoActivity;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.nucleo.carrito.LimpiadorCarrito;
import com.example.aplicacion1.nucleo.carrito.LimpiadorCarritoLocal;
import com.example.aplicacion1.nucleo.sesion.GestorSesion;
import com.example.aplicacion1.nucleo.sesion.GestorSesionLocal;
import com.example.aplicacion1.view.ProductDetailActivity;
import com.example.aplicacion1.view.UsersActivity;
import com.example.aplicacion1.view.CartHistoryActivity;
import controller.CartManager;
import view.CartActivity;
import com.example.aplicacion1.viewmodel.ProductViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProductViewModel viewModel;
    private ProductAdapter adapter;
    private ProgressBar progressBar;
    private ChipGroup chipGroupCategories;
    private GestorSesion gestorSesion;
    private LinearLayout layoutError;
    private TextView tvCatalogError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupViewModel();
        setupSesion();
        loadCategories();

        viewModel.loadAllProducts();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        layoutError = findViewById(R.id.layoutError);
        tvCatalogError = findViewById(R.id.tvCatalogError);
        findViewById(R.id.btnRetryCatalog).setOnClickListener(v -> {
            layoutError.setVisibility(View.GONE);
            loadCategories();
            viewModel.loadAllProducts();
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new ProductAdapter(product -> openProductDetail(product.getId()));

        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this)
                .get(ProductViewModel.class);

        viewModel.getProductsLiveData().observe(
                this,
                products -> adapter.setProducts(products)
        );

        viewModel.getIsLoading().observe(this, isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
        viewModel.getError().observe(this, message -> {
            if (message == null) layoutError.setVisibility(View.GONE);
            else { tvCatalogError.setText(message); layoutError.setVisibility(View.VISIBLE); }
        });
    }

    private void loadCategories() {
        viewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                setupChips(categories);
            } else {
                tvCatalogError.setText("No se pudieron cargar las categorías");
                layoutError.setVisibility(View.VISIBLE);
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

        chipGroupCategories.setOnCheckedChangeListener(
                (group, checkedId) -> {

                    Chip selectedChip =
                            group.findViewById(checkedId);

                    if (selectedChip == null
                            || selectedChip.getText()
                            .toString()
                            .equals("Ver todos")) {

                        viewModel.loadAllProducts();

                    } else {

                        viewModel.filterByCategory(
                                selectedChip.getText().toString()
                        );
                    }
                }
        );
    }

    private void setupSesion() {
        View btnAgregarProducto = findViewById(R.id.btnAgregarProducto);

        gestorSesion = new GestorSesionLocal(this);

        LimpiadorCarrito limpiadorCarrito =
                new LimpiadorCarritoLocal(this);

        ControladorCierreSesion controladorCierreSesion =
                new ControladorCierreSesion(
                        gestorSesion,
                        limpiadorCarrito
                );

        Rol rol = gestorSesion.obtenerRol();
        CartManager.getInstance().initialize(this, gestorSesion.obtenerIdUsuario());

        BottomNavigationView navigation = findViewById(R.id.bottomNavigation);
        navigation.getMenu().findItem(R.id.navCart).setVisible(rol == Rol.CLIENTE);
        navigation.getMenu().findItem(R.id.navUsers).setVisible(rol == Rol.ADMINISTRADOR || rol == Rol.AUDITOR);
        navigation.getMenu().findItem(R.id.navHistory).setVisible(rol == Rol.ADMINISTRADOR || rol == Rol.AUDITOR);
        navigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navCatalog) return true;
            if (id == R.id.navCart) startActivity(new Intent(this, CartActivity.class));
            else if (id == R.id.navUsers) startActivity(new Intent(this, UsersActivity.class));
            else if (id == R.id.navHistory) startActivity(new Intent(this, CartHistoryActivity.class));
            else if (id == R.id.navLogout) {
                new AlertDialog.Builder(this)
                        .setTitle("Cerrar sesión")
                        .setMessage("¿Deseas cerrar tu sesión?")
                        .setNegativeButton("Cancelar", (dialog, which) -> navigation.setSelectedItemId(R.id.navCatalog))
                        .setPositiveButton("Cerrar sesión", (dialog, which) -> cerrarSesion(controladorCierreSesion))
                        .show();
            }
            return true;
        });

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

    private void cerrarSesion(ControladorCierreSesion controlador) {
        controlador.cerrarSesion(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    public void openProductDetail(int productId) {
        Intent intent = new Intent(
                MainActivity.this,
                ProductDetailActivity.class
        );

        intent.putExtra(
                "PRODUCT_ID",
                productId
        );

        startActivity(intent);
    }
}
