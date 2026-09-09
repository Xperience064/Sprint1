package view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import controller.CartManager;
import model.CartItem;

public class ProductDetailActivity extends AppCompatActivity {

    private String currentUserRole = "Cliente"; // Cambia a "Auditor" para probar el ocultamiento

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crear un contenedor base centrado para mostrar el botón
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(32, 32, 32, 32);

        Button btnAddToCart = new Button(this);
        btnAddToCart.setText("Añadir al Carrito");

        // US09 - Criterio 3: Ocultar botón si es Auditor
        if ("Auditor".equalsIgnoreCase(currentUserRole)) {
            btnAddToCart.setVisibility(View.GONE);
        } else {
            btnAddToCart.setVisibility(View.VISIBLE);
            btnAddToCart.setOnClickListener(v -> {
                // US09 - Criterio 1: Crear item y añadir al carrito
                CartItem item = new CartItem(1, "Producto Ejemplo", 29.99, 1);

                CartManager.getInstance().addItem(item,
                        () -> {
                            Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show();

                            // Navegar hacia la pantalla del Carrito
                            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                            startActivity(intent);
                        },
                        () -> Toast.makeText(this, "Error al comunicar con la API", Toast.LENGTH_SHORT).show()
                );
            });
        }

        // Agregar el botón al layout y renderizarlo en pantalla
        layout.addView(btnAddToCart);
        setContentView(layout);
    }
}