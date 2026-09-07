package com.example.aplicacion1.datos.dto.autenticacion;

import com.google.gson.annotations.SerializedName;

public class RespuestaLogin {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }
}