package com.example.aplicacion1;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicacion1.view.ProductDetailActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }
}