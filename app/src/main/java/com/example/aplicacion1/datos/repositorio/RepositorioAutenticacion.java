package com.example.aplicacion1.datos.repositorio;

import com.example.aplicacion1.datos.dto.autenticacion.RespuestaLogin;
import com.example.aplicacion1.datos.dto.autenticacion.SolicitudLogin;
import com.example.aplicacion1.datos.dto.autenticacion.UsuarioAutenticacion;
import com.example.aplicacion1.datos.remoto.autenticacion.ApiAutenticacion;
import com.example.aplicacion1.nucleo.red.ClienteApi;

import java.util.List;

import retrofit2.Call;

public class RepositorioAutenticacion {

    private final ApiAutenticacion apiAutenticacion;

    public RepositorioAutenticacion() {

        apiAutenticacion = ClienteApi
                .obtenerRetrofit()
                .create(ApiAutenticacion.class);
    }

    public Call<RespuestaLogin> iniciarSesion(
            String nombreUsuario,
            String contrasena
    ) {

        SolicitudLogin solicitudLogin =
                new SolicitudLogin(
                        nombreUsuario,
                        contrasena
                );

        return apiAutenticacion.iniciarSesion(solicitudLogin);
    }

    public Call<List<UsuarioAutenticacion>> obtenerUsuarios() {
        return apiAutenticacion.obtenerUsuarios();
    }
}