package com.example.aplicacion1.nucleo.red;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ClienteApi {

    private static final String URL_BASE = "https://fakestoreapi.com/";

    private static Retrofit retrofit;

    private ClienteApi() {
        // Evita que esta clase sea instanciada.
    }

    public static Retrofit obtenerRetrofit() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}