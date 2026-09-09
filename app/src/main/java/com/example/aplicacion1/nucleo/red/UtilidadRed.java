package com.example.aplicacion1.nucleo.red;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;;
import android.net.NetworkCapabilities;
import android.os.Build;

public final class UtilidadRed {

    private UtilidadRed() {
        // Evita crear objetos de esta clase.
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    @SuppressWarnings("deprecation")
    public static boolean hayConexionInternet(Context context) {
        if (context == null) {
            return false;
        }

        ConnectivityManager administradorConexion =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE
                );

        if (administradorConexion == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        } else {
            android.net.NetworkInfo redInfo = administradorConexion.getActiveNetworkInfo();
            return redInfo != null && redInfo.isConnected();
        }
    }
}