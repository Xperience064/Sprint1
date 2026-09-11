package com.example.aplicacion1.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    private static final String BASE_URL = "https://fakestoreapi.com/";
    private static Retrofit retrofit;
    private static ApiService apiService;

    private RetrofitClient() {
    }

    public static ApiService getApiService() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        if (apiService == null) {
            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }
}