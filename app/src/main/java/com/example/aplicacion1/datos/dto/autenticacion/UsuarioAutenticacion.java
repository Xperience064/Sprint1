package com.example.aplicacion1.datos.dto.autenticacion;

import com.google.gson.annotations.SerializedName;

public class UsuarioAutenticacion {

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String nombreUsuario;

    public int getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}