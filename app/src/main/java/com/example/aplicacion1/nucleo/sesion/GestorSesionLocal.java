package com.example.aplicacion1.nucleo.sesion;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.aplicacion1.modelo.Rol;

public class GestorSesionLocal implements GestorSesion {

    private static final String NOMBRE_PREFERENCIAS = "sesion_usuario_segura_v2";

    private static final String CLAVE_TOKEN = "token";
    private static final String CLAVE_ID_USUARIO = "id_usuario";
    private static final String CLAVE_ROL = "rol";

    private final SharedPreferences preferencias;

    public GestorSesionLocal(Context context) {
        SharedPreferences almacenamientoSeguro;
        try {
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            almacenamientoSeguro = EncryptedSharedPreferences.create(
                    NOMBRE_PREFERENCIAS,
                    masterKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception errorCifrado) {
            context.deleteSharedPreferences(NOMBRE_PREFERENCIAS);
            try {
                String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
                almacenamientoSeguro = EncryptedSharedPreferences.create(
                        NOMBRE_PREFERENCIAS, masterKey, context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
            } catch (Exception segundoError) {
                throw new IllegalStateException("No fue posible recuperar la sesión segura", segundoError);
            }
        }
        preferencias = almacenamientoSeguro;
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
