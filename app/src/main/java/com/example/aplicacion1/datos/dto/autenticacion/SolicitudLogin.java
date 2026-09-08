package com.example.aplicacion1.datos.dto.autenticacion;

import com.google.gson.annotations.SerializedName;

public class SolicitudLogin {

    @SerializedName("username")
    private String nombreUsuario;

    @SerializedName("password")
    private String contrasena;

    public SolicitudLogin(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }
}