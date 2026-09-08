package com.example.aplicacion1.nucleo.sesion;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.aplicacion1.modelo.Rol;

public class GestorSesionLocal implements GestorSesion {

    private static final String NOMBRE_PREFERENCIAS = "sesion_usuario";

    private static final String CLAVE_TOKEN = "token";
    private static final String CLAVE_ID_USUARIO = "id_usuario";
    private static final String CLAVE_ROL = "rol";

    private final SharedPreferences preferencias;

    public GestorSesionLocal(Context context) {
        preferencias = context.getSharedPreferences(
                NOMBRE_PREFERENCIAS,
                Context.MODE_PRIVATE
        );
    }

    @Override
    public void guardarSesion(String token, int idUsuario, Rol rol) {

        preferencias.edit()
                .putString(CLAVE_TOKEN, token)
                .putInt(CLAVE_ID_USUARIO, idUsuario)
                .putString(CLAVE_ROL, rol.name())
                .apply();
    }

    @Override
    public String obtenerToken() {
        return preferencias.getString(CLAVE_TOKEN, null);
    }

    @Override
    public int obtenerIdUsuario() {
        return preferencias.getInt(CLAVE_ID_USUARIO, -1);
    }

    @Override
    public Rol obtenerRol() {

        String rolGuardado = preferencias.getString(CLAVE_ROL, null);

        if (rolGuardado == null) {
            return null;
        }

        try {
            return Rol.valueOf(rolGuardado);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean haySesionActiva() {
        return obtenerToken() != null
                && obtenerIdUsuario() != -1
                && obtenerRol() != null;
    }

    @Override
    public void cerrarSesion() {
        preferencias.edit()
                .clear()
                .apply();
    }
}