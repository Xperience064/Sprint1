package com.example.aplicacion1.funcionalidad.autenticacion.vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicacion1.MainActivity;
import com.example.aplicacion1.R;
import com.example.aplicacion1.datos.repositorio.RepositorioAutenticacion;
import com.example.aplicacion1.funcionalidad.autenticacion.controlador.ControladorLogin;
import com.example.aplicacion1.modelo.Rol;
import com.example.aplicacion1.nucleo.seguridad.MapeadorRol;
import com.example.aplicacion1.nucleo.sesion.GestorSesion;
import com.example.aplicacion1.nucleo.sesion.GestorSesionLocal;

public class LoginActivity extends AppCompatActivity
        implements ControladorLogin.ResultadoLogin {

    private EditText edtUsuario;
    private EditText edtContrasena;
    private TextView txtError;
    private ProgressBar progresoLogin;
    private Button btnIniciarSesion;

    private ControladorLogin controladorLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarComponentes();
        inicializarControlador();
        configurarEventos();
    }

    private void inicializarComponentes() {

        edtUsuario = findViewById(R.id.edtUsuario);
        edtContrasena = findViewById(R.id.edtContrasena);
        txtError = findViewById(R.id.txtError);
        progresoLogin = findViewById(R.id.progresoLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
    }

    private void inicializarControlador() {

        RepositorioAutenticacion repositorioAutenticacion =
                new RepositorioAutenticacion();

        GestorSesion gestorSesion =
                new GestorSesionLocal(this);

        MapeadorRol mapeadorRol =
                new MapeadorRol();

        controladorLogin = new ControladorLogin(
                repositorioAutenticacion,
                gestorSesion,
                mapeadorRol
        );
    }

    private void configurarEventos() {

        btnIniciarSesion.setOnClickListener(v -> {

            ocultarError();

            String usuario =
                    edtUsuario.getText()
                            .toString()
                            .trim();

            String contrasena =
                    edtContrasena.getText()
                            .toString();

            controladorLogin.iniciarSesion(
                    this,
                    usuario,
                    contrasena,
                    this
            );
        });
    }

    @Override
    public void cargando(boolean estaCargando) {

        if (estaCargando) {

            progresoLogin.setVisibility(View.VISIBLE);
            btnIniciarSesion.setEnabled(false);

        } else {

            progresoLogin.setVisibility(View.GONE);
            btnIniciarSesion.setEnabled(true);
        }
    }

    @Override
    public void exito(Rol rol) {

        Intent intent =
                new Intent(
                        LoginActivity.this,
                        MainActivity.class
                );

        startActivity(intent);

        finish();
    }

    @Override
    public void credencialesIncorrectas() {

        mostrarError(
                "Usuario o contraseña inválidos"
        );
    }

    @Override
    public void sinConexion() {

        mostrarError(
                "No tienes conexión a Internet"
        );
    }

    @Override
    public void camposInvalidos() {

        mostrarError(
                "Ingresa tu usuario y contraseña"
        );
    }

    @Override
    public void error(String mensaje) {

        mostrarError(mensaje);
    }

    private void mostrarError(String mensaje) {

        txtError.setText(mensaje);
        txtError.setVisibility(View.VISIBLE);
    }

    private void ocultarError() {

        txtError.setText("");
        txtError.setVisibility(View.GONE);
    }
}