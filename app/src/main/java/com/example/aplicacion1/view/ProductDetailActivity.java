package com.example.aplicacion1.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.aplicacion1.R;
import com.example.aplicacion1.model.Product;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.network.RetrofitClient;
import com.example.aplicacion1.nucleo.sesion.GestorSesionLocal;
import com.example.aplicacion1.viewmodel.ProductDetailViewModel;
import com.example.aplicacion1.funcionalidad.crearproducto.vista.CrearProductoActivity;

import controller.CartManager;
import model.CartItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import view.CartActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvProductTitle, tvProductCategory, tvProductPrice, tvProductDescription;
    private LinearLayout layoutAdminButtons;

    private ProductDetailViewModel viewModel;
    private GestorSesionLocal gestorSesion;
    private int productId;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();

        gestorSesion = new GestorSesionLocal(this);
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
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
        currentProduct = product;
        // Escenario 1: Datos estándar
        tvProductTitle.setText(product.getTitle());
        tvProductCategory.setText(product.getCategory());
        tvProductPrice.setText(String.format("$%.2f", product.getPrice()));
        tvProductDescription.setText(product.getDescription());

        Glide.with(this)
                .load(product.getImageUrl())
                .into(ivProductImage);

        // Regla de Negocio: Validar sesión local para controles de Admin
        Rol rol = gestorSesion.obtenerRol();
        if (rol == Rol.ADMINISTRADOR) {
            // Escenario 2: Agregar controles dinámicos (sin instanciar ocultos)
            setupAdminControls();
        } else if (rol == Rol.CLIENTE) {
            setupClientControls(product);
        }
    }

    private void setupClientControls(Product product) {
        layoutAdminButtons.removeAllViews();
        Button btnAdd = new Button(this);
        btnAdd.setText("Añadir al carrito");
        btnAdd.setOnClickListener(v -> CartManager.getInstance().addItem(
                new CartItem(product.getId(), product.getTitle(), product.getPrice(), 1, product.getImage()),
                () -> runOnUiThread(() -> {
                    Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, CartActivity.class));
                }),
                () -> runOnUiThread(() -> Toast.makeText(this, "No se pudo actualizar el carrito", Toast.LENGTH_SHORT).show())
        ));
        layoutAdminButtons.addView(btnAdd);
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
            if (currentProduct == null) return;
            Intent intent = new Intent(this, CrearProductoActivity.class);
            intent.putExtra(CrearProductoActivity.EXTRA_MODO_EDITAR, true);
            intent.putExtra(CrearProductoActivity.EXTRA_ID, currentProduct.getId());
            intent.putExtra("titulo", currentProduct.getTitle());
            intent.putExtra("precio", currentProduct.getPrice());
            intent.putExtra("descripcion", currentProduct.getDescription());
            intent.putExtra("imagen", currentProduct.getImage());
            intent.putExtra("categoria", currentProduct.getCategory());
            startActivity(intent);
        });

        Button btnDelete = new Button(this);
        btnDelete.setText("Eliminar");
        LinearLayout.LayoutParams paramsDelete = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        paramsDelete.setMargins(8, 0, 0, 0);
        btnDelete.setLayoutParams(paramsDelete);
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar producto")
                    .setMessage("¿Seguro que deseas eliminar este producto?")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Eliminar", (dialog, which) -> deleteProduct())
                    .show();
        });

        layoutAdminButtons.addView(btnEdit);
        layoutAdminButtons.addView(btnDelete);
    }

    private void deleteProduct() {
        RetrofitClient.getApiService().deleteProduct(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleError() {
        Toast.makeText(this, "Producto no disponible", Toast.LENGTH_LONG).show();
        finish(); // Retorna automáticamente a la pantalla del catálogo
    }
}
