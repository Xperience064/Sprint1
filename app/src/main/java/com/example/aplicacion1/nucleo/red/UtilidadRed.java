package com.example.aplicacion1.nucleo.red;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public final class UtilidadRed {

    private UtilidadRed() {
        // Evita crear objetos de esta clase.
    }

    public static boolean hayConexionInternet(Context context) {

        ConnectivityManager administradorConexion =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE
                );

        if (administradorConexion == null) {
            return false;
        }

        Network redActiva = administradorConexion.getActiveNetwork();

        if (redActiva == null) {
            return false;
        }

        NetworkCapabilities capacidades =
                administradorConexion.getNetworkCapabilities(redActiva);

        if (capacidades == null) {
            return false;
        }

        return capacidades.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
        );
    }
}
