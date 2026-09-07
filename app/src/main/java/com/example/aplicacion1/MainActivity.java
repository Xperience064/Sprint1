package com.example.aplicacion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicacion1.funcionalidad.crearproducto.vista.CrearProductoActivity;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.nucleo.sesion.GestorSesion;
import com.example.aplicacion1.nucleo.sesion.GestorSesionLocal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

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

        Button btnAgregarProducto =
                findViewById(R.id.btnAgregarProducto);

        GestorSesion gestorSesion =
                new GestorSesionLocal(this);

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