package com.example.aplicacion1.nucleo.carrito;

import android.content.Context;
import android.content.SharedPreferences;

public class LimpiadorCarritoLocal implements LimpiadorCarrito {

    private static final String NOMBRE_PREFERENCIAS = "carrito_local";

    private final SharedPreferences preferencias;

    public LimpiadorCarritoLocal(Context context) {
        preferencias = context.getSharedPreferences(
                NOMBRE_PREFERENCIAS,
                Context.MODE_PRIVATE
        );
    }

    @Override
    public void limpiarCarrito() {
        preferencias.edit().clear().apply();
    }
}