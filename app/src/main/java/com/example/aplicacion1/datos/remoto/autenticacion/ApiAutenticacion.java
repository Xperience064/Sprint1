package com.example.aplicacion1.datos.remoto.autenticacion;

import com.example.aplicacion1.datos.dto.autenticacion.RespuestaLogin;
import com.example.aplicacion1.datos.dto.autenticacion.SolicitudLogin;
import com.example.aplicacion1.datos.dto.autenticacion.UsuarioAutenticacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiAutenticacion {

    @POST("auth/login")
    Call<RespuestaLogin> iniciarSesion(
            @Body SolicitudLogin solicitudLogin
    );

    @GET("users")
    Call<List<UsuarioAutenticacion>> obtenerUsuarios();
}