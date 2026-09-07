package view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import model.CartItem;
import controller.CartManager;

public class ProductDetailActivity extends AppCompatActivity {

    private String currentUserRole = "Auditor"; // Cambia a "Cliente" para probar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btnAddToCart = new Button(this);

        // US09 - Criterio 3: Ocultar botón si es Auditor
        if ("Auditor".equalsIgnoreCase(currentUserRole)) {
            btnAddToCart.setVisibility(View.GONE);
        } else {
            btnAddToCart.setVisibility(View.VISIBLE);
            btnAddToCart.setOnClickListener(v -> {
                // US09 - Criterio 1: Crear item y añadir al carrito
                CartItem item = new CartItem(1, "Producto Ejemplo", 29.99, 1);

                CartManager.getInstance().addItem(item,
                        () -> Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show(),
                        () -> Toast.makeText(this, "Error al comunicar con la API", Toast.LENGTH_SHORT).show()
                );
            });
        }
    }
}