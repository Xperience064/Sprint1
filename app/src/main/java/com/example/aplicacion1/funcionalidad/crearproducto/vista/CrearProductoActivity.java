package com.example.aplicacion1.funcionalidad.crearproducto.vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicacion1.MainActivity;
import com.example.aplicacion1.R;
import com.example.aplicacion1.datos.repositorio.RepositorioProducto;
import com.example.aplicacion1.funcionalidad.crearproducto.controlador.ControladorCrearProducto;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.nucleo.sesion.GestorSesion;
import com.example.aplicacion1.nucleo.sesion.GestorSesionLocal;

public class CrearProductoActivity extends AppCompatActivity
        implements ControladorCrearProducto.ResultadoCrearProducto {

    private EditText edtTituloProducto;
    private EditText edtPrecioProducto;
    private EditText edtDescripcionProducto;
    private EditText edtImagenProducto;
    private EditText edtCategoriaProducto;
    private TextView txtErrorCrearProducto;
    private ProgressBar progresoCrearProducto;
    private Button btnCrearProducto;

    private ControladorCrearProducto controladorCrearProducto;
    private GestorSesion gestorSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestorSesion = new GestorSesionLocal(this);

        if (gestorSesion.obtenerRol() != Rol.ADMINISTRADOR) {

            Toast.makeText(
                    this,
                    "Acceso permitido únicamente para administradores",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    CrearProductoActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_crear_producto);

        inicializarComponentes();
        inicializarControlador();
        configurarEventos();
    }

    private void inicializarComponentes() {

        edtTituloProducto =
                findViewById(R.id.edtTituloProducto);

        edtPrecioProducto =
                findViewById(R.id.edtPrecioProducto);

        edtDescripcionProducto =
                findViewById(R.id.edtDescripcionProducto);

        edtImagenProducto =
                findViewById(R.id.edtImagenProducto);

        edtCategoriaProducto =
                findViewById(R.id.edtCategoriaProducto);

        txtErrorCrearProducto =
                findViewById(R.id.txtErrorCrearProducto);

        progresoCrearProducto =
                findViewById(R.id.progresoCrearProducto);

        btnCrearProducto =
                findViewById(R.id.btnCrearProducto);
    }

    private void inicializarControlador() {

        RepositorioProducto repositorioProducto =
                new RepositorioProducto();

        controladorCrearProducto =
                new ControladorCrearProducto(
                        repositorioProducto,
                        gestorSesion
                );
    }

    private void configurarEventos() {

        btnCrearProducto.setOnClickListener(v -> {

            ocultarError();

            controladorCrearProducto.crearProducto(
                    edtTituloProducto.getText().toString(),
                    edtPrecioProducto.getText().toString(),
                    edtDescripcionProducto.getText().toString(),
                    edtImagenProducto.getText().toString(),
                    edtCategoriaProducto.getText().toString(),
                    this
            );
        });
    }

    @Override
    public void cargando(boolean estaCargando) {

        if (estaCargando) {
            progresoCrearProducto.setVisibility(View.VISIBLE);
            btnCrearProducto.setEnabled(false);
        } else {
            progresoCrearProducto.setVisibility(View.GONE);
            btnCrearProducto.setEnabled(true);
        }
    }

    @Override
    public void productoCreado(int idProducto) {

        Toast.makeText(
                this,
                "Producto creado. ID: " + idProducto,
                Toast.LENGTH_LONG
        ).show();

        limpiarFormulario();
    }

    @Override
    public void datosInvalidos(String mensaje) {
        mostrarError(mensaje);
    }

    @Override
    public void accesoDenegado() {

        Toast.makeText(
                this,
                "No tienes permiso para crear productos",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                CrearProductoActivity.this,
                MainActivity.class
        );

        startActivity(intent);
        finish();
    }

    @Override
    public void error(String mensaje) {
        mostrarError(mensaje);
    }

    private void limpiarFormulario() {

        edtTituloProducto.setText("");
        edtPrecioProducto.setText("");
        edtDescripcionProducto.setText("");
        edtImagenProducto.setText("");
        edtCategoriaProducto.setText("");

        ocultarError();
    }

    private void mostrarError(String mensaje) {

        txtErrorCrearProducto.setText(mensaje);
        txtErrorCrearProducto.setVisibility(View.VISIBLE);
    }

    private void ocultarError() {

        txtErrorCrearProducto.setText("");
        txtErrorCrearProducto.setVisibility(View.GONE);
    }
}